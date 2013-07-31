package net.stuxcrystal.commandhandler.compat.canary;

import net.canarymod.Translator;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.TranslationHandler;

/**
 * Translation handler for canary mod.
 */
public class CanaryTranslationHandler implements TranslationHandler {

    /**
     * This translation class uses the inbuilt localization feature of Canary-Recode.
     *
     * @param sender The sender that sent the command.
     * @param key    The Key of the translation.
     * @return The translated message.
     */
    @Override
    public String getTranslation(CommandExecutor sender, String key) {
        return Translator.translate(key);
    }

}
