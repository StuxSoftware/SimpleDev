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

package net.stuxcrystal.configuration.generators.xml;

import net.stuxcrystal.configuration.node.DataNode;
import net.stuxcrystal.configuration.node.MapNode;
import net.stuxcrystal.configuration.node.Node;
import net.stuxcrystal.configuration.node.NullNode;
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

    public static class NodeContainer {

        private Node<?> node = new NullNode();

        private NodeContainer parent = null;

        public NodeContainer() {
        }

        private <T> Node<T> convert(Node<T> instance) {
            Node<?> pre = node;
            node = instance;

            node.setName(pre.getName());

            return instance;
        }

        private void setName(String name) {
            node.setName(name);
        }

        private Node<?> getRawNode() {
            return node;
        }

        private <T extends Node<?>> T getCastedRawNode() {
            return (T) node;
        }

        public void setParent(NodeContainer container) {
            this.parent = container;
        }

        public <T extends Node<?>> T getNode() {
            if (parent != null)
                node.setParent(parent.node);
            return (T) node;
        }
    }

    private LinkedList<NodeContainer> currentStack = new LinkedList<>();

    private Node<?> base = null;

    private static SAXParser getParser() throws ParserConfigurationException, SAXException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        return factory.newSAXParser();
    }

    private static InputSource toSource(InputStream stream) throws UnsupportedEncodingException {
        Reader reader = new InputStreamReader(stream, "UTF-8");
        InputSource source = new InputSource(reader);
        source.setEncoding("UTF-8");
        return source;
    }

    public static Node<?> parse(InputStream stream) throws IOException {
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


    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        NodeContainer parent = currentStack.peek();

        NodeContainer container = new NodeContainer();
        container.setName(qName);
        container.setParent(parent);
        currentStack.push(container);
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        String content = new String(ch, start, length);

        if (StringUtils.isBlank(content))
            return;

        NodeContainer container = currentStack.peek();

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

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        NodeContainer container = currentStack.pop();

        if (currentStack.isEmpty()) {
            base = container.getNode();
        } else {
            NodeContainer parent = currentStack.peek();
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
