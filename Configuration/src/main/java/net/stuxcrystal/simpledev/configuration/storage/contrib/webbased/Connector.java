package net.stuxcrystal.simpledev.configuration.storage.contrib.webbased;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Class that will actually connect to the web.
 */
public interface Connector {

    /**
     * Opens a stream for reading
     *
     * @param location The location of the stream.
     * @return The stream that can be used to write data.
     * @throws java.io.IOException If an I/O-Operation fails.
     */
    public InputStream openRead(WebConfigurationLocation location) throws IOException;

    /**
     * Opens a stream for updating files.
     *
     * @param location  The location of the stream.
     * @return The stream that can be used to write data.
     * @throws IOException If an I/O-Operation fails.
     */
    public OutputStream openWrite(WebConfigurationLocation location) throws IOException;

}
