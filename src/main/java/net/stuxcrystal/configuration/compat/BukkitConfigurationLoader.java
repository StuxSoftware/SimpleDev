package net.stuxcrystal.configuration.compat;

import net.stuxcrystal.configuration.ConfigurationLoader;
import net.stuxcrystal.configuration.Constructor;
import net.stuxcrystal.configuration.logging.JULBinding;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Bukkit version for a configuration loader.<p />
 * <p/>
 * Automatically hooks into the logging system of the plugin.
 */
public class BukkitConfigurationLoader extends ConfigurationLoader {

    /**
     * Uses the default parameters for the configuration loader.
     *
     * @param plugin The plugin that the logger should use.
     */
    public BukkitConfigurationLoader(JavaPlugin plugin) {
        super();
        this.setLoggingInterface(new JULBinding(plugin.getLogger()));
    }

    /**
     * Uses the given constructor to initialize the configuration loader.
     *
     * @param plugin      The plugin which logger should be used.
     * @param constructor The constructor-object that adds the types and resolvers of the configuration loader.
     */
    public BukkitConfigurationLoader(JavaPlugin plugin, Constructor constructor) {
        super(constructor);
        this.setLoggingInterface(new JULBinding(plugin.getLogger()));
    }
}
