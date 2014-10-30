package net.stuxcrystal.simpledev.configuration.storage;

import net.stuxcrystal.simpledev.configuration.parser.BaseConstructor;
import net.stuxcrystal.simpledev.configuration.parser.ConfigurationHandler;
import net.stuxcrystal.simpledev.configuration.parser.Constructor;
import net.stuxcrystal.simpledev.configuration.parser.NodeTreeGenerator;
import net.stuxcrystal.simpledev.configuration.parser.exceptions.ConfigurationException;
import net.stuxcrystal.simpledev.configuration.parser.exceptions.FileException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The configuration of a module.
 */
public class ModuleConfigurationLoader {

    /**
     * The parent configuration loader.
     */
    private final ModuleConfigurationLoader parent;

    /**
     * The name of the module. (Root-Module has null.)
     */
    private final String name;

    /**
     * The backend for the configuration storage.
     */
    private StorageBackend backend = null;

    /**
     * The configuration handler of the loader.
     */
    private ConfigurationHandler handler;

    /**
     * Creates a new module configuration
     * @param name   The name of the module.
     * @param parent The parent module.
     */
    private ModuleConfigurationLoader(String name, ModuleConfigurationLoader parent) {
        this.name = name;
        this.parent = parent;
    }

    /**
     * Creates a new module configuration loader with the given storage backend.
     * @param name   The name of the module.
     * @param backend The backend for the storage.
     */
    protected ModuleConfigurationLoader(String name, StorageBackend backend) {
        this(name, (ModuleConfigurationLoader)null);
        this.backend = backend;
    }

    /**
     * Creates a new Configuration-Handler.
     * @param constructor The constructor to create the configuration handler.
     */
    protected void createConfigurationHandler(Constructor constructor) {
        if (constructor == null)
            constructor = new BaseConstructor();
        this.handler = new ConfigurationHandler(constructor);
    }

    /**
     * Returns the storage backend for the module.
     * @return The storage backend.
     */
    public StorageBackend getStorageBackend() {
        ModuleConfigurationLoader current = this;
        while (current.backend == null) {
            if (current.parent == null)
                return null;

            current = current.parent;
        }
        return current.backend;
    }

    /**
     * Returns the storage backend for the ModuleConfiguration.
     * @param backend The backend to use. (Affects submodules if they don't provide their own configuration storage)
     */
    public void setStorageBackend(StorageBackend backend) {
        this.backend = backend;
    }

    /**
     * Returns the name of the module.
     * @return The name of the moule.
     */
    public String getModuleName() {
        return this.name;
    }

    /**
     * Returns the module path (Non-Recursive)
     * @return The path to the module.
     */
    public String[] getModulePath() {
        return this.getModulePath(false);
    }

    /**
     * Returns the module path.
     * @param recursive Should the path only determined until the first storage backend has been found?
     * @return The module path. Empty array on root modules.
     */
    public String[] getModulePath(boolean recursive) {
        List<String> path = new ArrayList<>();

        // Get path.
        ModuleConfigurationLoader current = this;
        while (current.getModuleName() != null || (!recursive && current.backend == null)) {
            path.add(current.getModuleName());
            current = current.parent;
        }

        // Reverse it so it is in the right order.
        Collections.reverse(path);

        return path.toArray(new String[path.size()]);
    }

    /**
     * Returns a submodule for the module configuration loader.<p />
     *
     * '{@code worlds}' is a reserved module name and may not be used.
     *
     * @param name The name of the submodule.
     * @return The configuration loader for the submodule.
     */
    public ModuleConfigurationLoader getSubModule(String name) {
        if (name.equalsIgnoreCase("worlds"))
            throw new IllegalArgumentException("Reserved module name 'worlds'");

        return new ModuleConfigurationLoader(name, this);
    }

