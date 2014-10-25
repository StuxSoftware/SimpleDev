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

package net.stuxcrystal.simpledev.commandhandler;

import net.stuxcrystal.simpledev.commandhandler.contrib.DefaultPermissionHandler;
import net.stuxcrystal.simpledev.commandhandler.contrib.history.History;
import net.stuxcrystal.simpledev.commandhandler.contrib.history.HistoryComponent;
import net.stuxcrystal.simpledev.commandhandler.contrib.sessions.SessionManager;
import net.stuxcrystal.simpledev.commandhandler.contrib.sessions.Sessions;
import net.stuxcrystal.simpledev.commandhandler.utils.HandleWrapper;

/**
 * Represents the sender of a command.
 */
public abstract class CommandExecutor<T> extends HandleWrapper<T> {

    /**
     * The CommandHandler providing the handler.
     */
    private final CommandHandler handler;


    /**
     * The handler.
     * @param handle  The handle that is wrapped by the wrapper.
     * @param handler The handler providing the permission handler.
     */
    public CommandExecutor(T handle, CommandHandler handler) {
        super(handle);
        this.handler = handler;
    }

    /**
     * @return Returns the name of the sender.
     */
    public abstract String getName();

    /**
     * Sends one or more messages.
     *
     * @param message The messages to send.
     */
    public abstract void sendMessage(String... message);

    /**
     * Checks if the sender has a certain permission.
     *
     * @param node The node to test.
     * @return true if the sender has the permission needed.
     */
    public final boolean hasPermission(String node) {
        PermissionHandler handler = this.handler.getPermissionHandler();
        if (handler == null)
            this.handler.setPermissionHandler(handler = new DefaultPermissionHandler(this.handler));

        return handler.hasPermission(this, node);
    }

    /**
     * Returns the type of the sender.
     *
     * @return true if the sender is a player.
     */
    public abstract boolean isPlayer();

    /**
     * Returns the type of the player.
     *
     * @return true if the sender is the console or is op. Otherwise false.
     */
    public abstract boolean isOp();

    /**
     * Returns the command handler that is associated with
     * this executor.
     * @return The command handler that is associated with this executor.
     */
    @Override
    public CommandHandler getCommandHandler() {
        return this.handler;
    }

    /**
     * Returns the history of the given player.
     * @return The history of the given player.
     */
    public History getHistory() {
        // Make sure that the history subsystem has been registered.
        if (!this.getCommandHandler().isComponentRegistered(HistoryComponent.class))
            this.getCommandHandler().registerComponent(HistoryComponent.class);

        return this.getComponent(History.class);
    }

    /**
     * Returns the manager for sessions.
     * @return The history for sessions.
     */
    public Sessions getSessions() {
        if (!this.getCommandHandler().isComponentRegistered(SessionManager.class))
            this.getCommandHandler().registerComponent(new SessionManager(this.getCommandHandler()));
        return this.getComponent(Sessions.class);
    }

    /**
     * Compare the underlying handles.
     *
     * @param otherObject an other object.
     * @return true if the executor have the same handle.
     */
    @Override
    public boolean equals(Object otherObject) {
        if (!(otherObject instanceof CommandExecutor)) return false;
        T me = this.getHandle();
        Object other = ((CommandExecutor) otherObject).getHandle();

        return (me == null) == (other == null) && me != null && me.equals(other);
    }

    /**
     * Creates a string representation for the executor.
     * @return The string that contains data for the executor.
     */
    @Override
    public String toString() {
        return "CommandExecutor[" + this.getHandle().toString() + "]";
    }

    /**
     * Use the hash code of the underlying object.
     *
     * @return The hash-code of the object.
     */
    @Override
    public int hashCode() {
        T me = this.getHandle();
        if (me == null) return super.hashCode();
        return me.hashCode();
    }

    /**
     * @return The backend that manages the players.
     */
    public CommandBackend getBackend() {
        return this.handler.backend;
    }

}
