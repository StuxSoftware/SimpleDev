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

import org.apache.commons.lang.StringUtils;

import java.lang.ref.WeakReference;
import java.util.Arrays;

/**
 * Represents a Node in the configuration file.
 *
 * @author StuxCrystal
 */
public abstract class Node<T> {

    /**
     * Contains the comments.
     */
    private String[] comments;

    /**
     * Reference to the parent.
     */
    private WeakReference<Node<?>> parent;

    /**
     * Contains the contents of the node.
     */
    private T contents;

    /**
     * Contains the name of the node.
     */
    private String name;

    /**
     * Returns true if the node has childnodes.<p />
     * Retrieve the children using {@code (Node[]) Node.getData();}
     *
     * @return {@code true} if node has children.
     */
    public abstract boolean hasChildren();

    /**
     * Returns the data.
     *
     * @return The data.
     */
    public T getData() {
        return this.contents;
    }

    /**
     * Sets the data.
     *
     * @param data The new data.
     */
    public void setData(T data) {
        this.contents = data;
    }

    /**
     * Has the node a name.
     */
    public boolean hasName() {
        return StringUtils.isNotBlank(this.name);
    }

    /**
     * Returns the name of the node.
     */
    public String getName() {
        return this.hasName()?this.name:null;
    }

    /**
     * The name of the node.
     *
     * @param name The name of the node.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the comments of the node.
     *
     * @return The comments.
     */
    public String[] getComments() {
        return this.comments;
    }

    /**
     * Sets the comments.
     *
     * @param comments The comments.
     */
    public void setComments(String[] comments) {
        this.comments = comments;
    }

    /**
     * Returns the parent node.
     *
     * @return The parent node.
     */
    protected Node<?> getParent() {
        return this.parent.get();
    }

    /**
     * Sets the parent node.
     *
     * @param parent The new parent.
     */
    public void setParent(Node<?> parent) {
        this.parent = new WeakReference<Node<?>>(parent);
    }

    /**
     * Creates a string representation for the node.
     * @return The string representation.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.getClass().getSimpleName());
        sb.append("[");
        sb.append("name=").append(this.getName());
        sb.append(", parent=WeakReference<").append(this.getParent() == null ? "null" : ""+this.getParent().hashCode()).append(">");
        if (this.getData() instanceof String)
            sb.append(", value=").append(this.getData());
        else
            sb.append(", value=").append(Arrays.toString((Node<?>[]) this.getData()));
        sb.append("]");

        return sb.toString();
    }
}
