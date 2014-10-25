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
import net.stuxcrystal.simpledev.configuration.parser.node.DataNode;
import net.stuxcrystal.simpledev.configuration.parser.node.MapNode;
import net.stuxcrystal.simpledev.configuration.parser.node.Node;
import net.stuxcrystal.simpledev.configuration.parser.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Parses and dumps maps.
 *
 * @author net.stuxcrystal
 */
public class MapType implements ValueType<Map<String, ?>> {

    @Override
    public boolean isValidType(Object object, Field field, Type cls) throws ReflectiveOperationException {
        if (!ReflectionUtil.toClass(cls).isAssignableFrom(Map.class))
            return false;

        Type[] t = ReflectionUtil.getGenericArguments(cls);
        return t.length == 2;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, ?> parse(Object object, Field field, ConfigurationParser parser, Type cls, Node<?> node) throws ReflectiveOperationException, ValueException {
        Map map = new LinkedHashMap();
        Type tKey = ReflectionUtil.getGenericArguments(cls)[0];
        Type tValue = ReflectionUtil.getGenericArguments(cls)[1];
        Node<?>[] children = ((Node<Node<?>[]>) node).getData();
        for (Node<?> cNode : children) {
            Object key = parser.parseObject(object, field, tKey, new DataNode(cNode.getName()));
            Object value = parser.parseObject(object, field, tValue, cNode);
            map.put(key, value);
        }

        return map;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Node<?> dump(Object object, Field field, ConfigurationParser parser, Type cls, Object data) throws ReflectiveOperationException, ValueException {
        Type tKey = ReflectionUtil.getGenericArguments(cls)[0];
        Type tValue = ReflectionUtil.getGenericArguments(cls)[1];

        Node<?>[] children = new Node<?>[((Map<?, ?>) data).size()];
        MapNode parent = new MapNode(null);

        int i = 0;
        for (Map.Entry<?, ?> entry : ((Map<?, ?>) data).entrySet()) {
            Object value = entry.getValue();
            Object rawKey = entry.getKey();
            Node<?> node = parser.dumpObject(object, field, tValue, value);
            node.setParent(parent);
            node.setComments(new String[0]);
            try {
                node.setName(((Node<String>) parser.dumpObject(object, field, tKey, rawKey)).getData());
            } catch (ClassCastException e) {
                throw new ValueException("Maps only support simple data types.", e);
            }
            children[i++] = node;
        }
        parent.setData(children);
        return parent;
    }
}
