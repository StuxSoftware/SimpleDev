/*
 * Copyright 2013 StuxCrystal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package net.stuxcrystal.commandhandler.compat.bukkit;

import net.stuxcrystal.commandhandler.Backend;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.CommandHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

/**
 * Represents a backend backend.
 */
public class BukkitPluginBackend implements Backend<Plugin> {

    private final Plugin plugin;

    private CommandHandler handler;

    public BukkitPluginBackend(Plugin plugin) {
        this.plugin = plugin;
    }

    public void setCommandHandler(CommandHandler handler) {
        this.handler = handler;
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
    public Plugin getHandle() {
        return plugin;
    }

    @Override
    public Boolean hasPermission(CommandExecutor<?> executor, String node) {
        if (!(executor instanceof BukkitSenderWrapper)) return null;
        return ((BukkitSenderWrapper) executor).getHandle().hasPermission(node);
    }

    CommandExecutor<?> wrapSender(CommandSender sender) {
        return new BukkitSenderWrapper(sender, handler);
    }
}
