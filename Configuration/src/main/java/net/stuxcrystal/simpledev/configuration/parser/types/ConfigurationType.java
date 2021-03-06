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

package net.stuxcrystal.simpledev.configuration.parser.types;

import net.stuxcrystal.simpledev.configuration.parser.ConfigurationParser;
import net.stuxcrystal.simpledev.configuration.parser.ValueType;
import net.stuxcrystal.simpledev.configuration.parser.annotations.Configuration;
import net.stuxcrystal.simpledev.configuration.parser.annotations.Value;
import net.stuxcrystal.simpledev.configuration.parser.exceptions.ValueException;
import net.stuxcrystal.simpledev.configuration.parser.node.MapNode;
import net.stuxcrystal.simpledev.configuration.parser.node.Node;
import net.stuxcrystal.simpledev.configuration.parser.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

public class ConfigurationType implements ValueType<Object> {

    @Override
    public boolean isValidType(Object object, Type cls) throws ReflectiveOperationException {
        return ReflectionUtil.toClass(cls).isAnnotationPresent(Configuration.class);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object parse(Object object, ConfigurationParser parser, Type type, Node<?> value) throws ReflectiveOperationException, ValueException {
        Class<?> cls = ReflectionUtil.toClass(type);
        Object result = ReflectionUtil.newInstance(cls);

        for (Field f : cls.getDeclaredFields()) {
            if (Modifier.isTransient(f.getModifiers())) continue;                // Ignore Transient values.
            if (Modifier.isStatic(f.getModifiers())) continue;                   // Ignore Static fields.

            if (!f.isAccessible()) f.setAccessible(true);                        // Set the field accessible.

            String name = f.getName();
            if (f.isAnnotationPresent(Value.class)) {
                Value _value = f.getAnnotation(Value.class);
                name = _value.name().isEmpty() ? name : _value.name();

                if (_value.transientValue()) continue;
            }

            try {
                f.set(result, parser.parseObject(result, f.getGenericType(), getNode(((Node<Node<?>[]>) value), name)));
            } catch (NoSuchElementException e) {
                parser.getConfigurationLoader().getLoggingInterface().debug("Node not found...");
                parser.getConfigurationLoader().getLoggingInterface().debugException(e);
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
    public Node<?> dump(Object object, ConfigurationParser parser, Type type, Object data) throws ReflectiveOperationException, ValueException {
        Class<?> cls = ReflectionUtil.toClass(type);

        MapNode parent = new MapNode(null);
        List<Node<?>> nodes = new ArrayList<>();
        for (Field f : cls.getDeclaredFields()) {
            if (Modifier.isTransient(f.getModifiers())) continue;                // Ignore Transient values.
            if (Modifier.isStatic(f.getModifiers())) continue;                   // Ignore Static fields.

            if (!f.isAccessible()) f.setAccessible(true);                        // Set the field accessible.

            String name = f.getName();
            String[] comments = cls.getAnnotation(Configuration.class).header();             // Use the default header.
            if (f.isAnnotationPresent(Value.class)) {
                Value value = f.getAnnotation(Value.class);
                name = value.name().isEmpty() ? name : value.name();
                comments = value.comment();

                if (value.transientValue()) continue;
            }

            Node<?> node = parser.dumpObject(data, f.getGenericType(), f.get(data));
            node.setName(name);
            node.setComments(comments);
            node.setParent(parent);

            nodes.add(node);
        }

        parent.setData(nodes.toArray(new Node<?>[nodes.size()]));
        return parent;
    }

}
