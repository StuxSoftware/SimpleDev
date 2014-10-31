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

import java.lang.ref.WeakReference;

/**
 * A data node can only have this types: <pre>{@link String} or {@link Node}[]</pre>.
 *
 * @author StuxCrystal
 */
public class DataNode extends Node<String> {

    /**
     * Creates a new data node.
     * @param data Contents
     */
    public DataNode(String data) {
        this.setData(data);
    }

    /**
     * Creates a new data node.
     */
    public DataNode() {
        this(null);
    }

    /**
     * Checks if the node has children.
     * @return {@code false}
     */
    @Override
    public boolean hasChildren() {
        return false;
    }

}
