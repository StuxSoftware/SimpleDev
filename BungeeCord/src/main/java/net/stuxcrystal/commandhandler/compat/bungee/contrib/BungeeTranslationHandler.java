package net.stuxcrystal.commandhandler.compat.bungee.contrib;

import net.md_5.bungee.api.ProxyServer;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.translations.TranslationHandler;

/**
 * Translation handler for BungeeCord.
 */
public class BungeeTranslationHandler implements TranslationHandler {

    /**
     * Translates using a translation handler.
     * @param sender The sender that sent the command.
     * @param key    The Key of the translation.
     * @return The translation handler.
     */
    @Override
    public String getTranslation(CommandExecutor sender, String key) {
        return ProxyServer.getInstance().getTranslation(key);
    }
}
