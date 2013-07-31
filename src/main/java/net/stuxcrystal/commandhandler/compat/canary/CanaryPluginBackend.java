package net.stuxcrystal.commandhandler.compat.canary;

import net.canarymod.Canary;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.plugin.Plugin;
import net.stuxcrystal.commandhandler.Backend;
import net.stuxcrystal.commandhandler.CommandExecutor;

import java.util.logging.Logger;

/**
 * Backend for the Canary-Mod-Server.
 */
public class CanaryPluginBackend implements Backend<Plugin> {

    final Plugin plugin;

    public CanaryPluginBackend(Plugin plugin) {

        this.plugin = plugin;
        this.plugin.getLogman().warning("CanaryMod Recode does not support asynchronous tasks out of the box. Falling back to threads.");
    }

    @Override
    public Logger getLogger() {
        return this.plugin.getLogman();
    }

    @Override
    public void schedule(Runnable runnable) {
        new Thread(runnable).start();
    }

    @Override
    public CommandExecutor<?> getExecutor(String name) {
        if (name == null || name.isEmpty()) {
            return wrapReceiver(Canary.getServer());
        }

        return wrapReceiver(Canary.getServer().getPlayer(name));
    }

    @Override
    public Plugin getHandle() {
        return this.plugin;
    }

    CommandExecutor<?> wrapReceiver(MessageReceiver receiver) {
        return new CanarySenderWrapper(receiver);
    }
}
