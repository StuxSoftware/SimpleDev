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

import net.stuxcrystal.configuration.LoggingInterface;
import net.stuxcrystal.configuration.parser.exceptions.ConfigurationException;
import net.stuxcrystal.configuration.parser.exceptions.FileException;
import net.stuxcrystal.configuration.parser.exceptions.ReflectionException;
import net.stuxcrystal.configuration.parser.exceptions.ValueException;
import net.stuxcrystal.configuration.parser.logging.ErrorStreamBinding;
import net.stuxcrystal.configuration.parser.node.Node;
import net.stuxcrystal.configuration.storage.contrib.filebased.FileBasedStorageBackend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A singleton to load the configurations.
 *
 * @author StuxCrystal
 */
public class ConfigurationHandler {

    /**
     * A list of all {@link NodeTreeGenerator}s.
     */
    private final List<NodeTreeGenerator> generators = new ArrayList<>();

    /**
     * A list of all known {@link ValueType}s.
     */
    final List<ValueType<?>> types = new ArrayList<>();

    /**
     * The logging interface.
     */
    private LoggingInterface logger = new ErrorStreamBinding();

    /**
     * Basic Configuration Loader.
     */
    public ConfigurationHandler() {
        this(new BaseConstructor());
    }

    /**
     * Constructs the configuration loader with a Constructor object that loads the types and generators needed.
     *
     * @param constructor
     */
    public ConfigurationHandler(Constructor constructor) {
        constructor.loadGenerators(this);
        constructor.loadValueTypes(this);
    }

    /**
     * Adds a tree-generator.
     *
     * @param generator The generator to be added.
     */
    public void addTreeGenerator(NodeTreeGenerator generator) {
        this.generators.add(generator);
    }

    /**
     * Removes a tree-generator.
     *
     * @param generator The generator to be removed.
     */
    public void removeTreeGenerator(NodeTreeGenerator generator) {
        this.generators.remove(generator);
    }

    /**
     * Adds a type.
     *
     * @param type The type to be added.
     */
    public void addType(ValueType<?> type) {
        this.types.add(type);
    }

    /**
     * Removes a type.
     *
     * @param type The type to be removed.
     */
    public void removeType(ValueType<?> type) {
        this.types.remove(type);
    }

    /**
     * Sets the LoggingInterface for the configuration subsystem.
     * @param logger The logger to use.
     */
    public void setLoggingInterface(LoggingInterface logger) {
        this.logger = logger;
    }

    /**
     * Parses a file.
     *
     * @param file The file to be parsed.
     * @param cls  The class to be instantiated.
     * @return An instance with the specified values.
     * @throws net.stuxcrystal.configuration.parser.exceptions.ConfigurationException
     *          If something fails.
     */
    @SuppressWarnings("unchecked")
    public <T> T parseFile(File file, Class<? extends T> cls) throws ConfigurationException {
        NodeTreeGenerator usedGenerator = this.getTreeGenerator(file);

        if (usedGenerator == null)
            throw new ValueException("File type not supported.");

        try {
            return parseStream(new FileInputStream(file), usedGenerator, cls);
        } catch (IOException e) {
            throw new FileException(e);
        }
    }

    /**
     * Returns the generator for the file.
     * @param file The file.
     * @return The node tree generator.
     */
    public NodeTreeGenerator getTreeGenerator(File file) throws ConfigurationException {
        NodeTreeGenerator usedGenerator = null;
        for (NodeTreeGenerator generator : this.generators) {
            try {
                if (generator.isValidFile(file)) {
                    usedGenerator = generator;
                    break;
                }
            } catch (IOException e) {
                throw new FileException(e);
            }
        }
        return usedGenerator;
    }

    /**
     * Parses the contents of a stream.
     *
     * @param is        The stream to parse.
     * @param generator The generator to be used.
     * @param cls       The class of the configuration object.
     * @param <T>       The type.
     * @return An configuration object.
     * @throws ConfigurationException
     * @throws java.io.IOException
     */
    public <T> T parseStream(InputStream is, NodeTreeGenerator generator, Class<? extends T> cls) throws ConfigurationException, IOException {
        Node<?> nodes = generator.parse(is, this);

        if (nodes == null)
            throw new ValueException("Failed to parse stream content.");

        return parseNodeTree(nodes, cls);
    }

