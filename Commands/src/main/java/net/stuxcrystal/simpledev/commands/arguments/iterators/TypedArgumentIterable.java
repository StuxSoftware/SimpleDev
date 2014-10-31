package net.stuxcrystal.simpledev.commands.arguments.iterators;

/**
 * Implements a typed argument iterable.
 */
public class TypedArgumentIterable<T> extends ArgumentIterable<T> {

    /**
     * Creates a new abstract argument iterable.
     *
     * @param parent The parent iterable.
     * @param type   The type of the iterable.
     */
    public TypedArgumentIterable(ArgumentContainer parent, Class<T> type) {
        super(parent, type);
    }

    @Override
    public <E> E get(int index, Class<? extends E> cls) throws NumberFormatException {
        return this.getParent().get(index, cls);
    }

    @Override
    public int size() {
        return this.getParent().size();
    }
}
