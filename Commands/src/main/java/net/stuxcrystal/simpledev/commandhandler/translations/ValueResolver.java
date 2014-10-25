package net.stuxcrystal.simpledev.commandhandler.translations;

import net.stuxcrystal.simpledev.commandhandler.CommandExecutor;

import java.util.Map;

/**
 * Contains all values for variables.
 */
public interface ValueResolver {

    /**
     * Contains the map for values.
     * @param executor The executor of the translation.
     * @return The map with the values.
     */
    public Map<String, String> getFormatMap(CommandExecutor executor);

}