    /**
     * Writes the configuration into a file.<p />
     *
     * Creates the file (and the parent-directories) if needed.
     *
     * @param file          The file to save into.
     * @param configuration The configuration.
     * @throws ConfigurationException
     */
    public void dumpFile(File file, Object configuration) throws ConfigurationException, IOException {
        FileBasedStorageBackend.createFile(file);
        Node<?> nodes = dumpConfiguration(configuration);

        NodeTreeGenerator usedGenerator = this.getTreeGenerator(file);

        if (usedGenerator == null)
            throw new ValueException("File type not supported.");

        try {
            dumpStream(new FileOutputStream(file), usedGenerator, configuration);
        } catch (IOException e) {
            throw new FileException("Failed to open stream.", e);
        }
    }

    /**
     * Dumps a configuration into a file.
     *
     * @param stream        The stream to dump to.
     * @param generator     The generator to be used.
     * @param configuration The configuration object.
     * @throws net.stuxcrystal.configuration.parser.exceptions.ValueException
     * @throws ReflectionException
     * @throws java.io.IOException
     */
    public void dumpStream(OutputStream stream, NodeTreeGenerator generator, Object configuration) throws ValueException, ReflectionException, IOException {
        Node<?> nodes = dumpConfiguration(configuration);
        generator.dump(stream, nodes, this);
    }

    /**
     * Converts a configuration between formats.<p />
     * <p/>
     * Please note that this method does not ensure that the output will be the same as using
     * {@code dumpStream(stream, outType, parseStream(in, inType, <Type>)); }
     *
     * @param in      The input
     * @param out     The output
     * @param inType  The type of the input
     * @param outType The type of the output.
     * @throws java.io.IOException
     */
    public void convert(InputStream in, OutputStream out, NodeTreeGenerator inType, NodeTreeGenerator outType) throws IOException {
        outType.dump(out, inType.parse(in, this), this);
    }

    /**
     * Converts two files using an internal configuration type.<p />
     * <p/>
     * Please note that this method is significantly slower than the lossy conversion method.
     *
     * @param in       Input Stream.
     * @param out      OutputStream
     * @param inType   Serialization Type of the InputStream.
     * @param outType  Serialization Type of the OutputStream.
     * @param confType The type of the configuration class.
     * @throws java.io.IOException            Thrown when an IO-Operation fails.
     * @throws ConfigurationException Thrown when an an conversion fails.
     */
    public void convert(InputStream in, OutputStream out, NodeTreeGenerator inType, NodeTreeGenerator outType, Class<?> confType) throws IOException, ConfigurationException {
        dumpStream(out, outType, parseStream(in, inType, confType));
    }

    /**
     * Warns the user for a possible exception.
     *
     * @param where   In which part of the library does the exception occur.
     * @param message The message of the warning.
     */
    public void warn(String where, String message) {
        logger.warn("[" + where + "] " + message);
    }

    /**
     * Dumps a configuration object.
     *
     * @param configuration The configuration to dump.
     * @return
     * @throws ReflectionException
     * @throws net.stuxcrystal.configuration.parser.exceptions.ValueException
     */
    private Node<?> dumpConfiguration(Object configuration) throws ReflectionException, ValueException {
        ConfigurationParser parser = new ConfigurationParser(this);
        Node<?> nodes;
        try {
            nodes = parser.dumpObject(null, null, configuration.getClass(), configuration);
        } catch (ReflectiveOperationException e) {
            throw new ReflectionException(e);
        }
        return nodes;
    }

    /**
     * Parses a node tree.
     *
     * @param nodes The nodes to parse.
     * @param cls   The class that parses the nodes.
     * @param <T>   The type of the configuration class.
     * @return The configuration.
     */
    private <T> T parseNodeTree(Node<?> nodes, Class<? extends T> cls) throws ValueException, ReflectionException {
        ConfigurationParser parser = new ConfigurationParser(this);
        Object o;
        try {
            o = parser.parseObject(null, null, cls, nodes);
        } catch (ReflectiveOperationException e) {
            throw new ReflectionException(e);
        }
        return (T) o;
    }

    /**
     * The logging interface.
     *
     * @return A logging-interface object.
     */
    public LoggingInterface getLoggingInterface() {
        return this.logger;
    }

}
