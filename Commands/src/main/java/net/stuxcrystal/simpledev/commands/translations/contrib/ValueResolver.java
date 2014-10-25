package net.stuxcrystal.simpledev.commands.translations.contrib;

import net.stuxcrystal.simpledev.commands.CommandExecutor;

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
