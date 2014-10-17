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

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Support for CommandHandler and Configuration
 */
public class NumberType implements ValueType<Object> {

    /**
     * List of all class types.
     */
    public static final Class<?>[] WRAPPER_TYPES = new Class[]{
            Boolean.class, Character.class, Integer.class, Long.class,
            Short.class, Byte.class, Double.class, Float.class,
    };

    /**
     * Checks if the type is a wrapper type.
     *
     * @param cls
     * @return
     */
    private static boolean isWrapper(Class<?> cls) {

        for (Class<?> clz : WRAPPER_TYPES) {
            if (clz.equals(cls)) return true;
        }

        return false;

    }

    /**
     * Wraps a primitive class.
     *
     * @param before The primitive type.
     * @return The wrapper type.
     */
    public static Class<?> wrap(Class<?> before) {
        if (!before.isPrimitive()) return before;

        for (Class<?> clz : WRAPPER_TYPES) {
            if (ReflectionUtil.getFieldQuiet(clz, null, "TYPE").equals(before))
                return clz;
        }

        return before;
    }

    /**
     * Parses a value.
     *
     * @param cls   The class of the result.
     * @param value The string containing the value.
     * @return the parsed number.
     * @throws net.stuxcrystal.configuration.exceptions.ValueException If the parse fails.
     */
    public static <T> T parseValue(Class<T> cls, String value) throws ValueException {
        if (!(cls.isPrimitive() || isWrapper(cls)))
            throw new ValueException("Invalid type.");

        Class<?> wrapper = wrap(cls);
        try {
            if (wrapper.equals(Boolean.class))
                return (T) Boolean.valueOf(Boolean.valueOf(value));
            else if (wrapper.equals(Character.class))
                return (T) Character.valueOf(value.charAt(0));
            else if (wrapper.equals(Integer.class))
                return (T) Integer.valueOf(value);
            else if (wrapper.equals(Long.class))
                return (T) Long.valueOf(value);
            else if (wrapper.equals(Short.class))
                return (T) Short.valueOf(value);
            else if (wrapper.equals(Byte.class))
                return (T) Byte.valueOf(value);
            else if (wrapper.equals(Double.class))
                return (T) Double.valueOf(value);
            else if (wrapper.equals(Float.class))
                return (T) Float.valueOf(value);
            else
                throw new ValueException("Failed to parse number-value.");
        } catch (NumberFormatException e) {
            throw new ValueException("Failed to parse string...", e);
        }
    }

    /**
     * Returns true on primitive-types and types that derives from numbers.
     */
    @Override
    public boolean isValidType(Object object, Field field, Type cls) throws ReflectiveOperationException {
        return ReflectionUtil.toClass(cls).isPrimitive() || isWrapper(ReflectionUtil.toClass(cls));
    }

    /**
     * Parses a number.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Object parse(Object object, Field field, ConfigurationParser parser, Type type, Node<?> value) throws ReflectiveOperationException, ValueException {
        return parseValue(ReflectionUtil.toClass(type), ((Node<String>) value).getData());
    }

    /**
     * Dumps a number
     *
     * @param object The object the parser is parsing.
     * @param field  The field the parser is parsing.
     * @param parser
     * @param type
     * @param data   The data to be parsed.
     * @return
     * @throws ReflectiveOperationException
     * @throws net.stuxcrystal.configuration.exceptions.ValueException
     */
    @Override
    public Node<?> dump(Object object, Field field, ConfigurationParser parser, Type type, Object data) throws ReflectiveOperationException, ValueException {
        return new DataNode(data.toString());
    }

}
