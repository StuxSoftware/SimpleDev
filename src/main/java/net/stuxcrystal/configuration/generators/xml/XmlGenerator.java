package net.stuxcrystal.configuration.generators.xml;

import net.stuxcrystal.configuration.ConfigurationLoader;
import net.stuxcrystal.configuration.NodeTreeGenerator;
import net.stuxcrystal.configuration.node.Node;

import java.io.*;

/**
 * Node tree generator for the XML File-Type.
 */
public class XmlGenerator implements NodeTreeGenerator {

    @Override
    public String getName() {
        return "XML";
    }

    @Override
    public boolean isValidFile(File file) throws IOException {
        return file.getName().endsWith(".xml");
    }

    @Override
    public Node<?> parseFile(File file, ConfigurationLoader parser) throws IOException {
        return XMLParser.parse(new FileInputStream(file));
    }

    @Override
    public Node<?> parse(InputStream stream, ConfigurationLoader parser) throws IOException {
        return XMLParser.parse(stream);
    }

    @Override
    public void dumpFile(File file, Node<?> node, ConfigurationLoader parser) throws IOException {
        CommentAwareDumper.dump(new FileOutputStream(file), node);
    }

    @Override
    public void dump(OutputStream stream, Node<?> node, ConfigurationLoader parser) throws IOException {
        CommentAwareDumper.dump(stream, node);
    }
}
