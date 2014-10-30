package net.stuxcrystal.simpledev.configuration.storage.contrib.webbased;

import net.stuxcrystal.simpledev.configuration.storage.ConfigurationLocation;
import net.stuxcrystal.simpledev.configuration.storage.StorageBackend;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;

/**
 * Backend for the storage.
 */
public class WebConfigurationBackend implements StorageBackend, Connector {

    /**
     * Redirects all configuration backend requests to the specified connector.
     */
    private class WebConfigurationConnector implements Connector {

        @Override
        public InputStream openRead(WebConfigurationLocation location) throws IOException {
            return WebConfigurationBackend.this.connector.openRead(location);
        }

        @Override
        public OutputStream openWrite(WebConfigurationLocation location) throws IOException {
            return WebConfigurationBackend.this.connector.openWrite(location);
        }
    }

    /**
     * The proxy to connect to the internet.
     */
    private Proxy proxy = null;

    /**
     * The base uri for the configuration.
     */
    private final URI base;

    /**
     * Can you write to this location?
     */
    private final boolean canWrite;

    /**
     * The default connector
     */
    private Connector connector = this;

    /**
     * The connector passed to the location object.
     */
    private final Connector locationConnector = new WebConfigurationConnector();

    /**
     * The default extension for configuration files.
     */
    private final String defaultExtension;

    /**
     * Creates a new backend.
     * @param base              The base uri that in which the file names will be appended.
     * @param canWrite          {@code true} if updating files is allowed.
     * @param defaultExtension  The default extension for configuration files.
     */
    public WebConfigurationBackend(URI base, boolean canWrite, String defaultExtension) {
        this.base = base;
        this.canWrite = canWrite;
        this.defaultExtension = defaultExtension;
    }

    /**
     * Creates a new backend.
     * @param base              The base uri that in which the file names will be appended.
     * @param canWrite          {@code true} if updating files is allowed.
     */
    public WebConfigurationBackend(URI base, boolean canWrite) {
        this(base, canWrite, "xml");
    }

    /**
     * Sets the proxy of the configuration backend.
     * @param proxy The proxy.
     */
    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    /**
     * Returns the proxy of the backend.
     * @return The
     */
    public Proxy getProxy() {
        return this.proxy;
    }

    /**
     * Returns the connector of the backend.
     * @return The connector of the backend.
     */
    public Connector getConnector() {
        return this.connector;
    }

    /**
     * Sets the connector of the configuration backend.
     * @param connector The connector of the configuration backend.
     */
    public void setConnector(Connector connector) {
        this.connector = connector;
    }

    /**
     * The parameters to use.
     * @param base   The base file.
     * @param pathes The pathes.
     * @return The joined url.
     */
    private static URI join(URI base, String... pathes) throws URISyntaxException {
        StringBuilder sb = new StringBuilder(base.toString());
        for (String path : pathes)
            sb.append("/").append(path);
        return new URI(sb.toString());
    }

    @Override
    public ConfigurationLocation getConfiguration(String[] module, String world, String name) {
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
        if (!name.contains("."))
            name += "."+this.defaultExtension;

        // Create the base URI.
        URI configURI;
        try {
            configURI = WebConfigurationBackend.join(this.base, module);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Module-Names resolved to invalid url.", e);
        }

        // Add 'worlds' to the path if our format is world-based.
        if (world != null) {
            try {
                configURI = WebConfigurationBackend.join(configURI, world);
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException("World-Name resolved to invalid url.", e);
            }
        }

        // Join the configuration name to the uri.
        try {
            configURI = WebConfigurationBackend.join(configURI, name);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("File-Name resolved to unvalid url.", e);
        }

        try {
            configURI.toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Cannot convert uri to url.", e);
        }

        // Create the new configuration location
        return new WebConfigurationLocation(this.canWrite, configURI, this.locationConnector);
    }

    /**
     * Creates an url-connection object.
     * @param location The location of the URL.
     * @return The newly created connection.
     * @throws IOException If an I/O-Operation fails.
     */
    private URLConnection createConnection(WebConfigurationLocation location) throws IOException {
        URL url;
        try {
            url = location.getLocation().toURL();
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("Failed to convert URI to URL.");
        }

        URLConnection connection;
        if (this.proxy == null)
            connection = url.openConnection();
        else
            connection = url.openConnection(this.proxy);
        return connection;
    }

    @Override
    public InputStream openRead(WebConfigurationLocation location) throws IOException {
        URLConnection connection = this.createConnection(location);
        connection.setDoInput(true);
        return connection.getInputStream();
    }

    @Override
    public OutputStream openWrite(WebConfigurationLocation location) throws IOException {
        URLConnection connection = this.createConnection(location);
        connection.setDoOutput(true);
        return connection.getOutputStream();
    }
}
