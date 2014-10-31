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

package net.stuxcrystal.simpledev.commands;

import net.stuxcrystal.simpledev.commands.contrib.scheduler.Scheduler;
import net.stuxcrystal.simpledev.commands.contrib.scheduler.fallback.TaskComponent;
import net.stuxcrystal.simpledev.commands.utils.HandleWrapper;
import org.apache.commons.lang.StringUtils;

import java.util.logging.Logger;

/**
 * Backend of the command handler. Used to communicate with the program.
 *
 * @param <T> The type of the backend.
 * @param <P> The type of the player.
 */
public abstract class CommandBackend<T, P> extends HandleWrapper<T> {

    /**
     * Contains the command handler of the backend.
     */
    private CommandHandler handler;

    /**
     * Creates a new handle.
     *
     * @param handle The handle in the backend.
     */
    protected CommandBackend(T handle) {
        super(handle);
    }

    /**
     * Sets the command handler for the plugin.
     * @param handler The new command handler.
     */
    void setCommandHandler(CommandHandler handler) {
        this.handler = handler;
    }

    /**
     * Returns the command handler of the backend.
     * @return The command-handler of the backend.
     */
    @Override
    public CommandHandler getCommandHandler() {
        return this.handler;
    }

    /**
     * Called if the logger has to be used.
     *
     * @return Reference to the logger.
     */
    public abstract Logger getLogger();

    /**
     * Schedules an asynchronous task.
     *
     * @param runnable The runnable that should be executed
     */
    public abstract void scheduleAsync(Runnable runnable);

    /**
     * Schedules a task that runs in the server main thread.
     *
     * @param runnable The runnable that should be executed.
     */
    public abstract void scheduleSync(Runnable runnable);

    /**
     * Checks if the called of the method is currently in the main thread of the server.
     *
     * @return {@code true} if the task currently runs in the main thread.
     */
    public abstract boolean inMainThread();
    
    /**
     * Returns all players that are currently logged in.
     */
    public abstract CommandExecutor<?>[] getPlayers();

    /**
     * Returns an CommandExecutor by the given name.
     *
     * @param name The name of the executor. If null or empty, the console executor will be returned.
     */
    public abstract CommandExecutor<?> getExecutorExact(String name);

    /**
     * <p>Tries to match a executor.</p>
     * <p>
     *     It tries to match the executor name using these criteria:
     *     <ol>
     *         <li>Empty string, {@code null} or "CONSOLE"(case ignored) will always return the console user.</li>
     *         <li>The exact name</li>
     *         <li>The exact name disregarding case.</li>
     *         <li>The name starts with the passed parameter (respecting the case)</li>
     *         <li>The name starts with the passed parameter (disregarding case)</li>
     *         <li>The name contains the passed parameter (respecting the case)</li>
     *         <li>The name contains the passed parameter (disregarding case)</li>
     *     </ol>
     * </p>
     * @param name The name to match.
     * @return An executor that has been found.
     */
    public CommandExecutor<?> getExecutor(String name) {
        if (StringUtils.isBlank(name) || "CONSOLE".equalsIgnoreCase(name))
            return this.getConsole();

        CommandExecutor<?>[] executors = this.getPlayers();

        // Check for the exact name.
        for (CommandExecutor<?> executor : executors) {
            if (executor.getName().equals(name))
                return executor;
        }
        // Check for names
        for (CommandExecutor<?> executor : executors) {
            if (executor.getName().equalsIgnoreCase(name))
                return executor;
        }

        // Check for names that starts with the given name.
        for (CommandExecutor<?> executor : executors) {
            if (executor.getName().startsWith(name))
                return executor;
        }

        // Check for names that start with the given name.
        for (CommandExecutor<?> executor : executors) {
            if (executor.getName().toLowerCase().startsWith(name.toLowerCase()))
                return executor;
        }

        // Search for any executor that contains the name.
        for (CommandExecutor<?> executor : executors) {
            if (executor.getName().contains(name))
                return executor;
        }

        // Search for any executor that contains the name not regarding the name.
        for (CommandExecutor<?> executor : executors) {
            if (executor.getName().toLowerCase().contains(name.toLowerCase()))
                return executor;
        }

        return null;
    }

    /**
     * Wraps a player object.
     * @param player The player (sender) to wrap.
     * @return The wrapped player.
     */
    public abstract CommandExecutor<?> wrapPlayer(P player);

    /**
     * Returns an CommandExecutor that represents the console.
     * @return The Executor-Object that represents the console.
     */
    public abstract CommandExecutor<?> getConsole();

    /**
     * Checks if the user has the permission.
     *
     * @param executor The executor.
     * @param node     The node to use.
     * @return null, if the given backend does not support permissions.
     */
    public Boolean hasPermission(CommandExecutor<?> executor, String node) {
        return null;
    }

    /**
     * Returns the scheduler for this object.
     * @return The scheduler for this object.
     */
    public Scheduler getScheduler() {
        if (!this.getCommandHandler().hasFunction("__scheduler_exists", CommandBackend.class)) {
            this.getCommandHandler().registerComponent(new TaskComponent());
            this.getLogger().warning("Beware that the delay may not be accurate.");
        }
        return this.getComponent(Scheduler.class);
    }

}
