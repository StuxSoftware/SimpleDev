package net.stuxcrystal.simpledev.configuration.compat;

import net.stuxcrystal.simpledev.configuration.LoggingInterface;
import org.apache.logging.log4j.Logger;

/**
 * Binding to log4j.
 */
public class Log4jBinding implements LoggingInterface {

    private Logger logger;

    public Log4jBinding(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void warn(String message) {
        this.logger.warn(message);
    }

    @Override
    public void debug(String message) {
        this.logger.debug(message);
    }

    @Override
    public void debugException(Throwable exception) {
        this.logger.debug("Exception occured (probably handled internally):", exception);
    }

    @Override
    public void exception(Throwable exception) {
        this.logger.warn("Exception occured", exception);
    }
}
