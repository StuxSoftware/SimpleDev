package net.stuxcrystal.configuration;

/**
 * Interface to connect to the logging system used.
 */
public interface LoggingInterface {

    /**
     * Warns the user.
     *
     * @param message Message to send.
     */
    public void warn(String message);
}