    /**
     * Returns the configuration behind the configuration loader.
     * @return The configuration loader.
     */
    public ConfigurationHandler getConfigurationHandler() {
        ModuleConfigurationLoader current = this;
        while (current.handler == null) {
            if (current.parent == null)
                return null;

            current = current.parent;
        }
        return current.handler;
    }

    /**
     * Returns the location of the given file.
     * @param name   The name of the configuration file.
     * @param world  The world that this configuration applies. (May be {@code null} to denote a global configuration).
     * @return The location of the file.
     */
    private ConfigurationLocation getConfiguration(String name, String world) {
        return this.getStorageBackend().getConfiguration(
                this.getModulePath(),
                world,
                name
        );
    }

    /**
     * Loads the configuration file with the given name of the module.
     *
     * @param name   The name of the configuration file. (May be {@code null} on Main configurations).
     * @param world  The world that this configuration applies. (May be {@code null} to denote a global configuration).
     * @param type   The type of the configuration.
     * @param <T>    The return type.
     * @return The actual configuration.
     * @throws IOException              If an I/O-Operation fails.
     * @throws ConfigurationException   If the configuration couldn't be loaded.
     */
    public <T> T getConfiguration(String name, String world, Class<T> type) throws IOException, ConfigurationException {
        ConfigurationLocation location = this.getConfiguration(name, world);
        ConfigurationHandler handler = this.getConfigurationHandler();
        NodeTreeGenerator generator = location.getNodeTreeGenerator(handler);
        if (generator == null)
            throw new IOException("Unsupported file type.");
        return this.getConfigurationHandler().parseStream(location.getInputStream(), generator, type);
    }

    /**
     * Reads the global configuration with the given name.
     *
     * @param name The name of the configuration-file.
     * @param type The type of the configuration-file.
     * @param <T>  The return type.
     * @return The parsed configuration.
     * @throws IOException              If an IO-Operation fails.
     * @throws ConfigurationException   If the configuration couldn't be parsed.
     */
    public <T> T getGlobalConfiguration(String name, Class<T> type) throws IOException, ConfigurationException {
        return this.getConfiguration(name, null, type);
    }

    /**
     * Reads the main configuration of the module for the world.
     * @param world   The world where the main configuration is valid.
     * @param type    The type of the configuration.
     * @param <T>     The type of the configuration.
     * @return The parsed configuration.
     * @throws IOException              If an IO-Operation fails.
     * @throws ConfigurationException   If the configuration couldn't be parsed.
     */
    public <T> T getMainConfiguration(String world, Class<T> type) throws IOException, ConfigurationException {
        return this.getConfiguration(null, world, type);
    }

    /**
     * Reads the main configuration of the module for the world.
     * @param type    The type of the configuration.
     * @param <T>     The type of the configuration.
     * @return The parsed configuration.
     * @throws IOException              If an IO-Operation fails.
     * @throws ConfigurationException   If the configuration couldn't be parsed.
     */
    public <T> T getMainConfiguration(Class<T> type) throws IOException, ConfigurationException {
        return this.getMainConfiguration(null, type);
    }

    /**
     * Returns input stream for the file with the given name.
     *
     * @param name    The name of the file.
     * @param world   The world The world that this configuration applies. (May be {@code null} to denote a global configuration).
     * @return              An InputStream with the actual configuration.
     * @throws IOException  If an IO-Operation fails.
     */
    public InputStream getInputStream(String name, String world) throws IOException {
        ConfigurationLocation location = this.getConfiguration(name, world);
        return location.getInputStream();
    }

    /**
     * Returns a stream to the global configuration file.
     *
     * @param name  The name of the configuration file.
     * @return The stream to the file.
     * @throws IOException If an IO-Operation fails.
     */
    public InputStream getInputStream(String name) throws IOException {
        return this.getInputStream(name, null);
    }

    /**
     * Returns the input stream for main configuration of the world.
     *
     * @param world             The world where this configuration applies.
     * @return The stream to the file.
     * @throws IOException If an IO-Operation fails.
     */
    public InputStream getMainConfigurationInputStream(String world) throws IOException {
        return this.getInputStream(null, world);
    }

