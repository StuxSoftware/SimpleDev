package net.stuxcrystal.configuration.compat;

import net.canarymod.plugin.Plugin;
import net.stuxcrystal.configuration.ConfigurationLoader;
import net.stuxcrystal.configuration.Constructor;
import net.stuxcrystal.configuration.logging.JULBinding;

/**
 * Canary-Recode version for a configuration loader.<p />
 * <p/>
 * Automatically hooks into the logging system of the plugin.
 */
public class CanaryConfigurationLoader extends ConfigurationLoader {

    /**
     * Uses the default parameters for the configuration loader.
     *
     * @param plugin The plugin that the logger should use.
     */
    public CanaryConfigurationLoader(Plugin plugin) {
        super();
        this.setLoggingInterface(new JULBinding(plugin.getLogman()));
    }

    /**
     * Uses the given constructor to initialize the configuration loader.
     *
     * @param plugin      The plugin which logger should be used.
     * @param constructor The constructor-object that adds the types and resolvers of the configuration loader.
     */
    public CanaryConfigurationLoader(Plugin plugin, Constructor constructor) {
        super(constructor);
        this.setLoggingInterface(new JULBinding(plugin.getLogman()));
    }
}
