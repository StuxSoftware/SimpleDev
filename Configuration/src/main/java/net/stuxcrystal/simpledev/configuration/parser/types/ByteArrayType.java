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
import net.stuxcrystal.simpledev.configuration.parser.utils.Base64;
import net.stuxcrystal.simpledev.configuration.parser.utils.ReflectionUtil;

import java.lang.reflect.Type;

/**
 * Parses a byte array.
 */
public class ByteArrayType implements ValueType<byte[]> {

    @Override
    public boolean isValidType(Object object, Type cls) throws ReflectiveOperationException {
        Class<?> rawCls = ReflectionUtil.toClass(cls);
        return rawCls.isArray() && (rawCls.getComponentType().equals(Byte.class) || rawCls.getComponentType().equals(Byte.TYPE));

    }

    @Override
    public byte[] parse(Object object, ConfigurationParser parser, Type cls, Node<?> value) throws ReflectiveOperationException, ValueException {
        return Base64.decode(((Node<String>) value).getData());
    }

    @Override
    public Node<?> dump(Object object, ConfigurationParser parser, Type cls, byte[] data) throws ReflectiveOperationException, ValueException {
        return new DataNode(new String(Base64.encode(data)));
    }
}
