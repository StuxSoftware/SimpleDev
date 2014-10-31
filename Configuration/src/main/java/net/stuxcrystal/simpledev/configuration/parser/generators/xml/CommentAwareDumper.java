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

import net.stuxcrystal.simpledev.configuration.parser.node.Node;
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
     * Contains the doctype of the xml file.
     */
    private static final String XML_DOCTYPE = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>";

    /**
     * One does not simple create a new dumper.
     */
    private CommentAwareDumper() {
    }

    /**
     * Dumps a node tree into a XML-File.
     *
     * @param stream  The stream to write data to.
     * @param node    The node to dump.
     * @throws java.io.IOException If an I/O-Operation fils.
     */
    public static void dump(OutputStream stream, Node<?> node) throws IOException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream, "UTF-8"));
        writer.write(XML_DOCTYPE);
        writer.newLine();

        // Set the node name if necessary.
        if (node.getName() == null || node.getName().isEmpty())
            node.setName("xml");

        CommentAwareDumper.DUMPER.dumpNode(writer, node, 0);
        writer.close();
    }

    /**
     * Dumps the content of a node.
     *
     * @param writer   The writer to write the content into.
     * @param node     The node to dump.
     * @param indent   The indent we should add in each line.
     * @throws java.io.IOException If an I/O-Operation fails.
     */
    private void dump(BufferedWriter writer, Node<?> node, int indent) throws IOException {
        if (node.hasChildren()) {
            writer.newLine();
            for (Node<?> child : ((Node<Node<?>[]>) node).getData()) {
                this.dumpComment(writer, child, indent);
                this.dumpNode(writer, child, indent);
            }
            this.writeIndent(writer, indent - 2);

        } else {
            this.dumpScalar(writer, node);
        }
    }

    /**
     * Dumps the content of a string node.
     *
     * @param writer  The writer to write data.
     * @param node    The node in which the data should be copied.
     * @throws java.io.IOException If an I/O-Operation fails.
     */
    private void dumpScalar(BufferedWriter writer, Node<?> node) throws IOException {
        writer.write(StringEscapeUtils.escapeXml(((Node<String>) node).getData()));
    }

    /**
     * Dumps the content of a single node.
     *
     * @param writer The writer to write data.
     * @param node   The node.
     * @param indent The indent.
     * @throws java.io.IOException If an I/O-Operation fails.
     */
    private void dumpNode(BufferedWriter writer, Node<?> node, int indent) throws IOException {
        String name = (node.getName() != null && !node.getName().isEmpty()) ? node.getName() : "item";

        writeIndent(writer, indent);

        writer.write("<");
        writer.write(name);
        writer.write(">");

        this.dump(writer, node, indent + 2);

        writer.write("</");
        writer.write(name);
        writer.write(">");

        writer.newLine();
    }

    /**
     * Dumps a comment.
     *
     * @param writer  The writer to write the comment into.
     * @param node    The node that should be used.
     * @param indent  The indent of the nodes.
     * @throws java.io.IOException If an I/O-Operation fails.
     */
    private void dumpComment(BufferedWriter writer, Node<?> node, int indent) throws IOException {
        String[] comment = node.getComments();

        for (String line : comment) {
            this.writeIndent(writer, indent);
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
     * @param writer The writer to write the indent into.
     * @param indent The indent.
     * @throws java.io.IOException If an I/O-Operation fails.
     */
    private void writeIndent(BufferedWriter writer, int indent) throws IOException {
        // We will use a char array since we know how many characters we are going to write.
        char[] chars = new char[indent];
        for (int i = 0; i < indent; i++)
            chars[i] = ' ';
        writer.write(chars, 0, indent);
    }

}
