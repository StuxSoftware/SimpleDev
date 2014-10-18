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

package net.stuxcrystal.configuration.parser.generators.xml;

import net.stuxcrystal.configuration.parser.ConfigurationHandler;
import net.stuxcrystal.configuration.parser.NodeTreeGenerator;
import net.stuxcrystal.configuration.parser.node.Node;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Node tree generator for the XML File-Type.
 */
public class XmlGenerator implements NodeTreeGenerator {

    @Override
    public String getName() {
        return "XML";
    }

    @Override
    public boolean isValidFile(File file) throws IOException {
        return file.getName().endsWith(".xml");
    }

    @Override
    public Node<?> parse(InputStream stream, ConfigurationHandler parser) throws IOException {
        return XMLParser.parse(stream);
    }

    @Override
    public void dump(OutputStream stream, Node<?> node, ConfigurationHandler parser) throws IOException {
        CommentAwareDumper.dump(stream, node);
    }
}
