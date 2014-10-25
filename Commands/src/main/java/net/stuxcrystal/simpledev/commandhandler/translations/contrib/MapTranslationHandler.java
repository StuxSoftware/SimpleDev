package net.stuxcrystal.simpledev.commandhandler.translations.contrib;

import net.stuxcrystal.simpledev.commandhandler.CommandExecutor;
import net.stuxcrystal.simpledev.commandhandler.translations.TranslationHandler;

import java.util.Map;

/**
 * Translation-Handler that uses Maps.
 */
public class MapTranslationHandler implements TranslationHandler {

    /**
     * Contains the translations.
     */
    private final Map<String, String> translations;

    /**
     * Creates the new translation handler.
     * @param translations The translations.
     */
    public MapTranslationHandler(Map<String, String> translations) {
        this.translations = translations;
    }

    @Override
    public String getTranslation(CommandExecutor sender, String key) {
        return translations.get(key);
    }
}
