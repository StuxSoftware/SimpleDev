package net.stuxcrystal.commandhandler.compat.bukkit;

import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.TranslationHandler;
import net.stuxcrystal.commandhandler.TranslationManager;
import org.bukkit.configuration.MemoryConfiguration;

import java.util.Map;


/**
 * A handler using a memory configuration.
 */
public class ConfigurationLocalizationHandler implements TranslationHandler {

    /**
     * Starts the memory section.
     */
    protected MemoryConfiguration section;

    /**
     * Initializes the command handler.
     *
     * @param configuration
     */
    public ConfigurationLocalizationHandler(MemoryConfiguration configuration) {
        update(configuration);
    }

    /**
     * Returns the translation for the CommandHandler.
     *
     * @param sender The sender that sent the command.
     * @param key    The Key of the translation.
     * @return
     */
    @Override
    public String getTranslation(CommandExecutor sender, String key) {
        return this.section.getString(key);
    }

    /**
     * Updates the configuration data.
     *
     * @param configuration
     */
    public void update(MemoryConfiguration configuration) {
        this.section = configuration;
        // A little hack to enforce a cast from Map<String, String> to Map<String, Object>.
        this.section.addDefaults((Map<String, Object>) (Map) TranslationManager.getDefaults());
    }
}
