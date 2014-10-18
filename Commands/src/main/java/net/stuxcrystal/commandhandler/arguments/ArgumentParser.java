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
package net.stuxcrystal.commandhandler.arguments;

import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.CommandHandler;
import net.stuxcrystal.commandhandler.arguments.types.PrimitiveType;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

/**
 * Argument Parser for BukkitInstallPlugin
 *
 * @author StuxCrystal
 */
public class ArgumentParser {

    /**
     * Raw arguments
     */
    private String[] arguments = new String[0];

    /**
     * Raw flags.
     */
    private String flags = "";

    /**
     * Reference to the Command-Handler.
     */
    private final CommandHandler handler;

    /**
     * The executor that executed the command.
     */
    private final CommandExecutor executor;

    /**
     * Initializes the argument parser.
     *
     * @param args
     */
    public ArgumentParser(CommandExecutor executor, CommandHandler handler, String[] args) {
        this.executor = executor;
        this.handler = handler;
        parseArgs(args);
    }

    /**
     * Parses the args.
     *
     * @param args The array of arguments to be parsed.
     */
    private void parseArgs(String[] args) {
        String rawArgs = StringUtils.join(args, " ");

        String[] parsed = this.handler.getArgumentHandler().getArgumentSplitter().split(rawArgs);

        if (parsed.length >= 1) {
            flags = parsed[0];
        }

        if (parsed.length >= 2) {
            arguments = (String[]) ArrayUtils.subarray(parsed, 1, parsed.length);
        }
    }

    /**
     * Returns true if the flag is given.
     *
     * @param c The flag.
     * @return true if the flag is present.
     */
    public boolean hasFlag(char c) {
        return flags.contains(new String(new char[]{c}));
    }

    /**
     * Returns a list of flags.
     *
     * @return A string containing all flags.
     */
    public String getFlags() {
        return flags;
    }

    /**
     * Returns all arguments beginning at the given index.
     *
     * @param from the index of the first element
     * @return Al list of strings
     */
    public String[] getArguments(int from) {
        return (String[]) ArrayUtils.subarray(arguments, from, arguments.length);
    }

    /**
     * Returns the size of the arguments array.
     *
     * @return
     */
    public int count() {
        return arguments.length;
    }

    /**
     * Returns the real index as specified in getArgument.
     * @param index       The index passed in getArgument.
     * @param allowLength Allow
     * @return The real index or -1 if the index is invalid.
     */
    private int getRealIndex(int index, boolean allowLength) {
        // Check validity of the index.
        if (index > arguments.length) {
            // Index greater than the count of arguments.
            return -1;
        } else if (!allowLength && index == arguments.length) {
            // Disallow passing the actual length of the argument.
            return -1;
        } else if (index < 0) {
            // Support python-like indices.
            int preIndex = index;
            index = arguments.length - index;

            // If the index is still invalid, throw an exception.
            if (index < 0) {
                return -1;
            }
        }

        return index;
    }

    /**
     * Returns the real index as specified in getArgument.
     * @param index The index passed in getArgument.
     * @return The real index or -1 if the index is invalid.
     */
    private int getRealIndex(int index) {
        return getRealIndex(index, false);
    }

