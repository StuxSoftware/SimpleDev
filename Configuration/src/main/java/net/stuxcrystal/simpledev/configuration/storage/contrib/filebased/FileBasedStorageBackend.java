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
     * Makes the file canonical.
     *
     * @param file The file to make canonical.
     * @return The new file object.
     */
    private static File toAbsoluteCanonicalFile(File file) {
        file = file.getAbsoluteFile();
        try {
            file = file.getCanonicalFile();
        } catch (IOException ignored) {
        }
        return file;
    }

    /**
     * Joins a file.
     * @param base   The base directory.
     * @param names  The path chunks to join.
     * @return File to the joined path.
     */
    private static File join(File base, String... names) {
        StringBuilder sb = new StringBuilder(FileBasedStorageBackend.toAbsoluteCanonicalFile(base).getPath());
        for (String name : names)
            sb.append(File.separator).append(name);
        return new File(sb.toString());
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

        // Disallow "worlds" as module-name.
        for (String modName : module)
            if ("worlds".equalsIgnoreCase(modName))
                throw new IllegalArgumentException("'worlds' is an illegal name for a module.");

        // Use config as default name.
        if (name == null)
            name = "config";
        // Disallow config as module name.
        else if (name.equalsIgnoreCase("config"))
            throw new IllegalArgumentException("'config' is a reserved configuration name.");

        // Make world specific file names.
        if (world != null)
            directory = FileBasedStorageBackend.join(directory, world);

        // Add default file extension if there is no file extension.
        if (!name.contains(".")) {
            name += this.defaultEnding;
        }

        return new FileConfigurationLocation(new File(directory, name));
    }
}
