package net.stuxcrystal.simpledev.commands.translations;

import net.stuxcrystal.simpledev.commands.CommandExecutor;

import java.util.Map;

/**
 * Contains all values for variables.
 */
public interface ValueResolver {

    /**
     * Contains the map for values.
     * @param manager    The manager that uses the translation.
     * @param executor   The executor that
     * @param name       The name of the object.
     * @return The map with the values.
     */
    public Object get(TranslationManager manager, CommandExecutor<?> executor, String name);

}
