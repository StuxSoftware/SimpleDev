package net.stuxcrystal.simpledev.commands.arguments.iterators;

/**
 * Implements a typed argument iterable.
 */
public class TypedArgumentIterable<T> extends AbstractArgumentIterable<T> {

    /**
     * Creates a new abstract argument iterable.
     *
     * @param parent The parent iterable.
     * @param type   The type of the iterable.
     */
    public TypedArgumentIterable(ArgumentIterable parent, Class<T> type) {
        super(parent, type);
    }

    @Override
    public <E> E get(int index, Class<E> cls) throws NumberFormatException {
        return this.getParent().get(index, cls);
    }

    @Override
    public int size() {
        return this.getParent().size();
    }
}
