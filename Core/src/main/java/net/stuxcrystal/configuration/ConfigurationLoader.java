package net.stuxcrystal.configuration;

import net.stuxcrystal.configuration.parser.BaseConstructor;
import net.stuxcrystal.configuration.parser.Constructor;
import net.stuxcrystal.configuration.storage.ModuleConfigurationLoader;
import net.stuxcrystal.configuration.storage.StorageBackend;

/**
 * Defines the root-module.
 */
public class ConfigurationLoader extends ModuleConfigurationLoader {

    /**
     * Creates a new Configuration Loader with the given Constructor
     * for creating new configuration handlers.
     * @param constructor The constructor for configuration handlers.
     */
    public ConfigurationLoader(StorageBackend backend, Constructor constructor) {
        super(null, backend);
        this.createConfigurationHandler(constructor);
    }

    /**
     * Creates a new configuration loader with the default constructor.
     */
    public ConfigurationLoader(StorageBackend backend) {
        this(backend, null);
    }

    /**
     * Sets the logger for the logging interface.
     * @param logger The logger for the configuration subsystem.
     */
    public void setLoggingInterface(LoggingInterface logger) {
        this.getConfigurationHandler().setLoggingInterface(logger);
    }

    /**
     * Sets the logging interface for the configuration subsystem.
     *
     * @return A logging-interface object.
     */
    public LoggingInterface getLoggingInterface() {
        return this.getConfigurationHandler().getLoggingInterface();
    }
}
