package net.stuxcrystal.configuration.storage;

/**
 * <p>Defines a backend for the default storage.</p>
 */
public interface StorageBackend {

    /**
     * <p>
     *     Returns the actual configuration file for the given submodule(1), world and the
     *     actual name of the configuration file.
     * </p>
     *
     * <p>
     *     Since modules can have submodules that can have submodules themselves and so on.
     *     Hence the array defines the path of submodules to get to the configuration directory.
     *     <i>Please note that "worlds" is a reserved module name.</i>
     * </p>
     *
     * <p>Plase note that the submodule may not contain any dots.</p>
     *
     * <p>
     *     The world name can be {@code null} so that global configuration files can be defined.<br />
     *     If the name of the configuration file contains a file ending, it will not add the default
     *     file ending of the storage backend.
     * </p>
     *
     * <p>
     *     "config" is a reserved configuration name.
     * </p>
     *
     * @param module    The path to the modules.
     * @param world     The world for the configuration. {@code null} denotes a global configuration.
     * @param name      The actual name of the configuration file.
     * @return The path to the configuration file. (May not exist yet)
     */
    public ConfigurationLocation getConfiguration(String[] module, String world, String name);

}
