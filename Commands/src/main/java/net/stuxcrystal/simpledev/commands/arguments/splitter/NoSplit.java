package net.stuxcrystal.simpledev.commands.arguments.splitter;

import net.stuxcrystal.simpledev.commands.arguments.ArgumentSplitter;

/**
 * Completely disable splitting.
 */
public class NoSplit implements ArgumentSplitter {

    /**
     * Doesn't split the arguments.
     * The first item in the array are always the flags of the plugin (but they are empty because we don't parse them).
     *
     * @param args The input-string.
     * @return {@code String[]{"", args}}
     */
    @Override
    public String[] split(String args) {
        return new String[]{"", args};
    }
}
