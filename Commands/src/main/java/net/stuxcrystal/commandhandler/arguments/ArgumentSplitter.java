package net.stuxcrystal.commandhandler.arguments;

/**
 * Splits arguments.
 */
public interface ArgumentSplitter {

    /**
     * Splits the arguments.<p />
     *
     * The first item in the array are always the flags of the plugin.
     *
     * @param args The input-string.
     * @return A list of string containing the actual arguments.
     */
    public String[] split(String args);

}
