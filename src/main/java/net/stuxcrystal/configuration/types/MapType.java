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
import net.stuxcrystal.configuration.exceptions.ValueException;
import net.stuxcrystal.configuration.node.MapNode;
import net.stuxcrystal.configuration.node.Node;
import net.stuxcrystal.configuration.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Description
 *
 * @author stuxcrystal
 */
public class MapType implements ValueType<Map<String, ?>> {

    @Override
    public boolean isValidType(Object object, Field field, Type cls) throws ReflectiveOperationException {
        if (!ReflectionUtil.toClass(cls).isAssignableFrom(Map.class))
            return false;

        Type[] t = ReflectionUtil.getGenericArguments(cls);
        if (t.length != 2)
            return false;

        return ReflectionUtil.toClass(t[0]).isAssignableFrom(String.class);

    }

    @Override
    public Map<String, ?> parse(Object object, Field field, ConfigurationParser parser, Type cls, Node<?> value) throws ReflectiveOperationException, ValueException {
        if (!value.hasChildren())
            throw new ValueException("The node has no values.");

        Map map = new LinkedHashMap();
        Type t = ReflectionUtil.getGenericArguments(cls)[1];
        Node<?>[] children = ((Node<Node<?>[]>) value).getData();
        for (Node<?> node : children) {
            map.put(node.getName(), parser.parseObject(object, field, t, node));
        }

        return map;
    }

    @Override
    public Node<?> dump(Object object, Field field, ConfigurationParser parser, Type cls, Object data) throws ReflectiveOperationException, ValueException {
        Type t = ReflectionUtil.getGenericArguments(cls)[1];

        Node<?>[] children = new Node<?>[((Map<String, ?>) data).size()];
        MapNode parent = new MapNode(null);

        int i = 0;
        for (Map.Entry<String, ?> entry : ((Map<String, ?>) data).entrySet()) {
            Object obj = entry.getValue();
            Node<?> node = parser.dumpObject(object, field, t, obj);
            node.setParent(parent);
            node.setComments(new String[0]);
            node.setName(entry.getKey());
            children[i++] = node;
        }
        parent.setData(children);
        return parent;
    }
}
