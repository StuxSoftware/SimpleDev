package net.stuxcrystal.configuration.logging;

import net.stuxcrystal.configuration.LoggingInterface;

/**
 * Silently drops all messages.
 */
public class NullBinding implements LoggingInterface {

    /**
     * Drop the message.
     *
     * @param message Message to send.
     */
    @Override
    public void warn(String message) {
    }
}
