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

package net.stuxcrystal.configuration.node;

/**
 * A data node can only have this types: <pre>{@link String} or {@link Node}[]</pre>.
 *
 * @author StuxCrystal
 */
public class DataNode extends Node<String> {

    /**
     * The name of the node.
     */
    private String name;

    /**
     * The data of the node.
     */
    private String data;

    /**
     * The value of the node.
     */
    private String[] comments;

    /**
     * The parent node.
     */
    private Node<?> node;

    public DataNode(String data) {
        this.data = data;
    }

    public DataNode() {
        this(null);
    }

    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public String getData() {
        return this.data;
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
    public Node<?> getParent() {
        return this.node;
    }

    @Override
    public void setData(String data) {
        this.data = data;

    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setParent(Node<?> parent) {
        this.node = parent;
    }

    @Override
    public String[] getComments() {
        return this.comments;
    }

    @Override
    public void setComments(String[] comments) {
        this.comments = comments;
    }

}
