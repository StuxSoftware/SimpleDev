package net.stuxcrystal.simpledev.commands.translations;

import net.stuxcrystal.simpledev.commands.CommandExecutor;

/**
 * The basic implementation of a translation manager.
 */
public interface TranslationManager {

    /**
     * Actually begins the translation process.
     *
     * @param executor The executor that will provide the translations.
     * @param key      The keys.
     * @param values   The values.
     * @return The translated message.
     */
    public String translate(CommandExecutor executor, String key, Object... values);

    /**
     * Adds an {@link ValueResolver} that is providing the default variables.
     * @param resolver The resolver to add.
     */
    public void addValueResolver(ValueResolver resolver);

    /**
     * Removes an {@link ValueResolver} from this translation manager.
     * @param resolver The resolver to remove.
     */
    public void removeValueResolver(ValueResolver resolver);

}
