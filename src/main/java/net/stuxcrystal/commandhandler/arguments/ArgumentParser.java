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

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.NumberUtils;

/**
 * Argument Parser for BukkitInstallPlugin
 *
 * @author StuxCrystal
 */
public class ArgumentParser {

    /**
     * Raw arguments
     */
    private String[] arguments;

    /**
     * Raw flags.
     */
    private String flags;

    /**
     * Initializes the argument parser.
     *
     * @param args
     */
    public ArgumentParser(String[] args) {
        parseArgs(args);
    }

    /**
     * Parses the args.
     *
     * @param args The array of arguments to be parsed.
     */
    private void parseArgs(String[] args) {
        if (args.length == 0) {
            flags = "";
            arguments = args;
        } else if (args[0].startsWith("-")) {
            flags = args[0].substring(1);
            arguments = (String[]) ArrayUtils.remove(args, 0);
        } else {
            flags = "";
            arguments = args;
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
     * Returns a string with the given value.
     *
     * @param index the index of the required value
     */
    public String getString(int index) {
        return arguments[index];
    }

    /**
     * Parses the string at the given index.
     *
     * @param index The index
     * @return An integer.
     */
    public int getInt(int index) {
        return Integer.parseInt(arguments[index]);
    }

    /**
     * Parses the string at the given index.
     *
     * @param index The index
     * @return An integer.
     */
    public int getInt(int index, int def) {
        return index < arguments.length ? NumberUtils.toInt(arguments[index], def) : def;
    }

    /**
     * Parses the given argument.
     *
     * @param index The index of the argument.
     * @return A float.
     */
    public float getFloat(int index) {
        return Float.parseFloat(arguments[index]);
    }

    /**
     * Parses the given argument.
     *
     * @param index The index of the argument.
     * @param def   The default value.
     * @return A float.
     */
    public float getFloat(int index, float def) {
        return index < arguments.length ? NumberUtils.toFloat(arguments[index], def) : def;
    }

}
