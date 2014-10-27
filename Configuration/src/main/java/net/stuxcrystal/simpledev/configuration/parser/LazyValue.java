package net.stuxcrystal.simpledev.configuration.parser;

import net.stuxcrystal.simpledev.configuration.parser.exceptions.ConfigurationException;
import net.stuxcrystal.simpledev.configuration.parser.exceptions.ReflectionException;
import net.stuxcrystal.simpledev.configuration.parser.exceptions.ValueException;
import net.stuxcrystal.simpledev.configuration.parser.node.Node;
import net.stuxcrystal.simpledev.configuration.parser.utils.ReflectionUtil;

import java.lang.reflect.Type;

/**
 * Represents a lazy node.
 */
public class LazyValue {

    /**
     * Parser that parses lazy nodes.
     */
    private static class LazyValueParser implements ValueType<LazyValue> {

        @Override
        public boolean isValidType(Object object, Type cls) throws ReflectiveOperationException {
            return LazyValue.class.isAssignableFrom(ReflectionUtil.toClass(cls));
        }

        @Override
        public LazyValue parse(Object object, ConfigurationParser parser, Type cls, Node<?> value) throws ReflectiveOperationException, ValueException {
            // Since the value is lazy, we will just store the current node in the lazy node
            // with the given configuration parser.
            LazyValue result = new LazyValue();

            // We don't need synchronizing as we are sure that we are the only thread that can access the
            // node.
            result.node = value;
            result.parser = parser;
            return result;
        }

        @Override
        public Node<?> dump(Object object, ConfigurationParser parser, Type cls, LazyValue data) throws ReflectiveOperationException, ValueException {
            try {
                return data.getNode(ReflectionUtil.toClass(cls));
            } catch (ConfigurationException e) {
                throw new ValueException("Failed to dump values", e);
            }
        }
    }

    /**
     * <p>Parser for lazy value object.</p>
     * <p>
     *     Since the parser is actually stateless, we can store a reference to the parser locally.
     * </p>
     */
    private static final LazyValueParser PARSER = new LazyValueParser();

    /**
     * A parser for the configuration.
     */
    private ConfigurationParser parser;

    /**
     * The node used.
     */
    private Node<?> node;

    /**
     * The cached object.
     */
    private Object cache;

    /**
     * The object to execute locking operations.
     */
    private final Object lock = new Object();

    /**
     * Public constructor that sets the contents of the value.
     * @param o The object that this lazy node represents.
     */
    public LazyValue(Object o) {
        this();
        this.set(o);
    }

    /**
     * Constructor that automatically allows to convert an object into another.
     *
     * @param o        The object to use.
     * @param handler  The handler to use.
     */
    public LazyValue(Object o, ConfigurationHandler handler) {
        this(o);
        this.parser = new ConfigurationParser(handler);
    }

    /**
     * Internal constructor. The last used parser will be updated when the object will be parsed.
     */
    private LazyValue() {
        this.parser = null;
        this.node = null;
        this.cache = null;
    }

    /**
     * Returns the value.
     * @param type   The type of the value.
     * @param <R>    The return type.
     * @return The parsed value.
     * @throws ConfigurationException If parsing failed.
     */
    @SuppressWarnings("unchecked")
    public <R> R get(Class<R> type) throws ConfigurationException {
        synchronized (this.lock) {
            // If this is the currently cached value,
            // we will return this object.
            if (type.isInstance(this.cache))
                return (R)this.cache;
        }

        // Get the node.
        Node<?> node = this.getNode(type);

        // If we couldn't retrieve a node, return null.
        if (node == null)
            return null;

        // Since other threads could change the value of the node.
        ConfigurationParser parser;
        synchronized (this.lock) {
            parser = this.parser;
        }

        // Parse the object.
        try {
            return (R)parser.parseObject(this, type, node);
        } catch (ReflectiveOperationException e) {
            throw new ReflectionException("Failed to parse object", e);
        }
    }

    /**
     * Returns the node to parse.
     * @return The node to parse.
     * @throws ConfigurationException If the parsing fails.
     */
    private Node<?> getNode(Class<?> type) throws ConfigurationException {
        ConfigurationParser parser;
        Object cache;

        synchronized (this.lock) {
            // Check if there is already a node that has been parased.
            if (this.node != null)
                return node;

            // Check if we can create a new node.
            if (this.parser == null || this.cache == null)
                return null;

            parser = this.parser;
            cache = this.cache;
        }

        // Dump the data.
        Node<?> node;
        try {
            node = parser.dumpObject(this, type, cache);
        } catch (ReflectiveOperationException e) {
            throw new ReflectionException("Failed to dump cached object for reparsing.", e);
        }

        // Save the node into the cache.
        synchronized (this.lock) {
            this.node = node;
        }

        // Return the node.
        return node;
    }

    /**
     * Sets the content of the lazy value.
     * @param o The new value.
     */
    @SuppressWarnings("unchecked")
    public void set(Object o) {
        synchronized (this.lock) {
            // Invalidate node so we force the cached object to be reevaluated.
            this.node = null;
            this.cache = o;
        }
    }

    /**
     * Returns the parser for LazyValue-Objects in this library.
     * @return The parser for lazy value objects.
     */
    public static ValueType<LazyValue> getValueType() {
        return LazyValue.PARSER;
    }

}
