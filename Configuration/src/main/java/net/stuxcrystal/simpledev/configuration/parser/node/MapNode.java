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
 * Represents a parent node.
 *
 * @author StuxCrystal
 */
public class MapNode extends Node<Node<?>[]> {

    /**
     * Creates a new map node.
     * @param parent     The parent node.
     * @param nodes      The children.
     * @param comments   The comments.
     * @param name       The name of the node.
     */
    protected MapNode(Node<?> parent, Node<?>[] nodes, String[] comments, String name) {
        this(nodes);
        this.setName(name);
        this.setParent(parent);
        this.setComments(comments);
    }

    /**
     * Creates a new map node.
     * @param nodes      The children.
     */
    public MapNode(Node<?>[] nodes) {
        this();
        this.setData(nodes);
    }

    /**
     * Creates a new map node.
     */
    public MapNode() {

    }

    /**
     * Checks if the node has children.
     * @return {@code true}
     */
    @Override
    public boolean hasChildren() {
        return true;
    }
}
