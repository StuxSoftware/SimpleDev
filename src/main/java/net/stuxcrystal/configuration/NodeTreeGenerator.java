package net.stuxcrystal.configuration;

import net.stuxcrystal.configuration.node.Node;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Dumps
 *
 * @author StuxCrystal
 */
public interface NodeTreeGenerator {

    /**
     * Returns the name of the node-tree generator.
     *
     * @return The name of the generator.
     */
    public String getName();

    /**
     * Is the file a file that can be parsed using this node-tree-generator.<p />
     * <p/>
     * This method has to be executed quietly.
     *
     * @param file The file to be parsed.
     * @return true if the file can be parsed using this node-tree-generator.
     * @throws java.io.IOException if an IO-Operation failed.
     */
    public boolean isValidFile(File file) throws IOException;

    /**
     * Parses a file.
     *
     * @param file The file to be parsed.
     * @return a node-tree.
     * @throws java.io.IOException if an IO-Operation failed.
     */
    public Node<?> parseFile(File file, ConfigurationLoader parser) throws IOException;

    /**
     * Parses a stream.
     *
     * @param stream The stream to parse.
     * @return a node-tree.
     * @throws IOException if an IO-Operation fails.
     */
    public Node<?> parse(InputStream stream, ConfigurationLoader parser) throws IOException;

    /**
     * Dumps the node-tree into a file.
     *
     * @param file The file to be dumped.
     * @param node The node.
     * @throws java.io.IOException if an IO-Operation failed.
     */
    public void dumpFile(File file, Node<?> node, ConfigurationLoader parser) throws IOException;

    /**
     * Dumps to a stream.
     *
     * @param stream The stream to write to.
     * @param node   The node to store.
     * @throws IOException If an IO-Operation fails.
     */
    public void dump(OutputStream stream, Node<?> node, ConfigurationLoader parser) throws IOException;

}
