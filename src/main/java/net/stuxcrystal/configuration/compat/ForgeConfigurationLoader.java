package net.stuxcrystal.configuration.compat;

import cpw.mods.fml.common.FMLLog;
import net.stuxcrystal.configuration.ConfigurationLoader;
import net.stuxcrystal.configuration.Constructor;
import net.stuxcrystal.configuration.logging.JULBinding;

import java.util.logging.Logger;

/**
 * Configuration-Loader support for forge.
 */
public class ForgeConfigurationLoader extends ConfigurationLoader {

    /**
     * Constructs the configuration loader with the default types
     * and the FMLLog.getLogger()-Logger.
     */
    public ForgeConfigurationLoader() {
        this(FMLLog.getLogger());
    }

    /**
     * Constructs the configuration loader with the default types
     * and the given logger.
     * @param logger The logger to use.
     */
    public ForgeConfigurationLoader(Logger logger) {
        super();
        this.setLoggingInterface(new JULBinding(logger));
    }

    /**
     * Constructs the configuration loader with the given constructor
     * and the FMLLog.getLogger()-Logger.
     * @param constructor The constructor to use.
     */
    public ForgeConfigurationLoader(Constructor constructor) {
        this(constructor, FMLLog.getLogger());
    }

    /**
     * Binding for the forge configuration system.<p />
     *
     * Uses the given constructor and the given logger.
     *
     * @param constructor The constructor.
     * @param logger      The logger to use.
     */
    public ForgeConfigurationLoader(Constructor constructor, Logger logger) {
        super(constructor);
        this.setLoggingInterface(new JULBinding(logger));
    }

}
