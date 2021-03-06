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

package net.stuxcrystal.simpledev.configuration.parser.generators.yaml;

import net.stuxcrystal.simpledev.configuration.parser.ConfigurationHandler;
import net.stuxcrystal.simpledev.configuration.parser.node.ArrayNode;
import net.stuxcrystal.simpledev.configuration.parser.node.Node;
import org.apache.commons.lang.StringEscapeUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

/**
 * Dumps the yaml files with comments.
 */
class CommentAwareDumper {

    /**
     * The type of the object.
     */
    private static enum Type {
        MAP, ARRAY, SCALAR
    }

    /**
     * True if something was written.
     */
    private boolean wroteSomething = false;

    /**
     * The configuration handler.
     */
    private final ConfigurationHandler parser;

    /**
     * Creates the new dumper.
     * @param parser The parser for comments.
     */
    public CommentAwareDumper(ConfigurationHandler parser) {
        this.parser = parser;
    }

    /**
     * Dumps the node to the file.
     * @param writer The writer of the file
     * @param parent The parent node to dump.
     * @throws IOException I an I/O-Operation fails.
     */
    public void dump(Writer writer, Node<?> parent) throws IOException {
        BufferedWriter buffer = new BufferedWriter(writer);
        writeComment(buffer, parent.getComments(), 0);
        writeNode(buffer, parent, 0);
        buffer.flush();
    }

    /**
     * Writes a writer.
     *
     * @param writer The writer to write in.
     * @param lines  The lines to write.
     * @param indent The indent to add.
     */
    void writeComment(BufferedWriter writer, String[] lines, int indent) {
        if (lines == null) return;

        for (String line : lines) {
            writeIndent(writer, indent);
            if (line == null || line.isEmpty()) {
                _newline(writer);
                continue;
            }

            _write(writer, "# ");
            _write(writer, line);
            _newline(writer);

        }
    }

    /**
     * Writes a node.
     *
     * @param writer The writer to write to.
     * @param node   The node to write.
     * @param indent The indent to add.
     */
    void writeNode(BufferedWriter writer, Node<?> node, int indent) {
        switch (this.checkType(node)) {
            case SCALAR:
                writeScalar(writer, node, indent);
                break;

            case ARRAY:
                writeSequence(writer, node, indent);
                break;

            case MAP:
                writeMapping(writer, node, indent);
                break;
        }
    }

    /**
     * Writes the mapping.
     *
     * @param writer The writer to write to.
     * @param node   The node to write.
     * @param indent The indent to add.
     */
    @SuppressWarnings("unchecked")
    void writeMapping(BufferedWriter writer, Node<?> node, int indent) {
        if (((Node<Node<?>[]>) node).getData().length == 0) {
            _write(writer, "{}");
            return;
        }

        for (Node<?> current : ((Node<Node<?>[]>) node).getData()) {
            _newline(writer);
            writeComment(writer, current.getComments(), indent);
            writeIndent(writer, indent);
            writeNodeKey(writer, current);
            writeNode(writer, current, indent + 2);

        }
    }

    /**
     * Writes a sequence.
     *
     * @param writer The writer to write to.
     * @param node   The node to write.
     */
    @SuppressWarnings("unchecked")
    void writeSequence(BufferedWriter writer, Node<?> node, int indent) {
        if (((Node<Node<?>[]>) node).getData().length == 0) {
            _write(writer, "[]");
            return;
        }

        for (Node<?> current : ((Node<Node<?>[]>) node).getData()) {
            _newline(writer);
            writeComment(writer, current.getComments(), indent);
            writeIndent(writer, indent - 2);
            _write(writer, "- ");
            writeNode(writer, current, indent + 2);

        }
    }

    /**
     * Writes a scalar node.
     *
     * @param writer The writer to write to.
     * @param node   The node to write.
     */
    @SuppressWarnings("unchecked")
    void writeScalar(BufferedWriter writer, Node<?> node, int indent) {
        if (((Node<String>) node).getData().isEmpty()) {
            _write(writer, "\"\"");
            return;
        }

        String text = ((Node<String>) node).getData();
        if (!text.contains("\n")) {
            _write(writer, StringEscapeUtils.escapeXml(((Node<String>) node).getData()));
        } else {
            this.parser.warn("YAML", "Multi-Line String... Parsing may be inaccurate.");

            _write(writer, ">");
            _newline(writer);
            for (String line : text.split("\n")) {
                writeIndent(writer, indent);
                _write(writer, line);
                _newline(writer);
            }


        }
    }

    /**
     * Writes the Node-Key.
     *
     * @param writer The writer to write a key to.
     * @param node   The node
     */
    void writeNodeKey(BufferedWriter writer, Node<?> node) {
        if (node.hasName() || node.getName().isEmpty()) {
            _write(writer, node.getName());
            _write(writer, ": ");
        }
    }

    /**
     * Writes the indentation.
     *
     * @param writer The writer to write a key to.
     * @param indent The indent to write.
     */
    private void writeIndent(BufferedWriter writer, int indent) {
        char[] chars = new char[indent];
        for (int i = 0; i < indent; i++)
            chars[i] = ' ';

        _write(writer, new String(chars));
    }

    /**
     * Writes a string to the writer.
     *
     * @param writer The writer to write a text to.
     * @param text   The text to write.
     */
    private void _write(BufferedWriter writer, String text) {
        this.wroteSomething = true;

        try {
            writer.write(text);
        } catch (IOException e) {
            this.parser.getLoggingInterface().exception(e);
        }
    }

    /**
     * Writes a newline to the writer.
     *
     * @param writer The writer to write to.
     */
    private void _newline(BufferedWriter writer) {
        if (!this.wroteSomething) return;

        try {
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks the type.
     *
     * @param node The node to check.
     * @return The type of the generator.
     */
    Type checkType(Node<?> node) {
        if (node instanceof ArrayNode)
            return Type.ARRAY;

        if (!node.hasChildren()) {
            return Type.SCALAR;
        } else {
            boolean isMap = true;
            for (Node<?> n : (Node<?>[]) node.getData()) {
                if (n == null)
                    throw new IllegalArgumentException("Got a null value as a node.");

                if (n.getName() == null || n.getName().isEmpty()) {
                    isMap = false;
                    break;
                }
            }

            if (isMap)
                return Type.MAP;
            else
                return Type.ARRAY;
        }
    }

}
