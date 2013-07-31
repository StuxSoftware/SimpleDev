package net.stuxcrystal.configuration.logging;

import net.stuxcrystal.configuration.LoggingInterface;

import java.util.logging.Logger;

/**
 * Binding to java.util.logging. Use this for Bukkit.
 */
public class JULBinding implements LoggingInterface {

    private final Logger logger;

    public JULBinding(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void warn(String message) {
        this.logger.warning(message);
    }
}
