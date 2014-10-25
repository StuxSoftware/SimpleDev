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

import net.stuxcrystal.simpledev.configuration.parser.types.*;
import org.apache.commons.lang.ClassUtils;

/**
 * Basic constructor.
 */
public class BaseConstructor implements Constructor {

    /**
     * Generators to add by a subclass.
     * @param loader The loader.
     */
    public void addedGenerators(ConfigurationHandler loader) {}

    /**
     * Types to be added by a subclass.
     * @param loader The loader.
     */
    public void addedTypes(ConfigurationHandler loader) {}

    private void registerTreeGeneratorClass(ConfigurationHandler loader, String classname) {
        try {
            Class<?> cls = ClassUtils.getClass(this.getClass().getClassLoader(), classname);
            loader.addTreeGenerator((NodeTreeGenerator) cls.newInstance());
        } catch (Throwable e) {
            // Do a catchall, that's easier.
            loader.getLoggingInterface().debug("Not installing: " + loader);
            loader.getLoggingInterface().debugException(e);
        }
    }

    /**
     * Loads all existing generators.
     *
     * @param loader The loader where to add the generators.
     */
    @Override
    public final void loadGenerators(ConfigurationHandler loader) {
        addedGenerators(loader);

        // Adds predefined generators if they are supported.
        this.registerTreeGeneratorClass(loader, "net.stuxcrystal.simpledev.configuration.parser.generators.xml.XmlGenerator");
        this.registerTreeGeneratorClass(loader, "net.stuxcrystal.simpledev.configuration.parser.generators.yaml.YamlGenerator");
    }

    /**
     * Load all available logging types.
     *
     * @param loader The loader where to add the types.
     */
    @Override
    public final void loadValueTypes(ConfigurationHandler loader) {
        addedTypes(loader);

        // Adds predefined types.
        loader.addType(new ByteArrayType());                          // byte[]   to String using Base64
        loader.addType(new ArrayType());                              // All other arrays.
        loader.addType(new ListType());                               // Lists.
        loader.addType(new MapType());                                // Maps
        loader.addType(new EnumType());                               // Enums
        loader.addType(new ConfigurationType());                      // Other configurations.
        loader.addType(new NumberType());                             // Plain scalars.
        loader.addType(new StringType());                             // Simple strings.
        loader.addType(new FileType());                               // Paths.
        loader.addType(new SerializableType());                       // All other serializable types.
    }
}
