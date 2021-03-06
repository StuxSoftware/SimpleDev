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
import net.stuxcrystal.simpledev.configuration.parser.node.Node;
import net.stuxcrystal.simpledev.configuration.parser.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class EnumType implements ValueType<Enum<?>> {

    @Override
    public boolean isValidType(Object object, Type cls) throws ReflectiveOperationException {
        return ReflectionUtil.toClass(cls).isEnum();
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Enum<?> parse(Object object, ConfigurationParser parser, Type type, Node<?> value) throws ReflectiveOperationException, ValueException {
        Class<?> cls = ReflectionUtil.toClass(type);
        return (Enum<?>) convert(((Node<String>) value).getData(), cls);
    }

    @Override
    public Node<?> dump(Object object, ConfigurationParser parser, Type type, Enum<?> data) throws ReflectiveOperationException, ValueException {
        return new DataNode(data.toString());
    }

    @SuppressWarnings("unchecked")
    public Object convert(String value, Class<?> toClass) {
        return Enum.valueOf((Class<Enum>) toClass, value);
    }
}
