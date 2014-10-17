package net.stuxcrystal.configuration.compat;

import net.md_5.bungee.api.plugin.Plugin;
import net.stuxcrystal.configuration.ConfigurationLoader;
import net.stuxcrystal.configuration.Constructor;
import net.stuxcrystal.configuration.logging.JULBinding;

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
        super();
        this.setLoggingInterface(new JULBinding(plugin.getLogger()));
    }

    /**
     * Uses the given constructor to initialize the configuration loader.
     *
     * @param plugin      The plugin which logger should be used.
     * @param constructor The constructor-object that adds the types and resolvers of the configuration loader.
     */
    public BungeeConfigurationLoader(Plugin plugin, Constructor constructor) {
        super(constructor);
        this.setLoggingInterface(new JULBinding(plugin.getLogger()));
    }

}
