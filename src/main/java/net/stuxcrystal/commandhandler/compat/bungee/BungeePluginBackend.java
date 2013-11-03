package net.stuxcrystal.commandhandler.compat.bungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.stuxcrystal.commandhandler.CommandBackend;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.CommandHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Backend for BungeeCord Plugins.
 */
public class BungeePluginBackend implements CommandBackend<Plugin, CommandSender> {

    /**
     * Reference to the plugin.
     */
    private final Plugin plugin;

    /**
     * Represents the underlying command handler.
     */
    private CommandHandler handler = null;

    public BungeePluginBackend(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void setCommandHandler(CommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public Logger getLogger() {
        return this.plugin.getLogger();
    }

    @Override
    public void schedule(Runnable runnable) {
        // Fortunately this API supports scheduling asynchronous tasks.
        this.plugin.getProxy().getScheduler().runAsync(this.plugin, runnable);
    }

    @Override
    public CommandExecutor<?>[] getPlayers() {
        List<ProxiedPlayer> players = new ArrayList<>(this.plugin.getProxy().getPlayers());
        CommandExecutor[] executors = new CommandExecutor[players.size()];
        for (int i = 0; i<players.size(); i++) {
            executors[i] = wrapPlayer(players.get(i));
        }
        return executors;
    }

    @Override
    public CommandExecutor<?> getExecutor(String name) {
        if (name == null || name.isEmpty())
            return wrapPlayer(this.plugin.getProxy().getConsole());
        return wrapPlayer(this.plugin.getProxy().getPlayer(name));
    }

    @Override
    public CommandExecutor<?> wrapPlayer(CommandSender player) {
        if (player == null) return null;
        return new BungeeSenderWrapper(handler, player);
    }

    @Override
    public CommandExecutor<?> getConsole() {
        return wrapPlayer(this.plugin.getProxy().getConsole());
    }

    @Override
    public Plugin getHandle() {
        return this.plugin;
    }

    @Override
    public Boolean hasPermission(CommandExecutor<?> executor, String node) {
        return ((CommandSender) executor.getHandle()).hasPermission(node);
    }
}
