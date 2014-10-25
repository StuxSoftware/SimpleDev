package net.stuxcrystal.simpledev.i18n;

import java.util.Locale;

/**
 * The basic translation resolver.
 */
public interface TranslationResolver {

    /**
     * Resolves the translation.
     * @param locale  The language everything should be translated.
     * @param key     The key to translate.
     * @return The translated string.
     */
    public String getTranslation(Locale locale, String key);

}
