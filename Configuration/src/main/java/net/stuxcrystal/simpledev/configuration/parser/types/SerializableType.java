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

import java.io.*;
import java.lang.reflect.Type;

/**
 * Allows to store serializable data.
 */
// TODO: Remove snakeyaml dependency.
public class SerializableType implements ValueType<Object> {
    @Override
    public boolean isValidType(Object object, Type cls) throws ReflectiveOperationException {
        return Serializable.class.isAssignableFrom(ReflectionUtil.toClass(cls));
    }

    @Override
    public Object parse(Object object, ConfigurationParser parser, Type cls, Node<?> value) throws ReflectiveOperationException, ValueException {
        byte[] content = Base64.decode(((Node<String>) value).getData());
        ByteArrayInputStream input = new ByteArrayInputStream(content);
        ObjectInputStream ois;
        try {
            ois = new ObjectInputStream(input);
        } catch (IOException e) {
            throw new ValueException("Failed initialize from a byte array?.", e);
        }

        Object result;
        try {
            result = ois.readObject();
        } catch (IOException e) {
            throw new ValueException("Failed to read object", e);
        } finally {
            try {
                ois.close();
            } catch (IOException ignored) {
            }
        }

        // Check if the result types are the same.
        if (!ReflectionUtil.toClass(cls).isInstance(result))
            throw new ValueException("Incompatible types: " + ReflectionUtil.toClass(cls) + " and " + result.getClass());

        return result;
    }


    @Override
    public Node<?> dump(Object object, ConfigurationParser parser, Type cls, Object data) throws ReflectiveOperationException, ValueException {
        ObjectOutputStream oos;
        ByteArrayOutputStream output;
        try {
            oos = new ObjectOutputStream((output = new ByteArrayOutputStream()));
        } catch (IOException e) {
            throw new ValueException("Failed to initialize ObjectOutputStream.", e);
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
        return new DataNode(new String(Base64.encode(output.toByteArray())));
    }
}
