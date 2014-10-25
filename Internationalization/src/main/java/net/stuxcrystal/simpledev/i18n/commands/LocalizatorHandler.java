package net.stuxcrystal.simpledev.i18n.commands;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.translations.TranslationManager;
import net.stuxcrystal.simpledev.i18n.Localizator;

import java.util.Locale;

/**
 * The bridge between the localization package and the translation handler.
 */
public class LocalizatorHandler implements TranslationManager {

    /**
     * Reference to the localizator.
     */
    private final Localizator localizator;

    /**
     * The handler for the localizator.
     * @param localizator The localizator the translation requests are forwarded to.
     */
    public LocalizatorHandler(Localizator localizator) {
        this.localizator = localizator;
    }

    /**
     * Passes the execution to the localizator.
     *
     * @param executor The executor that will provide the translations.
     * @param key      The keys.
     * @param values   The values.
     *
     * @return The translated message.
     */
    @Override
    public String translate(CommandExecutor executor, String key, Object... values) {
        // Get the locale of the localizator handler.
        Locale locale = null;
        if (executor.getCommandHandler().hasFunction("__locale_exists", CommandExecutor.class)) {
            locale = ((LocaleHandler)executor.getComponent(LocaleHandler.class)).getLocale();
        }

        // The translator.
        return this.localizator.translate(locale, key, values);
    }
}
