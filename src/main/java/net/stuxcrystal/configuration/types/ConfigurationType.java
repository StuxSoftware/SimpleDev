/*
 * Copyright 2013 StuxCrystal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package net.stuxcrystal.configuration.types;

import net.stuxcrystal.configuration.ConfigurationParser;
import net.stuxcrystal.configuration.ValueType;
import net.stuxcrystal.configuration.annotations.Configuration;
import net.stuxcrystal.configuration.annotations.Value;
import net.stuxcrystal.configuration.exceptions.ValueException;
import net.stuxcrystal.configuration.node.MapNode;
import net.stuxcrystal.configuration.node.Node;
import net.stuxcrystal.configuration.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ConfigurationType implements ValueType<Object> {

    @Override
    public boolean isValidType(Object object, Field field, Type cls) throws ReflectiveOperationException {
        return ReflectionUtil.toClass(cls).isAnnotationPresent(Configuration.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object parse(Object object, Field field, ConfigurationParser parser, Type type, Node<?> value) throws ReflectiveOperationException, ValueException {
        Class<?> cls = ReflectionUtil.toClass(type);
        if (!value.hasChildren())
            throw new ValueException("The node does not have any subnodes.");

        Object result = ReflectionUtil.newInstance(cls);

        for (Field f : cls.getDeclaredFields()) {
            // if (f.isAnnotationPresent(Transient.class)) continue;    // Use the transient keyword to indicate that the value is transient.
            if (Modifier.isTransient(f.getModifiers())) continue;
            if (!f.isAccessible()) f.setAccessible(true);

            String name = f.getName();
            if (f.isAnnotationPresent(Value.class)) {
                Value _value = f.getAnnotation(Value.class);
                name = _value.name().isEmpty() ? name : _value.name();
            }

            try {
                f.set(result, parser.parseObject(result, f, f.getGenericType(), getNode(((Node<Node<?>[]>) value), name)));
            } catch (NoSuchElementException e) {
                // Should this message be removed?
                e.printStackTrace();
            }
        }

        return result;
    }

    private Node<?> getNode(Node<Node<?>[]> nodes, String name) {
        for (Node<?> node : nodes.getData()) {
            if (name.equalsIgnoreCase(node.getName()))
                return node;
        }

        throw new NoSuchElementException("Node not found: " + name + "@" + nodes);
    }

    @Override
    public Node<?> dump(Object object, Field field, ConfigurationParser parser, Type type, Object data) throws ReflectiveOperationException, ValueException {
        Class<?> cls = ReflectionUtil.toClass(type);

        MapNode parent = new MapNode(null);
        List<Node<?>> nodes = new ArrayList<>();                 // Use the transient keyword to indicate that the value is transient.
        for (Field f : cls.getDeclaredFields()) {
            // if (f.isAnnotationPresent(Transient.class)) continue;
            if (Modifier.isTransient(f.getModifiers())) continue;
            if (!f.isAccessible()) f.setAccessible(true);



            String name = f.getName();
            String[] comments = cls.getAnnotation(Configuration.class).header();             // Use the default header.
            if (f.isAnnotationPresent(Value.class)) {
                Value value = f.getAnnotation(Value.class);
                name = value.name().isEmpty() ? name : value.name();
                comments = value.comment();
            }

            Node<?> node = parser.dumpObject(data, f, f.getGenericType(), f.get(data));
            node.setName(name);
            node.setComments(comments);
            node.setParent(parent);

            nodes.add(node);
        }

        parent.setData(nodes.toArray(new Node<?>[nodes.size()]));
        return parent;
    }

}
