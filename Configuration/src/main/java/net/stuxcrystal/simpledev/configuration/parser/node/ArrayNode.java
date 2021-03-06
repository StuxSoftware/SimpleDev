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

package net.stuxcrystal.simpledev.configuration.parser.node;

/**
 * Represents a node that contains unnamed data.
 */
public class ArrayNode extends MapNode {

    /**
     * Creates a new array node.
     * @param parent    The parent node.
     * @param nodes     The nodes that the array node contains.
     * @param comments  The comments
     * @param name      The name of the node.
     */
    protected ArrayNode(Node<?> parent, Node<?>[] nodes, String[] comments, String name) {
        super(parent, nodes, comments, name);
    }

    /**
     * Creates a new array node.
     * @param nodes The node that the array node contains.
     */
    public ArrayNode(Node<?>[] nodes) {
        super(nodes);
    }

    /**
     * Creates a new array node.
     */
    public ArrayNode() {
        super();
    }
}
