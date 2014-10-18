package net.stuxcrystal.configuration.storage;

import net.stuxcrystal.configuration.parser.ConfigurationHandler;
import net.stuxcrystal.configuration.parser.NodeTreeGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Location of a configuration file.
 */
public interface ConfigurationLocation {

    /**
     * Can you write something to this location.
     * @return {@code true} if you can write to this location.
     */
    public boolean canWrite();

    /**
     * Reads the input stream for the given configuration location.
     * @return An {@link java.io.InputStream} to read the data from the configuration.
     * @throws java.io.IOException If an I/O-Operation fails.
     */
    public InputStream getInputStream() throws IOException;

    /**
     * Creates an OutputStream to write data.
     * @return An {@link java.io.OutputStream} to write data into the configuration.
     * @throws java.io.IOException                       If an I/O-Operation fails.
     * @throws java.lang.UnsupportedOperationException   If you cannot write to this location.
     */
    public OutputStream getOutputStream() throws IOException;

    /**
     * Returns the NodeTreeGenerator that can parse the configuration file.
     * @param handler The handler.
     * @return The node tree generator to use.
     */
    public NodeTreeGenerator getNodeTreeGenerator(ConfigurationHandler handler);

}
