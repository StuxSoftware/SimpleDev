package net.stuxcrystal.simpledev.commands.arguments.iterators;

import java.util.*;

/**
 * Implements an iterable for the argument parser.
 */
public abstract class ArgumentIterable extends AbstractArgumentIterable<String> {

    /**
     * The argument iterable we will be using.
     * @param iterable The iterable to use.
     */
    protected ArgumentIterable(ArgumentIterable iterable) {
        super(iterable, String.class);
    }


    /**
     * Returns the size of arguments that this iterable has.
     * @return The iterable.
     */
    public abstract int size();

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
    public abstract <E> E get(int index, Class<E> cls) throws NumberFormatException;

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
     * @param cls   The class of the argument.
     * @param def   The default value.
     * @param <T>   The type of the argument
     * @return The converted argument.
     */
    public <T> T get(int index, Class<T> cls, T def) {
        try {
            return this.get(index, cls);
        } catch (NumberFormatException | IndexOutOfBoundsException nfe) {
            return def;
        }
    }

    /**
     * Returns all arguments in this iterator.
     * @param type The type of the values in the list.
     * @param <T>  The type of the argument.
     * @return The argument.
     */
    public <T> List<T> getArguments(Class<T> type) {
        List<T> result = new ArrayList<>();
        for (T arg : this.as(type))
            result.add(arg);
        return result;
    }


    /**
     * Returns an iterable that returns the object in the given type.
     * @param cls The type of the values.
     * @param <T> The type.
     * @return An iterable that returns the iterable that returns the arguments at the given value.
     */
    @SuppressWarnings("unchecked")
    public <T> Iterable<T> as(Class<T> cls) {
        return new TypedArgumentIterable<>(this, cls);
    }

    /**
     * Slices the arguments of the argument parser.
     * @param start The first argument that is included in the slice.
     * @param stop  The first argument that should <b>not</b> be included in the slice.
     * @param step  The step or stride that should be between each object.
     *              (If this value is negative, the list will be traversed in reverse order)
     * @return A slice of the arguments.
     */
    public ArgumentIterable slice(Integer start, Integer stop, Integer step) {
        return new SliceIterable(this, start, stop, step);
    }

    /**
     * Slices the arguments of the argument parser.
     * @param start The first argument that is included in the slice.
     * @param stop  The first argument that should <b>not</b> be included in the slice.
     * @return A slice of the arguments.
     */
    public ArgumentIterable slice(Integer start, Integer stop) {
        return this.slice(start, stop, null);
    }

    /**
     * Since this list is unmodifiable, we just use our own slice-implementation that provides
     * these features for us.
     *
     * @param fromIndex low endpoint (inclusive) of the subList
     * @param toIndex   high endpoint (exclusive) of the subList
     * @return The sublist for the abstract list.
     */
    @Override
    public ArgumentIterable subList(int fromIndex, int toIndex) {
        return this.slice(fromIndex, toIndex);
    }

    /**
     * Returns an ArgumentIterable that contains all arguments from the given argument (inclusive).
     * @param start The first argument that is included in the iterable.
     * @return An iterable with arguments.
     */
    public ArgumentIterable from(int start) {
        return this.slice(start, null);
    }

    /**
     * Returns an ArgumentIterable that contains all arguments to the given argument (exclusive).
     * @param stop The first argument that is to be excluded in the iterable.
     * @return An iterable with arguments.
     */
    public ArgumentIterable to(int stop) {
        return this.slice(null, stop);
    }

    /**
     * Returns an ArgumentIterable that conains every n-th(step) argument.
     * @param step The n in n-th.
     * @return A new iterable.
     */
    public ArgumentIterable step(int step) {
        return this.slice(null, null, step);
    }

