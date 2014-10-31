package net.stuxcrystal.simpledev.commands.contrib.sessions;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.CommandHandler;

/**
 * Represents a session.<p />
 *
 * The session implementation MUST have a constructor that does not have
 * any parameters.
 */
public abstract class Session {

    /**
     * The command-handler this session belongs to.
     */
    private CommandHandler handler;

    /**
     * The name of the user.
     */
    private String name;

    /**
     * The last time of access.
     */
    private long lastAccessTime = -1;

    /**
     * The time for a session to expire (in milliseconds).<br />
     * If the expire-time is 0, the session will never expire.
     */
    private long expireTime = 0;

    /**
     * Was the session forced to be expired?
     */
    private boolean isExpired = false;

    /**
     * Constructs a session object.
     */
    public Session() { }

    /**
     * Initializes the Session-Object.
     * @param executor The current session-object.
     */
    final void initialize(CommandExecutor executor) {
        this.handler = executor.getCommandHandler().getRootCommandHandler();
        this.name = executor.isPlayer()?executor.getName():null;
        updateAccessTime();
    }

    /**
     * Updates the last-access-time.
     */
    public final void updateAccessTime() {
        if (this.isSessionExpired()) throw new IllegalStateException("The session is expired");
        this.lastAccessTime = System.currentTimeMillis();
    }

    /**
     * @return The time of the last access.
     */
    public final long getLastAccessTime() {
        return this.lastAccessTime;
    }

    /**
     * Sets the time for the session to expire.
     * @param time The time to expire or 0 if the session should never expire.
     */
    public final void setExpireTime(long time) {
        if (isSessionExpired()) throw new IllegalStateException("The session is expired.");
        this.expireTime = time;
    }

    /**
     * Checks if the session is expired.
     * @return true if the session is expired.
     */
    public final boolean isSessionExpired() {
        if (this.isExpired) return true;
        if (this.expireTime == 0) return false;
        if (this.lastAccessTime == -1) return false; // Forcefully allow the access time to exist
        boolean expired = System.currentTimeMillis() - this.lastAccessTime > this.expireTime;
        if (expired) this.isExpired = true;
        return expired;
    }

    /**
     * @return Returns the CommandExecutor this session belongs to.
     */
    public CommandExecutor getCommandExecutor() {
        return this.handler.getServerBackend().getExecutorExact(this.name);
    }

    /**
     * Expire the session now.
     */
    public void expire() {
        this.isExpired = true;
    }

}
