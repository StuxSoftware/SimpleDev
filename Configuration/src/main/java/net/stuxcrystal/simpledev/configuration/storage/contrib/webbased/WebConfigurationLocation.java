package net.stuxcrystal.simpledev.configuration.storage.contrib.webbased;

import net.stuxcrystal.simpledev.configuration.parser.ConfigurationHandler;
import net.stuxcrystal.simpledev.configuration.parser.NodeTreeGenerator;
import net.stuxcrystal.simpledev.configuration.parser.exceptions.ConfigurationException;
import net.stuxcrystal.simpledev.configuration.storage.ConfigurationLocation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;

/**
 * Represents a file which configuration location is on the web.
 */
public class WebConfigurationLocation implements ConfigurationLocation {

    /**
     * {@code true} if we can write to the location.
     */
    private final boolean canWrite;

    /**
     * The location of the file.
     */
    private final URI location;

    /**
     * The connector for the configuration location.
     */
    private final Connector connector;

    /**
     * Creates a new configuration location object that points to a file on the web.
     * @param canWrite  Can we write to the location?
     * @param location  The location.
     * @param connector The object that will be used to establish connections.
     */
    public WebConfigurationLocation(boolean canWrite, URI location, Connector connector) {
        this.canWrite = canWrite;
        this.location = location;
        this.connector = connector;
    }

    /**
     * Creates a new configuration location object that points to a file on the web.
     *
     * @param location  The location.
     * @param connector The object that will be used to establish connections.
     */
    public WebConfigurationLocation(URI location, Connector connector) {
        this(false, location, connector);
    }

    @Override
    public boolean canWrite() {
        return this.canWrite;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return this.connector.openRead(this);
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        if (!this.canWrite)
            throw new IOException("Cannot write to the destination.");
        return this.connector.openWrite(this);
    }

    @Override
    public NodeTreeGenerator getNodeTreeGenerator(ConfigurationHandler handler) {
        String[] dirs = this.location.getPath().split("/");
        try {
            return handler.getTreeGenerator(dirs[dirs.length-1]);
        } catch (ConfigurationException e) {
            handler.getLoggingInterface().debugException(e);
            return null;
        }
    }

    /**
     * Returns the uri to the file.
     * @return The uri to the file.
     */
    public URI getLocation() {
        return this.location;
    }
}
