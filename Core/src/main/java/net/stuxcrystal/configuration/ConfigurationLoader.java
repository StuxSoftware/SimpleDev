package net.stuxcrystal.configuration;

import net.stuxcrystal.configuration.parser.BaseConstructor;
import net.stuxcrystal.configuration.parser.ConfigurationHandler;
import net.stuxcrystal.configuration.parser.Constructor;

/**
 * Actually loads configuration
 */
public class ConfigurationLoader {

    /**
     * The configuration handler of the loader.
     */
    private ConfigurationHandler handler;

    /**
     * The constructor of the configuration loader.
     */
    private Constructor constructor;

    /**
     * Creates a new Configuration Loader with the given Constructor
     * for creating new configuration handlers.
     * @param constructor The constructor for configuration handlers.
     */
    public ConfigurationLoader(Constructor constructor) {
        this.constructor = constructor;
        this.handler = new ConfigurationHandler(constructor);
    }

    /**
     * Creates a new configuration loader with the default constructor.
     */
    public ConfigurationLoader() {
        this(new BaseConstructor());
    }

    /**
     * Returns the configuration behind the configuration loader.
     * @return The configuration loader.
     */
    public ConfigurationHandler getConfigurationHandler() {
        return this.handler;
    }

    /**
     * Sets the logger for the logging interface.
     * @param logger The logger for the configuration subsystem.
     */
    public void setLoggingInterface(LoggingInterface logger) {
        this.handler.setLoggingInterface(logger);
    }

    /**
     * Sets the logging interface for the configuration subsystem.
     *
     * @return A logging-interface object.
     */
    public LoggingInterface getLoggingInterface() {
        return this.handler.getLoggingInterface();
    }
}
