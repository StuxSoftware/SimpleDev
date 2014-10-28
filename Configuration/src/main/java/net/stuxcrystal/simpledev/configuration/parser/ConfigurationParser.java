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

package net.stuxcrystal.simpledev.configuration.parser;

import net.stuxcrystal.simpledev.configuration.parser.exceptions.ValueException;
import net.stuxcrystal.simpledev.configuration.parser.node.Node;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Parses a configuration.
 *
 * @author StuxCrystal
 */
public class ConfigurationParser {

    /**
     * The ConfigurationLoader.
     */
    private final ConfigurationHandler loader;

    /**
     * Creates a new configuration parser.
     * @param loader The loader to use.
     */
    ConfigurationParser(ConfigurationHandler loader) {
        this.loader = loader;
    }

    /**
     * Parses an object.
     *
     * @param object The object that is being parsed.
     * @param cls    The type of the object.
     * @param node   The node to parse.
     * @return The parsed object.
     */
    public Object parseObject(Object object, Type cls, Node<?> node) throws ReflectiveOperationException, ValueException {
        if (node == null)
            throw new IllegalStateException("The given node is null: " + cls.getTypeName());

        for (ValueType<?> type : this.loader.types) {
            if (type.isValidType(object, cls)) {
                return type.parse(object, this, cls, node);
            }
        }

        throw new ValueException("The parser does not know how to parse the object...");
    }

    /**
     * Dumps an object.
     *
     * @param object The object the parser is currently parsing.
     * @param cls    The class that is to be called.
     * @param o      The object that has to be dumped.
     * @return a node.
     * @throws ReflectiveOperationException If a reflective Operation fails.
     * @throws net.stuxcrystal.simpledev.configuration.parser.exceptions.ValueException
     *                                      Incorrect usage.
     */
    @SuppressWarnings("unchecked")
    public Node<?> dumpObject(Object object, Type cls, Object o) throws ReflectiveOperationException, ValueException {

        for (ValueType type : this.loader.types) {
            if (type.isValidType(object, cls)) {
                return type.dump(object, this, cls, o);
            }
        }
        throw new ValueException("The parser does not know how to parse the object...");
    }

    /**
     * Returns the underlying configuration loader instance.
     * @return The configuration loader using this object.
     */
    public ConfigurationHandler getConfigurationLoader() {
        return this.loader;
    }
}
