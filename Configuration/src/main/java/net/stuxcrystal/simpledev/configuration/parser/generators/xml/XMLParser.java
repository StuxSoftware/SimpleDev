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

import net.stuxcrystal.simpledev.configuration.parser.node.DataNode;
import net.stuxcrystal.simpledev.configuration.parser.node.MapNode;
import net.stuxcrystal.simpledev.configuration.parser.node.Node;
import net.stuxcrystal.simpledev.configuration.parser.node.NullNode;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.util.LinkedList;

/**
 * Parses an XML-File.
 */
class XMLParser extends DefaultHandler {

    /**
     * Contains the node that we are currently parsing.
     */
    public static class NodeContainer {

        /**
         * The current node.
         */
        private Node<?> node = new NullNode();

        /**
         * The parent node.
         */
        private NodeContainer parent = null;

        /**
         * Creates a new node container.
         */
        public NodeContainer() {
        }

        /**
         * Converts the current node in this node container to a node with the given type.
         * @param instance The new node
         * @param <T>      The type of the node.
         * @return The node passed to the function..
         */
        private <T> Node<T> convert(Node<T> instance) {
            Node<?> pre = this.node;
            this.node = instance;

            this.node.setName(pre.getName());

            return instance;
        }

        /**
         * Sets the name of the node.
         * @param name The name of the node.
         */
        private void setName(String name) {
            node.setName(name);
        }

        /**
         * Returns the raw node.
         * @return The raw node.
         */
        private Node<?> getRawNode() {
            return node;
        }

        /**
         * Casts the node object to the given type. (Yeah... I was practically a n00b in java as I wrote this)
         * @param <T> The type of the node.
         * @return The casted node.
         */
        @SuppressWarnings("unchecked")
        private <T extends Node<?>> T getCastedRawNode() {
            return (T) node;
        }

        /**
         * Sets the parent of the node.
         * @param container The parent.
         */
        public void setParent(NodeContainer container) {
            this.parent = container;
        }

        /**
         * Retrieve the actual node.
         * @param <T> The type of the node.
         * @return The node.
         */
        @SuppressWarnings("unchecked")
        public <T extends Node<?>> T getNode() {
            if (parent != null)
                node.setParent(parent.node);
            return (T) node;
        }
    }

    /**
     * The current stack of node containers.
     */
    private LinkedList<NodeContainer> currentStack = new LinkedList<>();

    /**
     * The current node.
     */
    private Node<?> base = null;

    /**
     * Creates a new SAX-Parser.
     * @return The new sax parser.
     * @throws ParserConfigurationException If we failed to create anew SAX-Parser.
     * @throws SAXException                 If we failed to create anew SAX-Parser.
     */
    private static SAXParser getParser() throws ParserConfigurationException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        return factory.newSAXParser();
    }

    /**
     * Converts a InputStream to a input source.
     * @param stream The stream that contains the file.
     * @return The InputSource for XML.
     * @throws UnsupportedEncodingException If there is a computer without UTF-8 let me know.
     */
    private static InputSource toSource(InputStream stream) throws UnsupportedEncodingException {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        InputSource source = new InputSource(reader);
        source.setEncoding("UTF-8");
        return source;
    }

    /**
     * Checks if a stream is actually empty.
     * @param pis The input stream.
     * @return {@code true} if so.
     */
    private static boolean isEmpty(PushbackInputStream pis) throws IOException {
        int b = pis.read();
        boolean empty = (b==-1);
        pis.unread(b);
        return empty;
    }

    /**
     * Parse the XML-File.
     * @param stream The stream to use.
     * @return The Node-Tree.
     * @throws IOException if an I/O-Operation fails.
     */
    public static Node<?> parse(InputStream stream) throws IOException {
        // Make sure empty files are supported...
        stream = new PushbackInputStream(stream, 1);
        if (XMLParser.isEmpty((PushbackInputStream)stream)) {
            // The node has no content.
            return new MapNode(new Node[0]);
        }

        SAXParser parser;
        try {
            parser = getParser();
        } catch (ParserConfigurationException | SAXException e) {
            throw new IOException(e.getLocalizedMessage(), e);
        }

        XMLParser raw_parser = new XMLParser();
        try {
            parser.parse(toSource(stream), raw_parser);
        } catch (SAXException e) {
            throw new IOException(e.getLocalizedMessage(), e);
        }

        return raw_parser.base;
    }

    /**
     * Called when a new node starts.
     * @param uri            PAIN IN THE ASS
     * @param localName      PAIN IN THE ASS
     * @param qName          PAIN IN THE ASS
     * @param attributes     The attributes of the element
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        NodeContainer parent = this.currentStack.peek();

        NodeContainer container = new NodeContainer();
        container.setName(qName);
        container.setParent(parent);
        this.currentStack.push(container);
    }

    /**
     * Parse the characters of the node.
     * @param ch         The character array. (Can't we deal with strings?)
     * @param start      The start
     * @param length     The length
     * @throws SAXException Cannot mix node types.
     */
    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        String content = new String(ch, start, length);

        if (StringUtils.isBlank(content))
            return;

        NodeContainer container = this.currentStack.peek();

        if (!(container.getRawNode() instanceof NullNode) && (!(container.getRawNode() instanceof DataNode))) {
            throw new SAXException("Node Value already set: " + container.getRawNode().toString());
        }

        if (container.getRawNode() instanceof NullNode) {
            DataNode node = (DataNode) container.convert(new DataNode());
            node.setData("");
        }

        DataNode node = container.getCastedRawNode();
        node.setData(node.getData() + content);
    }

    /**
     * Actually update the node that is below our current stack.
     * @param uri            PAIN IN THE ASS
     * @param localName      PAIN IN THE ASS
     * @param qName          PAIN IN THE ASS
     * @throws SAXException Cannot mix node types.
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        NodeContainer container = this.currentStack.pop();

        if (this.currentStack.isEmpty()) {
            this.base = container.getNode();
        } else {
            NodeContainer parent = this.currentStack.peek();
            if (!(parent.getRawNode() instanceof NullNode) && !(parent.getRawNode() instanceof MapNode))
                throw new SAXException("The configuration should not mix multiple element types:" + parent.getRawNode().toString());

            if (parent.getRawNode() instanceof NullNode) {
                MapNode base = (MapNode) parent.convert(new MapNode());
                base.setData(new Node<?>[0]);
            }

            MapNode node = parent.getCastedRawNode();
            node.setData((Node<?>[]) ArrayUtils.add(node.getData(), container.getNode()));
        }
    }

}
