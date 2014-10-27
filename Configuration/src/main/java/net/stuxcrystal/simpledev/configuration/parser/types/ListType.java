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
import net.stuxcrystal.simpledev.configuration.parser.exceptions.ValueException;
import net.stuxcrystal.simpledev.configuration.parser.node.ArrayNode;
import net.stuxcrystal.simpledev.configuration.parser.node.MapNode;
import net.stuxcrystal.simpledev.configuration.parser.node.Node;
import net.stuxcrystal.simpledev.configuration.parser.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Description
 *
 * @author net.stuxcrystal
 */
public class ListType implements ValueType<List<?>> {
    @Override
    public boolean isValidType(Object object, Type cls) throws ReflectiveOperationException {
        return ReflectionUtil.toClass(cls).isAssignableFrom(List.class) && ReflectionUtil.getGenericArguments(cls).length == 1;
    }

    @Override
    public List<?> parse(Object object, ConfigurationParser parser, Type cls, Node<?> value) throws ReflectiveOperationException, ValueException {
        Node<?>[] children = ((Node<Node<?>[]>) value).getData();
        if (children == null) return new ArrayList(0);

        Type type = ReflectionUtil.getGenericArguments(cls)[0];
        List data = new ArrayList(children.length);

        for (Node<?> node : children) {
            data.add(parser.parseObject(object, type, node));
        }

        return data;
    }

    @Override
    public Node<?> dump(Object object, ConfigurationParser parser, Type cls, List<?> data) throws ReflectiveOperationException, ValueException {
        Type type = ReflectionUtil.getGenericArguments(cls)[0];

        Node<?>[] children = new Node<?>[data.size()];
        MapNode parent = new ArrayNode(null);
        for (int i = 0; i < data.size(); i++) {
            Object obj = data.get(i);
            Node<?> node = parser.dumpObject(object, type, obj);
            node.setParent(parent);
            node.setComments(new String[0]);
            children[i] = node;
        }
        parent.setData(children);
        return parent;

    }
}
