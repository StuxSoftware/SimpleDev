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

import java.lang.ref.WeakReference;

/**
 * Node without data.
 */
public class NullNode extends Node<Void> {

    private String name;

    private WeakReference<Node<?>> parent;


    @Override
    public boolean hasChildren() {
        return false;
    }

    @Override
    public Void getData() {
        return null;
    }

    @Override
    public void setData(Void data) {

    }

    @Override
    public boolean hasName() {
        return false;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String[] getComments() {
        return new String[0];  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void setComments(String[] comments) {

    }

    @Override
    public Node<?> getParent() {
        return this.parent.get();
    }

    @Override
    public void setParent(Node<?> parent) {
        this.parent = new WeakReference<Node<?>>(parent);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("NullNode[name=").append(getName()).append("]");

        return sb.toString();
    }
}
