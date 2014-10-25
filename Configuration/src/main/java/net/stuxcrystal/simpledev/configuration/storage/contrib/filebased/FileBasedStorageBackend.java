package net.stuxcrystal.simpledev.configuration.storage.contrib.filebased;

import net.stuxcrystal.simpledev.configuration.storage.ConfigurationLocation;
import net.stuxcrystal.simpledev.configuration.storage.StorageBackend;

import java.io.File;
import java.io.IOException;

/**
 * Defines a directory-based storage backend.
 */
public class FileBasedStorageBackend implements StorageBackend {

    /**
     * Joins a file.
     * @param base   The base directory.
     * @param names  The path chunks to join.
     * @return File to the joined path.
     */
    private static File join(File base, String... names) {
        File current = base;
        for (String name : names)
            current = new File(current, name);
        return current;
    }

    /**
     * Creates a new file.
     * @param file The file to create.
     * @throws IOException If an IO-Operation fails.
     */
    public static void createFile(File file) throws IOException {
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            file.createNewFile();
        }
    }

    /**
     * If the configuration ending
     */
    private final String defaultEnding;

    /**
     * The base directory.
     */
    private final File baseDirectory;

    /**
     * Creates a new base directory.
     * @param defaultEnding The default file ending.
     * @param baseDirectory The base directory.
     */
    public FileBasedStorageBackend(String defaultEnding, File baseDirectory) {
        this.defaultEnding = defaultEnding;
        this.baseDirectory = baseDirectory;
    }

    @Override
    public ConfigurationLocation getConfiguration(String[] module, String world, String name) {
        File directory = FileBasedStorageBackend.join(this.baseDirectory, module);

        if (name == null)
            name = "config";
        else if (name.equalsIgnoreCase("config"))
            throw new IllegalArgumentException("'config' is a reserved configuration name.");

        if (world != null)
            directory = FileBasedStorageBackend.join(directory, world);

        if (!name.contains(".")) {
            name += this.defaultEnding;
        }

        return new FileConfigurationLocation(new File(directory, name));
    }
}
