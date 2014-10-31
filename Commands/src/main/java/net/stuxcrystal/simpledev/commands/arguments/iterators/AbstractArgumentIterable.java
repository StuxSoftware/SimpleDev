package net.stuxcrystal.simpledev.commands.arguments.iterators;

import net.stuxcrystal.simpledev.commands.arguments.ArgumentList;

import java.util.AbstractList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Implements an abstract argument itrable.
 * @param <T> The type of the values.
 */
public abstract class AbstractArgumentIterable<T> extends AbstractList<T> {

    /**
     * The parent of the iterable.
     */
    protected final ArgumentIterable parent;

    /**
     * The type of the iterable.
     */
    protected final Class<T> type;

    /**
     * Creates a new abstract argument iterable.
     * @param parent The parent iterable.
     * @param type   The type of the iterable.
     */
    public AbstractArgumentIterable(ArgumentIterable parent, Class<T> type) {
        this.parent = parent;
        this.type = type;
    }

    /**
     * Returns the converted argument at the given index.<p />
     *
     * The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.<p />
     *
     * If the given argument was not found or couldn't be converted, null will be returned. If the
     * given class is the Class-instance for a primitive type, a NumberFormatException will be thrown
     * if the number couldn't be parsed.
     *
     * @param index The index of the argument.
     * @param cls   The type of the argument.
     * @return The argument passed to the command.
     * @throws NumberFormatException          If the parsing of the argument failed.
     * @throws IllegalArgumentException       The given type is not supported.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    protected abstract <E> E get(int index, Class<E> cls) throws NumberFormatException;

    /**
     * Returns the argument at the given index.
     * @param index The argument at the given index.
     * @return The argument at the given index.
     */
    public T get(int index) {
        return this.get(index, this.type);
    }

    /**
     * Returns the converted argument at the given index.<p />
     *
     * The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.<p />
     *
     * If the given argument was not found or couldn't be converted, null will be returned.
     *
     * @param index The index of the argument.
     * @param def   The default value.
     * @return The converted argument.
     */
    public T get(int index, T def) {
        try {
            return this.get(index);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return def;
        }
    }

    /**
     * Returns all arguments as strings.
     * @return All arguments as raw strings.
     */
    public List<String> copy() {
        return this.getParent().getArguments(String.class);
    }

    /**
     * Returns the parent iterable.
     * @return The parent iterable.
     */
    public ArgumentIterable getParent() {
        return this.parent;
    }

    /**
     * Returns the argument parser behind the iterable.
     * @return The argument parser behind the iterable.
     */
    public ArgumentList getArgumentParser() {
        return this.getParent().getArgumentParser();
    }

    /**
     * Implementation of contains all that only iterate once over c and the list itself.
     * @param c The collection that should be checked.
     * @return {@code true} if this list is a superset of the passed collection.
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        HashSet<?> hs = new HashSet<>(c);
        for (T item : this) {
            hs.remove(item);
            if (hs.size()==0)
                return true;
        }
        return false;
    }
}