    /**
     * Returns the input stream for main configuration of the world.

     * @return The stream to the file.
     * @throws IOException If an IO-Operation fails.
     */
    public InputStream getMainConfigurationInputStream() throws IOException {
        return this.getMainConfigurationInputStream(null);
    }

    /**
     * Can you write to the file.
     * @param name   The name of the configuration file. (May be {@code null} if you want to use the main configuration).
     * @param world  The world that this configuration applies. (May be {@code null} to denote a global configuration).
     * @return {@code true} true if you can write to the file.
     */
    public boolean canWrite(String name, String world) {
        return this.getConfiguration(name, world).canWrite();
    }

    /**
     * Writes the configuration to the given file.
     * @param name            The name of the configuration file.
     * @param world           The world that this configuration applies. (May be {@code null} to denote a global configuration).
     * @param configuration   The actual configuration.
     * @throws IOException              If an IO-Operation fails.
     * @throws ConfigurationException   If the dump of the configuration failed.
     */
    public void writeConfiguration(String name, String world, Object configuration) throws IOException, ConfigurationException {
        ConfigurationLocation location = this.getConfiguration(name, world);
        ConfigurationHandler handler = this.getConfigurationHandler();
        NodeTreeGenerator generator = location.getNodeTreeGenerator(handler);
        if (generator == null)
            throw new IOException("Unsupported file type.");
        handler.dumpStream(location.getOutputStream(), generator, configuration);
    }

    /**
     * Writes the global configuration to the given file.
     * @param name            The name of the configuration file.
     * @param configuration   The actual configuration.
     * @throws IOException              If an IO-Operation fails.
     * @throws ConfigurationException   If the dump of the configuration failed.
     */
    public void writeGlobalConfiguration(String name, Object configuration) throws IOException, ConfigurationException {
        this.writeConfiguration(name, null, configuration);
    }

    /**
     * Returns the output stream for the given configuration file in the given world.
     *
     * @param name   The name of the configuration file.
     * @param world  The world where this configuration applies.
     * @return The OutputStream to write data into.
     * @throws IOException If an IO-Operation fails.
     */
    public OutputStream getOutputStream(String name, String world) throws IOException {
        return this.getConfiguration(name, world).getOutputStream();
    }

    /**
     * Returns the output stream for the given configuration file in the given world.
     *
     * @param name   The name of the configuration file.
     * @return The OutputStream to write data into.
     * @throws IOException If an IO-Operation fails.
     */
    public OutputStream getOutputStream(String name) throws IOException {
        return this.getOutputStream(name, null);
    }

    /**
     * Returns the output stream of the main configuration file of the world.
     *
     * @param world  The name of the world.
     * @return The OutputStream to write data into.
     * @throws IOException If an IO-Operation fails.
     */
    public OutputStream getMainConfigurationOutputStream(String world) throws IOException {
        return this.getOutputStream(null, world);
    }

    /**
     * Returns the output stream of the main configuration
     * @return The OutputStream to write data into.
     * @throws IOException If an IO-Operation fails.
     */
    public OutputStream getMainConfigurationOutputStream() throws IOException {
        return this.getOutputStream(null);
    }

    /**
     * Loads the configuration file and writes its contents back to the backend.
     *
     * @param name   The name of the configuration file. (May be {@code null} if you want to use the main configuration).
     * @param world  The world that this configuration applies. (May be {@code null} to denote a global configuration).
     * @param type   The type of the configuration file.
     * @param <T>    The type of the configuration file.
     * @return The configuration
     */
    public <T> T loadAndUpdate(String name, String world, Class<T> type) throws ConfigurationException {
        try {
            T result = this.getConfiguration(world, name, type);
            if (this.canWrite(name, world)) {
                this.writeConfiguration(name, world, result);
            }
            return result;
        } catch (IOException e) {
            throw new FileException("Failed to parse file.", e);
        }

    }
}
