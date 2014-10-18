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

package net.stuxcrystal.commandhandler;

import net.stuxcrystal.commandhandler.component.ComponentProxy;
import net.stuxcrystal.commandhandler.contrib.DefaultPermissionHandler;
import net.stuxcrystal.commandhandler.history.History;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * Represents the sender of a command.
 */
public abstract class CommandExecutor<T> {

    /**
     * Map of all recognized sessions handled by the command-executor.<p />
     *
     * The sessions are stored in a static variable because multiple CommandExecutors can access the same
     * handle.<p />
     *
     * The access to the sessions is thread-safe.
     */
    private static final Map<String, Map<Class<? extends Session>, Session>> SESSIONS = Collections.synchronizedMap(
            new HashMap<String, Map<Class<? extends Session>, Session>>()
    );

    /**
     * Sessions for a console-object.<p />
     *
     * Designed to handle only one type of Non-Player-Sessions.<p />
     *
     * The access to the sessions is thread-safe.
     */
    private static final Map<Class<? extends Session>, Session> CONSOLE_SESSIONS = Collections.synchronizedMap(
            new HashMap<Class<? extends Session>, Session>()
    );

    /**
     * The CommandHandler providing the handler.
     */
    private final CommandHandler handler;

    /**
     * The component proxy for the object.
     */
    private final ComponentProxy proxy = new ComponentProxy(this);

    /**
     * The handler.
     * @param handler The handler providing the permission handler.
     */
    public CommandExecutor(CommandHandler handler) {
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
     * Used to return a handle.
     *
     * @return
     */
    public abstract T getHandle();

    /**
     * Returns the command handler that is associated with
     * this executor.
     * @return The command handler that is associated with this executor.
     */
    public CommandHandler getCommandHandler() {
        return this.handler;
    }

    /**
     * Returns the history of the given player.
     * @return The history of the given player.
     */
    public History getHistory() {
        return this.getSession(History.class);
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

        if (me == null || other == null) return false;
        return me.equals(other);
    }

    /**
     * Use the hash code of the underlying object.
     *
     * @return
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

    /**
     * Creates a new session.
     */
    protected <S extends Session> S createSession(Class<S> cls) {

        S session;
        try {
            session = cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            // Log Exception.
            this.getBackend().getLogger().log(Level.WARNING, "Failed to create session of " + cls, e);
            return null;
        }

        session.initialize(this);
        return session;

    }

    /**
     * Returns the session with the given type.
     * @param sessions The session-container of this command-executor.
     * @param cls      The class that contains the session-container.
     * @param <S>      The type of the session object.
     * @return The session-object or null if the creation of the session failed.
     */
    private <S extends Session> S _getSession(Map<Class<? extends Session>, Session> sessions, Class<S> cls) {
        S result = (S) sessions.get(cls);
        if (result == null || result.isSessionExpired()) {
            result = this.createSession(cls);
            if (result == null) return null;
            sessions.put(cls, result);
        }

        return result;
    }

    /**
     * Returns the session using this object.<p />
     *
     * The implementation of this session depends on the type of the CommandExecutor.<br />
     * If the CommandExecutor is a Player, the CommandExecutor will use the name of the Player to
     * identify the right Session-Map.<p />
     *
     * If the type of the CommandExecutor is <i>not</i> a Player, a separate Session-Map is stored. This
     * Map is shared between all Non-Player-Executors as the type of the executor cannot be resolved
     * from a platform-neutral viewpoint.<p />
     *
     * Sessions are stored by a synchronized map are identified by the Class of the implementing type.
     *
     * @param cls The class object.
     * @param <S> The type of the session.
     * @return null if the creation of the session failed.
     */
    public final <S extends Session> S getSession(Class<S> cls) {
        Map<Class<? extends Session>, Session> sessions;
        if (this.isPlayer()) {
            sessions = SESSIONS.get(this.getHandle());
            if (sessions == null) {
                SESSIONS.put(
                        this.getName(),
                        (sessions = Collections.synchronizedMap(new HashMap<Class<? extends Session>, Session>()))
                );
            }
        } else {
            sessions = CONSOLE_SESSIONS;
        }

        return _getSession(sessions, cls);
    }

    /**
     * Removes expired sessions from a command-executor.
     * @param sessions The sessions of an command-executor.
     */
    public static void _cleanupSessions(Map<Class<? extends Session>, Session> sessions) {
        ArrayList<Class<? extends Session>> expired = new ArrayList<>();
        for (Map.Entry<Class<? extends Session>, Session> session : sessions.entrySet()) {
            if (session.getValue().isSessionExpired()) expired.add(session.getKey());
        }

        for (Class<? extends Session> cls : expired) {
            // Multi-Threading-Case: A new session is created while cleaning up.
            if (!sessions.get(cls).isSessionExpired()) continue;
            sessions.remove(expired);
        }
    }

    /**
     * Cleans-up all sessions.
     */
    public static void cleanupSessions() {
        _cleanupSessions(CONSOLE_SESSIONS);

        ArrayList<String> remove = new ArrayList<>();
        for (Map.Entry<String, Map<Class<? extends Session>, Session>> handles : SESSIONS.entrySet()) {
            _cleanupSessions(handles.getValue());
            if (handles.getValue().size() == 0) remove.add(handles.getKey());
        }

        for (String handle : remove) {
            // Multi-Threading-Case: A new session is created while cleaning up.
            if (SESSIONS.get(handle).size() > 0) continue;
            SESSIONS.remove(handle);
        }
    }

    /**
     * Returns the extension for the player.
     * @param interfaceClass The extension class.
     * @param <R>            The extension class.
     * @return The type.
     */
    public <R> R getExtension(Class<R> interfaceClass) {
        return this.proxy.createInstance(interfaceClass);
    }
}
