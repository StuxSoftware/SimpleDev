package net.stuxcrystal.configuration.node;

/**
 * Represents a node.
 */
public class ArrayNode extends MapNode {

    protected ArrayNode(Node<?> parent, Node<?>[] nodes, String[] value, String name) {
        super(parent, nodes, value, name);
    }

    public ArrayNode(Node<?>[] nodes) {
        super(nodes);
    }

    public ArrayNode() {
        super();
    }
}
