package net.stuxcrystal.commandhandler;

/**
 * Returns the translation.
 *
 * @author StuxCrystal
 */
public interface TranslationHandler {

    /**
     * Returns the translation.
     *
     * @param sender The sender that sent the command.
     * @param key    The Key of the translation.
     * @return The translation.
     */
    public String getTranslation(CommandExecutor sender, String key);

}
