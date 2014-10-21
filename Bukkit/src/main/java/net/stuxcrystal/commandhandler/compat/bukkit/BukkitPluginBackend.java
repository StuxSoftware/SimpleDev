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

import net.stuxcrystal.commandhandler.CommandBackend;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.CommandHandler;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.logging.Logger;

/**
 * Represents a backend backend.
 */
public class BukkitPluginBackend extends CommandBackend<Plugin,CommandSender> {
    /**
     * Creates a new handle.
     *
     * @param handle The handle in the backend.
     */
    protected BukkitPluginBackend(Plugin handle) {
        super(handle);
    }

    @Override
    public Logger getLogger() {
        return this.getHandle().getLogger();
    }

    @Override
    public void schedule(Runnable runnable) {
        this.getHandle().getServer().getScheduler().runTaskAsynchronously(this.getHandle(), runnable);
    }
    
    @Override
    public CommandExecutor<?>[] getPlayers() {
        Player[] players = this.getHandle().getServer().getOnlinePlayers();
        CommandExecutor[] executors = new CommandExecutor[players.length];
        for (int i = 0; i<players.length; i++) {
            executors[i] = wrapSender(players[i]);
        }
        return executors;
    }

    @Override
    public CommandExecutor<?> getExecutor(String name) {

        if (name == null || name.isEmpty())
            return wrapSender(this.getHandle().getServer().getConsoleSender());
        return wrapSender(this.getHandle().getServer().getPlayer(name));
    }

    @Override
    public CommandExecutor<?> wrapPlayer(CommandSender player) {
        return wrapSender(player);
    }

    @Override
    public CommandExecutor<?> getConsole() {
        return wrapSender(this.getHandle().getServer().getConsoleSender());
    }

    /**
     * Checks if the sender has the permission needed.
     *
     * @param executor The executor.
     * @param node     The node to use.
     * @return {@code true} if the user has permission to do that.
     */
    @Override
    public Boolean hasPermission(CommandExecutor<?> executor, String node) {
        if (!(executor instanceof BukkitSenderWrapper)) return null;
        return ((BukkitSenderWrapper) executor).getHandle().hasPermission(node);
    }

    CommandExecutor<?> wrapSender(CommandSender sender) {
        if (sender == null) return null;
        return new BukkitSenderWrapper(sender, this.getCommandHandler());
    }
}
