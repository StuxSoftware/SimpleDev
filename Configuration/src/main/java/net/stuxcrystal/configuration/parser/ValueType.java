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

package net.stuxcrystal.configuration.parser;

import net.stuxcrystal.configuration.parser.exceptions.ValueException;
import net.stuxcrystal.configuration.parser.node.Node;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Represents a value type.
 *
 * @param <T> The type of the object.
 * @author StuxCrystal
 */
public interface ValueType<T> {

    /**
     * Checks if the given field can be parsed by this parser/dumper.<p />
     * Should also support arrays.
     *
     * @param object The current object the parser is working on.
     * @param field  the field the parser is working on.
     * @param cls    the class the parser is parsing.
     * @return true if the type can be parsed by this object.
     * @throws ReflectiveOperationException if a reflection-operation failed.
     */
    public boolean isValidType(Object object, Field field, Type cls) throws ReflectiveOperationException;

    /**
     * Parses a value.<p />
     * Only use the field-argument to check data because arrays are using the same value.
     *
     * @param object The current object the parser is parsing.
     * @param field  the field of the object to set.
     * @param parser The parser that parses object. (Used if you want to parse Array or Maps).
     * @param cls    Type type of the current value.
     * @param value  The current node to be parsed.
     * @return The value of the object.
     * @throws ReflectiveOperationException if a reflection-Operation failed.
     * @throws net.stuxcrystal.configuration.parser.exceptions.ValueException
     */
    public T parse(Object object, Field field, ConfigurationParser parser, Type cls, Node<?> value) throws ReflectiveOperationException, ValueException;

    /**
     * Dumps a field into a node.<p />
     * Don't use the object stored in the field because arrays are passing the current value they're parsing
     * into this method.
     *
     * @param object The object the parser is parsing.
     * @param field  The field the parser is parsing.
     * @param parser The parser that dumps the object.
     * @param cls    The type of the object.
     * @param data   The data to be parsed.
     * @return A node with the given data.
     * @throws ReflectiveOperationException if a reflection-operation failed.
     */
    public Node<?> dump(Object object, Field field, ConfigurationParser parser, Type cls, Object data) throws ReflectiveOperationException, ValueException;

}
