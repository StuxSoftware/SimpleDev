package net.stuxcrystal.simpledev;

import net.md_5.bungee.api.plugin.Plugin;
import net.stuxcrystal.commandhandler.compat.bungee.BungeeCommandHandler;
import net.stuxcrystal.configuration.compat.BungeeConfigurationLoader;

/**
 * Support for BungeeCord.
 */
public class BungeeRepresentation extends ServerRepresentation<BungeeCommandHandler, BungeeConfigurationLoader, Plugin> {

    /**
     * Constructs the server representation.
     *
     * @param plugin The plugin.
     */
    public BungeeRepresentation(Plugin plugin) {
        super(plugin);
    }


    @Override
    public BungeeCommandHandler createCommandHandler() {
        return new BungeeCommandHandler(this.plugin);
    }

    @Override
    public BungeeConfigurationLoader getConfigurationLoader() {
        return new BungeeConfigurationLoader(this.plugin);
    }
}
