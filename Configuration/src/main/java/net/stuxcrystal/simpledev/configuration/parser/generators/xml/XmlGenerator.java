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

package net.stuxcrystal.simpledev.configuration.parser.generators.xml;

import net.stuxcrystal.simpledev.configuration.parser.ConfigurationHandler;
import net.stuxcrystal.simpledev.configuration.parser.NodeTreeGenerator;
import net.stuxcrystal.simpledev.configuration.parser.node.Node;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Node tree generator for the XML File-Type.
 */
public class XmlGenerator implements NodeTreeGenerator {

    /**
     * Returns "XML"
     * @return "XML"
     */
    @Override
    public String getName() {
        return "XML";
    }

    /**
     * Only accept files that end with ".xml"
     * @param name The name of the file that should be parsed.
     * @return {@code true} if the name ends with ".xml"
     * @throws IOException if an I/O-Operation fails.
     */
    @Override
    public boolean isValidFileName(String name) throws IOException {
        return name.endsWith(".xml");
    }

    /**
     * Parse the xml file.
     * @param stream The stream to parse.
     * @param parser The configuration handler that parses the stream.
     * @return The node tree.
     * @throws IOException If an I/O-Operation fails.
     */
    @Override
    public Node<?> parse(InputStream stream, ConfigurationHandler parser) throws IOException {
        return XMLParser.parse(stream);
    }

    /**
     * Dump the node tree into a file.
     * @param stream The stream to write to.
     * @param node   The node to store.
     * @param parser The configuration handler that parses the stream.
     * @throws IOException If an I/O-Operation fails.
     */
    @Override
    public void dump(OutputStream stream, Node<?> node, ConfigurationHandler parser) throws IOException {
        CommentAwareDumper.dump(stream, node);
    }
}
