package net.stuxcrystal.simpledev.commandhandler.contrib.sessions;

import net.stuxcrystal.simpledev.commandhandler.CommandExecutor;
import net.stuxcrystal.simpledev.commandhandler.CommandHandler;
import net.stuxcrystal.simpledev.commandhandler.component.Component;
import net.stuxcrystal.simpledev.commandhandler.component.ComponentContainer;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * The manager for sessions.
 */
public class SessionManager implements ComponentContainer {

    /**
     * Map of all recognized sessions handled by the command-executor.<p />
     *
     * The sessions are stored in a static variable because multiple CommandExecutors can access the same
     * handle.<p />
     *
     * The access to the sessions is thread-safe.
     */
    private final Map<String, Map<Class<? extends Session>, Session>> sessions = Collections.synchronizedMap(
            new HashMap<String, Map<Class<? extends Session>, Session>>()
    );

    /**
     * Sessions for a console-object.<p />
     *
     * Designed to handle only one type of Non-Player-Sessions.<p />
     *
     * The access to the sessions is thread-safe.
     */
    private final Map<Class<? extends Session>, Session> consoleSessions = Collections.synchronizedMap(
            new HashMap<Class<? extends Session>, Session>()
    );

    /**
     * Reference to the command-handler in the backend.
     */
    private final CommandHandler handler;

    /**
     * Creates a new session-manager.
     * @param handler The
     */
    public SessionManager(CommandHandler handler) {
        this.handler = handler;
    }

    /**
     * Creates a new session.
     *
     * @param executor The executor that the sessions are attached.
     * @param cls      The type of ths session
     * @return The newly created session.
     */
    protected <S extends Session> S createSession(CommandExecutor executor, Class<S> cls) {

        S session;
        try {
            session = cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            // Log Exception.
            this.handler.getServerBackend().getLogger().log(Level.WARNING, "Failed to create session of " + cls, e);
            return null;
        }

        session.initialize(executor);
        return session;

    }

    /**
     * Returns the session with the given type.
     * @param executor The executor that the sessions are attached.
     * @param sessions The session-container of this command-executor.
     * @param cls      The class that contains the session-container.
     * @param <S>      The type of the session object.
     * @return The session-object or null if the creation of the session failed.
     */
    @SuppressWarnings("unchecked")
    private <S extends Session> S _getSession(CommandExecutor executor, Map<Class<? extends Session>, Session> sessions, Class<S> cls) {
        S result = (S) sessions.get(cls);
        if (result == null || result.isSessionExpired()) {
            result = this.createSession(executor, cls);
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
     * @param executor The executor that the sessions are attached.
     * @param cls The class object.
     * @param <S> The type of the session.
     * @return null if the creation of the session failed.
     */
    @Component
    public <S extends Session> S getSession(CommandExecutor executor, Class<S> cls) {
        Map<Class<? extends Session>, Session> sessions;
        if (executor.isPlayer()) {
            // Fixed bug with name resolving.
            sessions = this.sessions.get(executor.getName());
            if (sessions == null) {
                this.sessions.put(
                        executor.getName(),
                        (sessions = Collections.synchronizedMap(new HashMap<Class<? extends Session>, Session>()))
                );
            }
        } else {
            sessions =  this.consoleSessions;
        }

        return _getSession(executor, sessions, cls);
    }

    /**
     * Returns an array of all sessions registered for the executor.
     * @param executor The sessions registered for the executor.
     * @return The sessions registered for the executor.
     */
    @Component
    public Session[] getSessions(CommandExecutor executor) {
        Map<Class<? extends Session>, Session> sessions;
        if (executor.isPlayer()) {
            if (!this.sessions.containsKey(executor.getName()))
                return new Session[0];
            sessions = this.sessions.get(executor.getName());
        } else {
            sessions = this.consoleSessions;
        }

        Collection<Session> sessionCollection = sessions.values();
        return sessionCollection.toArray(new Session[sessions.size()]);
    }

}
