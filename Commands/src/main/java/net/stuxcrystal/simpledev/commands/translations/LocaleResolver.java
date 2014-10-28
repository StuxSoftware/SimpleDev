package net.stuxcrystal.simpledev.commands.translations;

import java.util.Locale;

/**
 * The local resolver.
 */
public interface LocaleResolver {

    /**
     * Returns the locale of the resolver.
     * @return The locale of the resolver.
     */
    public Locale getLocale();

}
