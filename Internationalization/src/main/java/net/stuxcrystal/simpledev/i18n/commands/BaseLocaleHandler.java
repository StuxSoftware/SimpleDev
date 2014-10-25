package net.stuxcrystal.simpledev.i18n.commands;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.component.ComponentContainer;

import java.util.Locale;

/**
 * Abstract class for implementations
 */
public interface BaseLocaleHandler extends ComponentContainer {

    /**
     * Internal function.
     */
    public void __locale_exists();

    /**
     * Retrieves the locale for the handler.
     * @param executor The executor for the handler.
     * @return The executor for the handler.
     */
    public Locale getLocale(CommandExecutor executor);

}
