package net.stuxcrystal.commandhandler.compat.bukkit;

import net.stuxcrystal.commandhandler.Backend;
import net.stuxcrystal.commandhandler.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

/**
 * Represents a backend backend.
 */
public class BukkitPluginBackend implements Backend<JavaPlugin> {

    private final JavaPlugin plugin;

    public BukkitPluginBackend(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Logger getLogger() {
        return this.plugin.getLogger();
    }

    @Override
    public void schedule(Runnable runnable) {
        this.plugin.getServer().getScheduler().runTaskAsynchronously(this.plugin, runnable);
    }

    @Override
    public CommandExecutor<?> getExecutor(String name) {
        if (name == null || name.isEmpty())
            return wrapSender(this.plugin.getServer().getConsoleSender());
        return wrapSender(this.plugin.getServer().getPlayer(name));
    }

    @Override
    public JavaPlugin getHandle() {
        return plugin;
    }

    CommandExecutor<?> wrapSender(CommandSender sender) {
        return new SenderWrapper(sender);
    }
}
