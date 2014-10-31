package net.stuxcrystal.simpledev.configuration.parser.generators.yaml;

import net.stuxcrystal.simpledev.configuration.parser.node.ArrayNode;
import net.stuxcrystal.simpledev.configuration.parser.node.DataNode;
import net.stuxcrystal.simpledev.configuration.parser.node.MapNode;
import net.stuxcrystal.simpledev.configuration.parser.node.Node;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.representer.Representer;
import org.yaml.snakeyaml.resolver.Resolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Parses YAML streams.
 */
class YamlParser {

    /**
     * A yaml parser that only supports strings.
     */
    private static class StringOnlyResolver extends Resolver {

        /**
         * Don't implement any resolvers.
         */
        protected void addImplicitResolvers() {

        }

    }

    /**
     * The yaml parser to use.
     */
    private static Yaml parser;

    /**
     * Prepare the parser.
     */
    static {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        parser = new Yaml(new Constructor(), new Representer(), options, new StringOnlyResolver());
    }

    /**
     * Parses the node.
     * @param stream The node to parse.
     * @return If an I/O-Operation or parsing fails.
     */
    public static Node<?> parse(InputStream stream) throws IOException {
        try {
            return parseNode(parser.load(stream));
        } catch (YAMLException e) {
            throw new IOException(e);
        }
    }

    /**
     * Parses the node.
     * @param value The value.
     * @return The node.
     */
    @SuppressWarnings("unchecked")
    private static Node<?> parseNode(Object value) {
        Node<?> n;
        if (value instanceof Map) {
            n = parseMap((Map<String, Object>) value);
        } else if (value instanceof List) {
            n = parseList((List<Object>) value);
        } else {
            n = new DataNode((String) value);
        }
        return n;
    }

    /**
     * Parses the map.
     * @param data The data to parse.
     * @return The map-node.
     */
    private static Node<?> parseMap(Map<String, Object> data) {
        MapNode node = new MapNode();
        ArrayList<Node<?>> nodes = new ArrayList<>();
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Node<?> n = parseNode(entry.getValue());
            n.setName(entry.getKey());
            n.setParent(node);
            nodes.add(n);
        }

        node.setData(nodes.toArray(new Node<?>[nodes.size()]));
        return node;
    }

    /**
     * Parses a list.
     * @param data The list to parse.
     * @return The node.
     */
    private static Node<?> parseList(List<Object> data) {
        MapNode node = new ArrayNode();
        ArrayList<Node<?>> nodes = new ArrayList<>(data.size());
        for (Object o : data) {
            Node<?> n = parseNode(o);
            n.setParent(node);
            nodes.add(n);
        }
        node.setData(nodes.toArray(new Node<?>[nodes.size()]));
        return node;
    }

}
