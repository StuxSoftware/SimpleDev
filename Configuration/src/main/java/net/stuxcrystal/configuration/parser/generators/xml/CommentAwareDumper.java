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

import net.stuxcrystal.configuration.parser.node.Node;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Dumps an Node-Tree into a stream.
 */
class CommentAwareDumper {

    /**
     * Internal Reference to the dumper.
     */
    private static final CommentAwareDumper DUMPER = new CommentAwareDumper();

    /**
     * Internal reference to the default XML declaration.
     */
    private static final String XML_DECLARATION = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>";

    private CommentAwareDumper() {
    }

    /**
     * Dumps a node tree into a XML-File.
     *
     * @param stream
     * @param node
     * @throws java.io.IOException
     */
    public static void dump(OutputStream stream, Node<?> node) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));
        writer.write(XML_DECLARATION);
        writer.newLine();

        // Set the node name if necessary.
        if (node.getName() == null || node.getName().isEmpty())
            node.setName("xml");

        DUMPER.dumpNode(writer, node, 0);
        writer.close();
    }

    /**
     * Dumps the content of a node.
     *
     * @param writer
     * @param node
     * @param indent
     * @throws java.io.IOException
     */
    private void dump(BufferedWriter writer, Node<?> node, int indent) throws IOException {
        if (node.hasChildren()) {
            writer.newLine();
            for (Node<?> child : ((Node<Node<?>[]>) node).getData()) {
                dumpComment(writer, child, indent);
                dumpNode(writer, child, indent);
            }
            writeIndent(writer, indent - 2);

        } else {
            dumpScalar(writer, node);
        }
    }

    /**
     * Dumps the content of a string node.
     *
     * @param writer
     * @param node
     * @throws java.io.IOException
     */
    private void dumpScalar(BufferedWriter writer, Node<?> node) throws IOException {
        writer.write(StringEscapeUtils.escapeXml(((Node<String>) node).getData()));
    }

    /**
     * Dumps the content of a single node.
     *
     * @param writer
     * @param node
     * @param indent
     * @throws java.io.IOException
     */
    private void dumpNode(BufferedWriter writer, Node<?> node, int indent) throws IOException {
        String name = (node.getName() != null && !node.getName().isEmpty()) ? node.getName() : "item";

        writeIndent(writer, indent);

        writer.write("<");
        writer.write(name);
        writer.write(">");

        dump(writer, node, indent + 2);

        writer.write("</");
        writer.write(name);
        writer.write(">");

        writer.newLine();
    }

    /**
     * Dumps a comment.
     *
     * @param writer
     * @param node
     * @param indent
     * @throws java.io.IOException
     */
    private void dumpComment(BufferedWriter writer, Node<?> node, int indent) throws IOException {
        String[] comment = node.getComments();

        for (String line : comment) {
            writeIndent(writer, indent);
            if (!(line == null || line.isEmpty())) {
                writer.write("<!-- ");
                writer.write(line);
                writer.write(" -->");
            }

            writer.newLine();
        }
    }

    /**
     * Writes an indent.
     *
     * @param writer
     * @param indent
     * @throws java.io.IOException
     */
    private void writeIndent(BufferedWriter writer, int indent) throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) sb.append(" ");
        writer.write(sb.toString());
    }

}
