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

package net.stuxcrystal.simpledev.commandhandler.compat.bukkit;

import net.stuxcrystal.simpledev.commandhandler.CommandHandler;
import net.stuxcrystal.simpledev.commandhandler.compat.bukkit.contrib.commands.BukkitCommandExecutorContainer;
import net.stuxcrystal.simpledev.commandhandler.compat.bukkit.contrib.commands.BukkitCommandLoader;
import net.stuxcrystal.simpledev.commandhandler.compat.bukkit.scheduler.BukkitSchedulerWrapper;
import org.apache.commons.lang.ArrayUtils;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

/**
 * Handler for Bukkit Plugins.
 */
public class BukkitCommandHandler extends CommandHandler implements CommandExecutor {
    /**
     * The Constructor for base-commands.
     *
     * @param plugin The backend that needs the command handler.
     */
    public BukkitCommandHandler(Plugin plugin) {
        super(new BukkitPluginBackend(plugin));
        this.initHandler();
    }

    /**
     * The constructor for base-commands.
     *
     * @param handler The handler to copy the values from.
     */
    public BukkitCommandHandler(BukkitCommandHandler handler) {
        super(handler);
        this.initHandler();
    }

    /**
     * Initializes the command handler.
     */
    private void initHandler() {
        this.registerCommandLoader(new BukkitCommandLoader());
        this.registerComponent(new BukkitSchedulerWrapper((BukkitPluginBackend)this.getServerBackend()));
    }

    /**
     * Simple command handler for subcommands.
     *
     * @param _sender   The sender that sends the command.
     * @param command   The internal command
     * @param label     The alias used to execute this command.
     * @param arguments The arguments passed to the command.
     */
    @Override
    public boolean onCommand(CommandSender _sender, org.bukkit.command.Command command, String label, String[] arguments) {
        if (arguments.length == 0) {
            // Make a command out of it.
            arguments = new String[]{CommandHandler.FALLBACK_COMMAND_NAME};
        }

        net.stuxcrystal.simpledev.commandhandler.CommandExecutor<?> sender = ((BukkitPluginBackend) this.backend).wrapSender(_sender);

        if (!this.execute(sender, arguments[0], (String[]) ArrayUtils.remove(arguments, 0)))
            sender.sendMessage(T(sender, "cmd.notfound"));

        return true;
    }

    /**
     * Use this function to implement a command-switch for the backend.
     *
     * @param _sender   The sender.
     * @param command   The command
     * @param label     The label
     * @param arguments The arguments
     * @return true if the command was found.
     */
    public boolean commandSwitch(CommandSender _sender, @SuppressWarnings("unused") org.bukkit.command.Command command, String label, String[] arguments) {
        net.stuxcrystal.simpledev.commandhandler.CommandExecutor<?> sender = ((BukkitPluginBackend) this.backend).wrapSender(_sender);
        return this.execute(sender, label, arguments);
    }

    /**
     * Register the native command.
     * @param name      The name of the command.
     * @param executor  The executor used.
     */
    public void registerCommand(String name, CommandExecutor executor) {
        this.registerCommands(new BukkitCommandExecutorContainer(name, executor));
    }
}
