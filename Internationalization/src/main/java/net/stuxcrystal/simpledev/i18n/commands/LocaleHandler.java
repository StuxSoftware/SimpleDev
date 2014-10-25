package net.stuxcrystal.simpledev.i18n.commands;

import java.util.Locale;

/**
 * Retrieves the locale of the command executor.
 */
public interface LocaleHandler {

    /**
     * Returns the locale for the locale handler.
     * @return The locale handler.
     */
    public Locale getLocale();

}
