package net.stuxcrystal.simpledev.commands.arguments.iterators;

import net.stuxcrystal.simpledev.commands.arguments.ArgumentList;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Implements an iterable for the argument parser.
 */
public abstract class ArgumentIterable implements Iterable<String> {

    /**
     * The iterator for the arguments.
     */
    public class ArgumentIterator<T> implements Iterator<T> {

        /**
         * The current index.
         */
        private int index = 0;

        /**
         * The object cache.
         */
        private T objCache = null;

        /**
         * The type of the object.
         */
        private final Class<T> type;

        /**
         * The lock for multithreading.
         */
        private final Object lock = new Object();

        /**
         * Creates a new argument iterator.
         * @param type The new argument iterator.
         */
        public ArgumentIterator(Class<T> type) {
            this.type = type;
        }

        /**
         * Checks if there is another iterable.
         * @return {@code true} if we were able to get the next argument.
         */
        @Override
        public boolean hasNext() {
            synchronized (this.lock) {
                if (this.objCache != null)
                    return true;

                try {
                    this.objCache = this._next();
                } catch (ArrayIndexOutOfBoundsException e) {
                    return false;
                }

                return true;
            }
        }

        /**
         * Returns the next object.
         * @return The next object.
         */
        @Override
        public T next() {
            synchronized (this.lock) {
                // Make sure we have an object cached.
                if (!this.hasNext())
                    throw new IllegalStateException("StopIteration");

                // Return and clear the cached object.
                T cache = this.objCache;
                this.objCache = null;
                return cache;
            }
        }

        /**
         * Tries to get the next object.
         * @return The next object.
         */
        @SuppressWarnings("unchecked")
        private T _next() {
            T result = (T)ArgumentIterable.this.getArgument(this.index, this.type);
            this.index++;
            return result;
        }
    }

    /**
     * The iterable this iterator is using.
     */
    private final ArgumentIterable iterable;

    /**
     * The argument iterable we will be using.
     * @param iterable The iterable to use.
     */
    protected ArgumentIterable(ArgumentIterable iterable) {
        this.iterable = iterable;
    }

    /**
     * Returns the argument parser behind the iterable.
     * @return The argument parser behind the iterable.
     */
    public ArgumentList getArgumentParser() {
        return this.iterable.getArgumentParser();
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
    public abstract <T> T getArgument(int index, Class<T> cls) throws NumberFormatException;

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
    public <T> T getArgument(int index, Class<T> cls, T def) {
        try {
            return this.getArgument(index, cls);
        } catch (NumberFormatException | ArrayIndexOutOfBoundsException nfe) {
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
        for (T arg : this.asClass(type))
            result.add(arg);
        return result;
    }

    /**
     * Returns all arguments as strings.
     * @return All arguments as raw strings.
     */
    public List<String> getArguments() {
        return this.getArguments(String.class);
    }

    /**
     * Returns an iterator to iterate over.
     * @return The iterator to iterate over.
     */
    public Iterator<String> iterator() {
        return new ArgumentIterator<>(String.class);
    }

    /**
     * Returns an iterable that returns the object in the given type.
     * @param cls The type of the values.
     * @param <T> The type.
     * @return An iterable that returns the iterable that returns the arguments at the given value.
     */
    @SuppressWarnings("unchecked")
    public <T> Iterable<T> asClass(final Class<T> cls) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return new ArgumentIterator<>(cls);
            }
        };
    }

    /**
     * Slices the arguments of the argument parser.
     * @param start The first argument that is included in the slice.
     * @param stop  The first argument that should <b>not</b> be included in the slice.
     * @param step  The step or stride that should be between each object.
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
        return new SliceIterable(this, start, stop, null);
    }

    /**
     * Returns the parent iterable.
     * @return The parent iterable.
     */
    public ArgumentIterable getParent() {
        return this.iterable;
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
        return this.getArgument(index, int.class);
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
        return this.getArgument(index, int.class, def);
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
        return this.getArgument(index, float.class);
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
        return this.getArgument(index, float.class, def);
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
        return this.getArgument(index, double.class);
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
        return this.getArgument(index, double.class, def);
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
        return this.getArgument(index, boolean.class);
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
        return this.getArgument(index, boolean.class, def);
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
        return this.getArgument(index, char.class);
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
        return this.getArgument(index, char.class, def);
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
        return this.getArgument(index, long.class);
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
        return this.getArgument(index, long.class, def);
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
        return this.getArgument(index, short.class);
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
        return this.getArgument(index, short.class, def);
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
        return this.getArgument(index, byte.class);
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
        return this.getArgument(index, byte.class, def);
    }
}
