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

package net.stuxcrystal.commandhandler.compat.forge;

import cpw.mods.fml.common.FMLLog;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.stuxcrystal.commandhandler.CommandBackend;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.CommandHandler;

import java.util.logging.Logger;

/**
 * The plugin backend for forge.
 */
public class ForgePluginBackend<T> implements CommandBackend<T, ICommandSender> {

    /**
     * Reference to a forge mod.
     */
    private final T mod;

    /**
     * The logger to use.
     */
    private final Logger logger;

    /**
     * The command-handler to use.
     */
    private CommandHandler handler;

    /**
     * Async-Task warning
     */
    private boolean warned = false;

    /**
     * Constructs the plugin backend with the given mod.
     * @param mod The mod to use.
     */
    public ForgePluginBackend(T mod) {
        this(mod, FMLLog.getLogger());
    }

    /**
     * Constructs the plugin backend with the given mod and logger.
     * @param mod    The mod to use.
     * @param logger The logger to use.
     */
    public ForgePluginBackend(T mod, Logger logger) {
        this.mod = mod;
        this.logger = logger;
    }

    @Override
    public Logger getLogger() {
        return this.logger;
    }

    @Override
    public void schedule(Runnable runnable) {
        if (!warned) {
            this.logger.warning("[SimpleDev:CommandHandler] Forge does not support asynchronous tasks out of the box. Defaulting to Threads.");
            warned = true;
        }

        new Thread(runnable).start();
    }

    @Override
    public CommandExecutor<?> getExecutor(String name) {
        return wrapExecutor(MinecraftServer.getServer().getConfigurationManager().getPlayerForUsername(name));
    }

    @Override
    public CommandExecutor<?> wrapPlayer(ICommandSender player) {
        return wrapExecutor(player);
    }

    @Override
    public CommandExecutor<?> getConsole() {
        return wrapExecutor(MinecraftServer.getServer());
    }

    public CommandExecutor wrapExecutor(ICommandSender sender) {
        return new ForgeCommandExecutor(sender, this.handler);
    }

    @Override
    public T getHandle() {
        return mod;
    }

    /**
     * Forge does not provide a permissions system by itself.
     *
     * @param executor The executor.
     * @param node     The node to use.
     * @return null.
     */
    @Override
    public Boolean hasPermission(CommandExecutor<?> executor, String node) {
        return null;
    }

    /**
     * The command handler to use.
     * @param handler The command handler.
     */
    public void setCommandHandler(CommandHandler handler) {
        this.handler = handler;
    }
}
