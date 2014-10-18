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

package net.stuxcrystal.configuration.parser.node;

/**
 * Represents a parent node.
 *
 * @author StuxCrystal
 */
public class MapNode extends Node<Node<?>[]> {


    /**
     * The nodes.
     */
    private Node<?>[] children;

    /**
     * The metadata.
     */
    private String[] value;

    /**
     * The name of the field
     */
    private String name = null;

    /**
     * The parent node.
     */
    private Node<?> parent;

    MapNode(Node<?> parent, Node<?>[] nodes, String[] value, String name) {
        this.parent = parent;
        this.children = nodes;
        this.value = value;
        this.name = name;
    }

    public MapNode(Node<?>[] nodes) {
        this.children = nodes;
    }

    public MapNode() {

    }

    @Override
    public boolean hasChildren() {
        return true;
    }

    @Override
    public Node<?>[] getData() {
        return children;
    }

    @Override
    public boolean hasName() {
        return this.name != null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String[] getComments() {
        return this.value;
    }

    @Override
    public Node<?> getParent() {
        return this.parent;
    }

    @Override
    public void setData(Node<?>[] data) {
        this.children = data;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setComments(String[] value) {
        this.value = value;
    }

    @Override
    public void setParent(Node<?> parent) {
        this.parent = parent;
    }

}