    /**
     * Returns the int at the given index.<p />
     *
     * The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.<p />
     *
     * If the given class is the Class-instance for a primitive type, a NumberFormatException will
     * be thrown if the number couldn't be parsed.<p />
     *
     * @param index The index of the argument.
     * @return The argument passed to the command.
     * @throws NumberFormatException          If the parsing of the argument failed.
     * @throws IllegalArgumentException       The given type is not supported.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public int getInt(int index) {
        return this.get(index, int.class);
    }

    /**
     * Returns the int at the given index.<p />
     *
     * The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.<p />
     *
     * If the argument couldn't be parsed, the default value will be returned.
     *
     * @param index The index of the argument.
     * @param def   The default value.
     * @return The argument passed to the command.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public int getInt(int index, int def) {
        return this.get(index, int.class, def);
    }


    /**
     * Returns the float at the given index.<p />
     *
     * The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.<p />
     *
     * If the given class is the Class-instance for a primitive type, a NumberFormatException will
     * be thrown if the number couldn't be parsed.<p />
     *
     * @param index The index of the argument.
     * @return The argument passed to the command.
     * @throws NumberFormatException          If the parsing of the argument failed.
     * @throws IllegalArgumentException       The given type is not supported.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public float getFloat(int index) {
        return this.get(index, float.class);
    }

    /**
     * Returns the float at the given index.<p />
     *
     * The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.<p />
     *
     * If the argument couldn't be parsed, the default value will be returned.
     *
     * @param index The index of the argument.
     * @param def   The default value.
     * @return The argument passed to the command.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public float getFloat(int index, float def) {
        return this.get(index, float.class, def);
    }


    /**
     * Returns the double at the given index.<p />
     *
     * The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.<p />
     *
     * If the given class is the Class-instance for a primitive type, a NumberFormatException will
     * be thrown if the number couldn't be parsed.<p />
     *
     * @param index The index of the argument.
     * @return The argument passed to the command.
     * @throws NumberFormatException          If the parsing of the argument failed.
     * @throws IllegalArgumentException       The given type is not supported.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public double getDouble(int index) {
        return this.get(index, double.class);
    }

    /**
     * Returns the double at the given index.<p />
     *
     * The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.<p />
     *
     * If the argument couldn't be parsed, the default value will be returned.
     *
     * @param index The index of the argument.
     * @param def   The default value.
     * @return The argument passed to the command.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public double getDouble(int index, double def) {
        return this.get(index, double.class, def);
    }


    /**
     * Returns the boolean at the given index.<p />
     *
     * The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.<p />
     *
     * If the given class is the Class-instance for a primitive type, a NumberFormatException will
     * be thrown if the number couldn't be parsed.<p />
     *
     * @param index The index of the argument.
     * @return The argument passed to the command.
     * @throws NumberFormatException          If the parsing of the argument failed.
     * @throws IllegalArgumentException       The given type is not supported.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public boolean getBoolean(int index) {
        return this.get(index, boolean.class);
    }

    /**
     * Returns the boolean at the given index.<p />
     *
     * The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.<p />
     *
     * If the argument couldn't be parsed, the default value will be returned.
     *
     * @param index The index of the argument.
     * @param def   The default value.
     * @return The argument passed to the command.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public boolean getBoolean(int index, boolean def) {
        return this.get(index, boolean.class, def);
    }


    /**
     * Returns the char at the given index.<p />
     *
     * The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.<p />
     *
     * If the given class is the Class-instance for a primitive type, a NumberFormatException will
     * be thrown if the number couldn't be parsed.<p />
     *
     * @param index The index of the argument.
     * @return The argument passed to the command.
     * @throws NumberFormatException          If the parsing of the argument failed.
     * @throws IllegalArgumentException       The given type is not supported.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public char getChar(int index) {
        return this.get(index, char.class);
    }

    /**
     * Returns the char at the given index.<p />
     *
     * The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.<p />
     *
     * If the argument couldn't be parsed, the default value will be returned.
     *
     * @param index The index of the argument.
     * @param def   The default value.
     * @return The argument passed to the command.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public char getChar(int index, char def) {
        return this.get(index, char.class, def);
    }


    /**
     * Returns the long at the given index.<p />
     *
     * The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.<p />
     *
     * If the given class is the Class-instance for a primitive type, a NumberFormatException will
     * be thrown if the number couldn't be parsed.<p />
     *
     * @param index The index of the argument.
     * @return The argument passed to the command.
     * @throws NumberFormatException          If the parsing of the argument failed.
     * @throws IllegalArgumentException       The given type is not supported.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public long getLong(int index) {
        return this.get(index, long.class);
    }

    /**
     * Returns the long at the given index.<p />
     *
     * The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.<p />
     *
     * If the argument couldn't be parsed, the default value will be returned.
     *
     * @param index The index of the argument.
     * @param def   The default value.
     * @return The argument passed to the command.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public long getLong(int index, long def) {
        return this.get(index, long.class, def);
    }


    /**
     * Returns the short at the given index.<p />
     *
     * The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.<p />
     *
     * If the given class is the Class-instance for a primitive type, a NumberFormatException will
     * be thrown if the number couldn't be parsed.<p />
     *
     * @param index The index of the argument.
     * @return The argument passed to the command.
     * @throws NumberFormatException          If the parsing of the argument failed.
     * @throws IllegalArgumentException       The given type is not supported.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public short getShort(int index) {
        return this.get(index, short.class);
    }

    /**
     * Returns the short at the given index.<p />
     *
     * The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.<p />
     *
     * If the argument couldn't be parsed, the default value will be returned.
     *
     * @param index The index of the argument.
     * @param def   The default value.
     * @return The argument passed to the command.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public short getShort(int index, short def) {
        return this.get(index, short.class, def);
    }


    /**
     * Returns the byte at the given index.<p />
     *
     * The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.<p />
     *
     * If the given class is the Class-instance for a primitive type, a NumberFormatException will
     * be thrown if the number couldn't be parsed.<p />
     *
     * @param index The index of the argument.
     * @return The argument passed to the command.
     * @throws NumberFormatException          If the parsing of the argument failed.
     * @throws IllegalArgumentException       The given type is not supported.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public byte getByte(int index) {
        return this.get(index, byte.class);
    }

    /**
     * Returns the byte at the given index.<p />
     *
     * The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.<p />
     *
     * If the argument couldn't be parsed, the default value will be returned.
     *
     * @param index The index of the argument.
     * @param def   The default value.
     * @return The argument passed to the command.
     * @throws ArrayIndexOutOfBoundsException If the index is invalid.
     */
    public byte getByte(int index, byte def) {
        return this.get(index, byte.class, def);
    }

    /**
     * Returns the real index as specified in get.
     * @param index       The index passed in get.
     * @param exclusive   Is this argument exclusive or inclusive.
     * @return The real index or -1 if the index is invalid.
     */
    protected int getRealIndex(int index, boolean exclusive) {
        int cnt = this.size();
        if (index > cnt) {
            // Index greater than the size of arguments.
            return -1;
        } else if (!exclusive && index == cnt) {
            // Disallow passing the actual length of the argument.
            return -1;
        } else if (index < 0) {
            // Support python-like indices.
            index += cnt;

            // If the index is still invalid, throw an exception.
            if (index < 0) {
                return -1;
            }
        }

        return index;
    }

    /**
     * Returns the real index as specified in get.
     * @param index The index passed in get.
     * @return The real index or -1 if the index is invalid.
     */
    protected int getRealIndex(int index) {
        return this.getRealIndex(index, false);
    }
}
