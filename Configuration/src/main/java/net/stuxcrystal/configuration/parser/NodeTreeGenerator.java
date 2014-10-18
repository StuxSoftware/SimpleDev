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

import net.stuxcrystal.configuration.parser.node.Node;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Dumps
 *
 * @author StuxCrystal
 */
public interface NodeTreeGenerator {

    /**
     * Returns the name of the node-tree generator.
     *
     * @return The name of the generator.
     */
    public String getName();

    /**
     * Is the file a file that can be parsed using this node-tree-generator.<p />
     * <p/>
     * This method has to be executed quietly.
     *
     * @param file The file to be parsed.
     * @return true if the file can be parsed using this node-tree-generator.
     * @throws java.io.IOException if an IO-Operation failed.
     */
    public boolean isValidFile(File file) throws IOException;

    /**
     * Parses a stream.
     *
     * @param stream The stream to parse.
     * @return a node-tree.
     * @throws java.io.IOException if an IO-Operation fails.
     */
    public Node<?> parse(InputStream stream, ConfigurationHandler parser) throws IOException;

    /**
     * Dumps to a stream.
     *
     * @param stream The stream to write to.
     * @param node   The node to store.
     * @throws java.io.IOException If an IO-Operation fails.
     */
    public void dump(OutputStream stream, Node<?> node, ConfigurationHandler parser) throws IOException;

}
