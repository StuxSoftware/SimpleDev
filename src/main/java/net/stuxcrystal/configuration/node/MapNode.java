package net.stuxcrystal.configuration.node;

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