    /**
     * Returns the converted argument at the given index.<p />
     *
     * The index-Argument is specially crafted: If the index is greater or equals 0 the default
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
    public <T> T getArgument(int index, Class<?> cls, T def) {
        int preIndex = index;
        index = this.getRealIndex(index);
        if (index == -1)
            throw new ArrayIndexOutOfBoundsException(preIndex);

        // Make the Class-Instance the Class-Referecne to the Wrapper-Type.
        if (cls.isPrimitive()) { cls = PrimitiveType.wrap(cls); }

        // Use the current argument handler.
        ArgumentHandler handler = this.handler.getArgumentHandler();

        // Make sure the handler supports the given type.
        if (!handler.supportsType(cls)) {
            this.handler.getServerBackend().getLogger().warning("Unknown type: " + cls);
            return def;
        }

        // Converts the specified result.
        Object result = handler.convertType(arguments[index], cls, this.executor, this.handler.getServerBackend());

        // Return the result if the result is not null, otherwise the default value will be returned.
        return result==null?def:(T) result;
    }

    /**
     * Returns the converted argument at the given index.<p />
     *
     * The index-Argument is specially crafted: If the index is greater or equals 0 the default
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
    public <T> T getArgument(int index, Class<T> cls) {
        if (this.getRealIndex(index) == -1)
            throw new ArrayIndexOutOfBoundsException(index);

        if (!this.handler.getArgumentHandler().supportsType(cls))
            throw new IllegalArgumentException("Argument-type not supported.");

        T result = this.getArgument(index, cls, null);
        if (cls.isPrimitive() && result == null) {
            throw new NumberFormatException("Failed to parse number.");
        }
        return result;
    }

    /**
     * Returns a string with the given value.<p />
     *
     * The index-Argument is specially crafted: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.<p />
     *
     * If the given class is the Class-instance for a primitive type, a NumberFormatException will
     * be thrown if the number couldn't be parsed.<p />
     *
     * @param index the index of the required value.
     */
    public String getString(int index) {
        String result = getArgument(index, String.class, null);
        if (result == null) return this.arguments[getRealIndex(index)];
        return result;
    }

    /**
     * Joins the arguments beginning by the specified beginIndex (inclusive).
     * @param beginIndex Index of first argument.
     * @return The joined string.
     */
    public String getJoinedString(int beginIndex) {
        return getJoinedString(beginIndex, this.arguments.length);
    }

    /**
     * Joins the arguments using the given indexes.
     * @param beginIndex First Index (Inclusive)
     * @param endIndex   Last Index (Exclusive)
     * @return The joined string.
     */
    public String getJoinedString(int beginIndex, int endIndex) {
        int pBegin = beginIndex, pEnd = endIndex;

        beginIndex = getRealIndex(beginIndex, false);    // Inclusive
        endIndex = getRealIndex(endIndex, true);         // Exclusive

        if (beginIndex < 0)
            throw new ArrayIndexOutOfBoundsException(pBegin);

        if (endIndex < 0)
            throw new ArrayIndexOutOfBoundsException(pEnd);

        String[] arguments = (String[]) ArrayUtils.subarray(this.arguments, beginIndex, endIndex);
        return StringUtils.join(arguments, " ");
    }

    /**
     * Returns the int at the given index.<p />
     *
     * The index-Argument is specially crafted: If the index is greater or equals 0 the default
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
     * The index-Argument is specially crafted: If the index is greater or equals 0 the default
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
     * The index-Argument is specially crafted: If the index is greater or equals 0 the default
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
     * The index-Argument is specially crafted: If the index is greater or equals 0 the default
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
     * The index-Argument is specially crafted: If the index is greater or equals 0 the default
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
     * The index-Argument is specially crafted: If the index is greater or equals 0 the default
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
     * The index-Argument is specially crafted: If the index is greater or equals 0 the default
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
     * The index-Argument is specially crafted: If the index is greater or equals 0 the default
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
     * The index-Argument is specially crafted: If the index is greater or equals 0 the default
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
     * The index-Argument is specially crafted: If the index is greater or equals 0 the default
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
     * The index-Argument is specially crafted: If the index is greater or equals 0 the default
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
     * The index-Argument is specially crafted: If the index is greater or equals 0 the default
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
     * The index-Argument is specially crafted: If the index is greater or equals 0 the default
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
     * The index-Argument is specially crafted: If the index is greater or equals 0 the default
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
     * The index-Argument is specially crafted: If the index is greater or equals 0 the default
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
     * The index-Argument is specially crafted: If the index is greater or equals 0 the default
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
