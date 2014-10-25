package net.stuxcrystal.simpledev.i18n;

import java.util.Locale;

/**
 * <p>This class is the base class for the localization system.</p>
 *
 * <p>
 *     It handles filters, plurals, etc...
 * </p>
 */
public class Localizator {

    /**
     * The resolver for the translation.
     */
    private TranslationResolver resolver;

    /**
     * Translates the values.
     * @param locale   The locale.
     * @param key      The key.
     * @param values   The values.
     * @return The translated string.
     */
    public String translate(Locale locale, String key, Object... values) {
        return key;
    }

}
