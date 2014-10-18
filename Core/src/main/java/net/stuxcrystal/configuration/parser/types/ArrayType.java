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

package net.stuxcrystal.configuration.parser.types;

import net.stuxcrystal.configuration.parser.ConfigurationParser;
import net.stuxcrystal.configuration.parser.ValueType;
import net.stuxcrystal.configuration.parser.exceptions.ValueException;
import net.stuxcrystal.configuration.parser.node.ArrayNode;
import net.stuxcrystal.configuration.parser.node.MapNode;
import net.stuxcrystal.configuration.parser.node.Node;
import net.stuxcrystal.configuration.parser.utils.ReflectionUtil;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

public class ArrayType implements ValueType<Object> {

    @Override
    public boolean isValidType(Object object, Field field, Type cls) throws ReflectiveOperationException {
        return ReflectionUtil.toClass(cls).isArray();
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object parse(Object object, Field field, ConfigurationParser parser, Type type, Node<?> value) throws ReflectiveOperationException, ValueException {
        Class<?> cls = ReflectionUtil.toClass(type);

        if (!value.hasChildren())
            throw new ValueException("The node has no values.");

        Type component;
        if (type instanceof GenericArrayType)
            component = ((GenericArrayType) type).getGenericComponentType();
        else
            component = ReflectionUtil.toClass(type).getComponentType();

        Node<?>[] children = ((Node<Node<?>[]>) value).getData();
        Object result = Array.newInstance(cls.getComponentType(), children.length);
        for (int i = 0; i < children.length; i++) {
            Array.set(result, i, parser.parseObject(object, field, component, children[i]));
        }

        return result;
    }

    @Override
    public Node<?> dump(Object object, Field field, ConfigurationParser parser, Type type, Object data) throws ReflectiveOperationException, ValueException {
        Type component;

        if (type instanceof GenericArrayType)
            component = ((GenericArrayType) type).getGenericComponentType();
        else
            component = ReflectionUtil.toClass(type).getComponentType();

        int length = Array.getLength(data);

        Node<?>[] children = new Node<?>[length];
        MapNode parent = new ArrayNode(null);
        for (int i = 0; i < length; i++) {
            Object obj = Array.get(data, i);
            Node<?> node = parser.dumpObject(object, field, component, obj);
            node.setParent(parent);
            node.setComments(new String[0]);
            children[i] = node;
        }
        parent.setData(children);
        return parent;
    }

}
