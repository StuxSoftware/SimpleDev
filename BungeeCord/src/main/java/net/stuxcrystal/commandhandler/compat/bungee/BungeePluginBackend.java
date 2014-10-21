package net.stuxcrystal.commandhandler.compat.bungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;
import net.stuxcrystal.commandhandler.CommandBackend;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.contrib.scheduler.FallbackSchedulerBackend;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Backend for BungeeCord Plugins.
 */
public class BungeePluginBackend extends CommandBackend<Plugin, CommandSender> {

    private FallbackSchedulerBackend fallbackScheduler;

    /**
     * Creates a new handle.
     *
     * @param handle The handle in the backend.
     */
    protected BungeePluginBackend(Plugin handle) {
        super(handle);
        this.fallbackScheduler = new FallbackSchedulerBackend();
    }

    @Override
    public Logger getLogger() {
        return this.getHandle().getLogger();
    }

    @Override
    public void scheduleAsync(Runnable runnable) {
        // Fortunately this API supports scheduling asynchronous tasks.
        this.getHandle().getProxy().getScheduler().runAsync(this.getHandle(), runnable);
    }

    @Override
    public void scheduleSync(Runnable runnable) {
        // There is no such thing as synchronous task.
        this.scheduleAsync(runnable);
    }

    @Override
    public boolean inMainThread() {
        return true;
    }

    @Override
    public CommandExecutor<?>[] getPlayers() {
        List<ProxiedPlayer> players = new ArrayList<>(this.getHandle().getProxy().getPlayers());
        CommandExecutor[] executors = new CommandExecutor[players.size()];
        for (int i = 0; i<players.size(); i++) {
            executors[i] = wrapPlayer(players.get(i));
        }
        return executors;
    }

    @Override
    public CommandExecutor<?> getExecutor(String name) {
        if (name == null || name.isEmpty() || "console".equalsIgnoreCase(name))
            return wrapPlayer(this.getHandle().getProxy().getConsole());
        return wrapPlayer(this.getHandle().getProxy().getPlayer(name));
    }

    @Override
    public CommandExecutor<?> wrapPlayer(CommandSender player) {
        if (player == null) return null;
        return new BungeeSenderWrapper(this.getCommandHandler(), player);
    }

    @Override
    public CommandExecutor<?> getConsole() {
        return wrapPlayer(this.getHandle().getProxy().getConsole());
    }

    @Override
    public Boolean hasPermission(CommandExecutor<?> executor, String node) {
        return ((CommandSender) executor.getHandle()).hasPermission(node);
    }
}
