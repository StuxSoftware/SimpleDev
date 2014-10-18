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
import net.stuxcrystal.configuration.parser.node.DataNode;
import net.stuxcrystal.configuration.parser.node.Node;
import net.stuxcrystal.configuration.parser.utils.ReflectionUtil;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Parses a byte array.
 */
public class ByteArrayType implements ValueType<byte[]> {

    @Override
    public boolean isValidType(Object object, Field field, Type cls) throws ReflectiveOperationException {
        Class<?> rawCls = ReflectionUtil.toClass(cls);
        return rawCls.isArray() && (rawCls.getComponentType().equals(Byte.class) || rawCls.getComponentType().equals(Byte.TYPE));

    }

    @Override
    public byte[] parse(Object object, Field field, ConfigurationParser parser, Type cls, Node<?> value) throws ReflectiveOperationException, ValueException {
        return Base64Coder.decode(((Node<String>) value).getData());
    }

    @Override
    public Node<?> dump(Object object, Field field, ConfigurationParser parser, Type cls, Object data) throws ReflectiveOperationException, ValueException {
        return new DataNode(new String(Base64Coder.encode((byte[]) data)));
    }
}
