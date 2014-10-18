package net.stuxcrystal.configuration.compat;

import net.md_5.bungee.api.plugin.Plugin;
import net.stuxcrystal.configuration.ConfigurationLoader;
import net.stuxcrystal.configuration.parser.Constructor;
import net.stuxcrystal.configuration.parser.logging.JULBinding;
import net.stuxcrystal.configuration.storage.contrib.filebased.FileBasedStorageBackend;

/**
 * Configuration Loader for BungeeCord Proxy Servers.
 */
public class BungeeConfigurationLoader extends ConfigurationLoader {

    /**
     * Uses the default parameters for the configuration loader.
     *
     * @param plugin The plugin that the logger should use.
     */
    public BungeeConfigurationLoader(Plugin plugin) {
        super(new FileBasedStorageBackend(".yml", plugin.getDataFolder()));
        this.setLoggingInterface(new JULBinding(plugin.getLogger()));
    }

    /**
     * Uses the given constructor to initialize the configuration loader.
     *
     * @param plugin      The plugin which logger should be used.
     * @param constructor The constructor-object that adds the types and resolvers of the configuration loader.
     */
    public BungeeConfigurationLoader(Plugin plugin, Constructor constructor) {
        super(new FileBasedStorageBackend(".yml", plugin.getDataFolder()), constructor);
        this.setLoggingInterface(new JULBinding(plugin.getLogger()));
    }

}
