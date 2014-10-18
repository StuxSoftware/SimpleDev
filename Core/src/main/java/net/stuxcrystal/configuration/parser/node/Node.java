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

import java.util.Arrays;

/**
 * Represents a Node in the configuration file.
 *
 * @author StuxCrystal
 */
public abstract class Node<T> {

    /**
     * Returns true if the node has childnodes.<p />
     * Retrieve the children using {@code (Node[]) Node.getData();}
     *
     * @return
     */
    public abstract boolean hasChildren();

    /**
     * Returns the data.
     *
     * @return
     */
    public abstract T getData();

    /**
     * Sets the data.
     *
     * @param data
     */
    public abstract void setData(T data);

    /**
     * Has the node a name.
     */
    public abstract boolean hasName();

    /**
     * Returns the name of the node.
     */
    public abstract String getName();

    /**
     * The name of the param.
     *
     * @param name
     */
    public abstract void setName(String name);

    /**
     * Returns the metadata of the node.
     *
     * @return
     */
    public abstract String[] getComments();

    /**
     * Sets the metadata.
     *
     * @param comments
     */
    public abstract void setComments(String[] comments);

    /**
     * The parent-node.
     *
     * @return
     */
    protected abstract Node<?> getParent();

    /**
     * Sets the parent node.
     *
     * @param parent
     */
    public abstract void setParent(Node<?> parent);

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Node[");
        sb.append("name=").append(this.getName());
        sb.append(", parent=").append(this.getParent() == null ? 0 : this.getParent().hashCode());
        if (this.getData() instanceof String)
            sb.append(", value=").append(this.getData());
        else
            sb.append(", value=").append(Arrays.toString((Node<?>[]) this.getData()));
        sb.append("]");

        return sb.toString();
    }
}
