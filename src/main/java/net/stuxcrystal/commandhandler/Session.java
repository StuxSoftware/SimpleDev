package net.stuxcrystal.commandhandler;

/**
 * Represents a session.
 */
public abstract class Session {

    /**
     * The executor that belongs to the session.
     */
    private CommandExecutor executor;

    /**
     * The last time of access.
     */
    private long lastAccessTime = 0;

    /**
     * The time for a session to expire.<br />
     * If the expire-time is 0, the session will never expire.
     */
    private long expireTime = 30*60*1000;             // Half an hour.

    /**
     * Constructs a session object.
     */
    public Session() { }

    /**
     * Initializes the Session-Object.
     * @param executor The current session-object.
     */
    final void initialize(CommandExecutor executor) {
        this.executor = executor;
        updateAccessTime();
    }

    /**
     * Updates the last-access-time.
     */
    public final void updateAccessTime() {
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
        if (isSessionExpired()) throw new IllegalArgumentException("The session is expired.");
        this.expireTime = time;
    }

    /**
     * Checks if the session is expired.
     * @return true if the session is expired.
     */
    public final boolean isSessionExpired() {
        if (this.expireTime == 0) return false;
        return System.currentTimeMillis() - this.lastAccessTime > this.expireTime;
    }

}
