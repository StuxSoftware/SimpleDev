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
import net.stuxcrystal.configuration.node.DataNode;
import net.stuxcrystal.configuration.node.Node;
import net.stuxcrystal.configuration.utils.ReflectionUtil;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Allows to store serializable data.
 */
public class SerializableType implements ValueType<Object> {
    @Override
    public boolean isValidType(Object object, Field field, Type cls) throws ReflectiveOperationException {
        return Serializable.class.isAssignableFrom(ReflectionUtil.toClass(cls));
    }

    @Override
    public Object parse(Object object, Field field, ConfigurationParser parser, Type cls, Node<?> value) throws ReflectiveOperationException, ValueException {
        byte[] content = Base64Coder.decode(((Node<String>) value).getData());
        ByteArrayInputStream input = new ByteArrayInputStream(content);
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(input);
        } catch (IOException e) {
            throw new ValueException("Failed initialize from a byte array?.", e);
        }

        try {
            return ois.readObject();
        } catch (IOException e) {
            throw new ValueException("Failed to read object", e);
        } finally {
            try {
                ois.close();
            } catch (IOException ignored) {
            }
        }
    }


    @Override
    public Node<?> dump(Object object, Field field, ConfigurationParser parser, Type cls, Object data) throws ReflectiveOperationException, ValueException {
        ObjectOutputStream oos;
        ByteArrayOutputStream output;
        try {
            oos = new ObjectOutputStream((output = new ByteArrayOutputStream()));
        } catch (IOException e) {
            throw new ValueException("Failed to initialie ObjectOutputStream.", e);
        }

        try {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new ValueException("Failed to serialize object.", e);
        }

        try {
            oos.close();
        } catch (IOException ignored) {
        }
        return new DataNode(new String(Base64Coder.encode(output.toByteArray())));
    }
}
