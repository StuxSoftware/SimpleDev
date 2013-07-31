package net.stuxcrystal.configuration.generators.xml;

import net.stuxcrystal.configuration.node.Node;

/**
 * Node without data.
 */
public class NullNode extends Node<Void> {

    private String name;

    private Node<?> parent;


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
        return parent;
    }

    @Override
    public void setParent(Node<?> parent) {
        this.parent = parent;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("NullNode[name=").append(getName()).append("]");

        return sb.toString();
    }
}
