package net.stuxcrystal.configuration.storage.contrib.filebased;

import net.stuxcrystal.configuration.parser.ConfigurationHandler;
import net.stuxcrystal.configuration.parser.NodeTreeGenerator;
import net.stuxcrystal.configuration.parser.exceptions.ConfigurationException;
import net.stuxcrystal.configuration.storage.ConfigurationLocation;

import java.io.*;

/**
 * A file configuration location.
 */
public class FileConfigurationLocation implements ConfigurationLocation {

    /**
     * The file that this instance points to.
     */
    private final File file;

    /**
     * Creates a new FileConfigurationLocation.
     * @param file The file this location points to.
     */
    public FileConfigurationLocation(File file) {
        this.file = file;
    }

    @Override
    public boolean canWrite() {
        File current = file;
        while (!current.exists()) {
            current = current.getParentFile();
        }
        return current.canWrite();
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return new FileInputStream(this.file);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        if (!this.canWrite())
            throw new IOException("Cannot write to the file");
        FileBasedStorageBackend.createFile(file);
        return new FileOutputStream(this.file);
    }

    @Override
    public NodeTreeGenerator getNodeTreeGenerator(ConfigurationHandler handler) {
        try {
            return handler.getTreeGenerator(this.file);
        } catch (ConfigurationException e) {
            return null;
        }
    }
}
