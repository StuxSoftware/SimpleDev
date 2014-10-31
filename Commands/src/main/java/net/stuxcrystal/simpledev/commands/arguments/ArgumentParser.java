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
package net.stuxcrystal.simpledev.commands.arguments;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.CommandHandler;
import net.stuxcrystal.simpledev.commands.arguments.iterators.ArgumentIterable;
import net.stuxcrystal.simpledev.commands.arguments.types.PrimitiveType;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Argument Parser for BukkitInstallPlugin
 *
 * @author StuxCrystal
 */
public class ArgumentParser extends ArgumentIterable {

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
     * @param executor  The executor that executes the function.
     * @param handler   The handler that handles the function.
     * @param args      The arguments specified by the function.
     */
    public ArgumentParser(CommandExecutor executor, CommandHandler handler, String[] args) {
        super(null);
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
     * @return The count of arguments.
     */
    public int count() {
        return this.arguments.length;
    }

    /**
     * Returns the real index as specified in getArgument.
     * @param index       The index passed in getArgument.
     * @param allowLength Allow
     * @return The real index or -1 if the index is invalid.
     */
    private int getRealIndex(int index, boolean allowLength) {
        // Check validity of the index.
        if (index > this.arguments.length) {
            // Index greater than the count of arguments.
            return -1;
        } else if (!allowLength && index == this.arguments.length) {
            // Disallow passing the actual length of the argument.
            return -1;
        } else if (index < 0) {
            // Support python-like indices.
            index = this.arguments.length - index;

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
    @Override
    @SuppressWarnings("unchecked")
    public <T> T getArgument(int index, Class<T> cls) {
        Class<?> clazz = cls;

        int preIndex = index;
        index = this.getRealIndex(index);
        if (index == -1)
            throw new ArrayIndexOutOfBoundsException(preIndex);

        // Make the Class-Instance the Class-Reference to the Wrapper-Type.
        if (clazz.isPrimitive()) {
            clazz = PrimitiveType.wrap((Class<?>) cls);
        }

        // Use the current argument handler.
        ArgumentHandler handler = this.handler.getArgumentHandler();

        // Make sure the handler supports the given type.
        if (!handler.supportsType(clazz)) {
            throw new NumberFormatException("Unsupported type: " + cls);
        }

        // Convert and return the specified result.
        return (T) handler.convertType(arguments[index], clazz, this.executor, this.handler.getServerBackend());
    }

    /**
     * Returns a string with the given value.<p />
     *
     * The index argument has some additional features: If the index is greater or equals 0 the default
     * Java&trade;-Indexing of Arrays. If the index is under zero, the index is counted from
     * the last item on.<p />
     *
     * If the given class is the Class-instance for a primitive type, a NumberFormatException will
     * be thrown if the number couldn't be parsed.<p />
     *
     * @param index the index of the required value.
     */
    public String getString(int index) {
        String result = this.getArgument(index, String.class, null);
        if (result == null) return this.arguments[this.getRealIndex(index)];
        return result;
    }

    /**
     * Joins the arguments beginning by the specified beginIndex (inclusive).
     * @param beginIndex Index of first argument.
     * @return The joined string.
     */
    public String getJoinedString(int beginIndex) {
        return this.getJoinedString(beginIndex, this.arguments.length);
    }

    /**
     * Joins the arguments using the given indexes.
     * @param beginIndex First Index (Inclusive)
     * @param endIndex   Last Index (Exclusive)
     * @return The joined string.
     */
    public String getJoinedString(int beginIndex, int endIndex) {
        int pBegin = beginIndex, pEnd = endIndex;

        beginIndex = this.getRealIndex(beginIndex, false);    // Inclusive
        endIndex = this.getRealIndex(endIndex, true);         // Exclusive

        if (beginIndex < 0)
            throw new ArrayIndexOutOfBoundsException(pBegin);

        if (endIndex < 0)
            throw new ArrayIndexOutOfBoundsException(pEnd);

        String[] arguments = (String[]) ArrayUtils.subarray(this.arguments, beginIndex, endIndex);
        return StringUtils.join(arguments, " ");
    }

    /**
     * Returns the argument parser behind the iterable.
     * @return The argument parser behind the iterable.
     */
    @Override
    public ArgumentParser getArgumentParser() {
        return this;
    }
}
