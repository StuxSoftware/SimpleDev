package net.stuxcrystal.configuration;

import net.stuxcrystal.configuration.exceptions.ValueException;
import net.stuxcrystal.configuration.node.Node;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Parses a configuration.
 *
 * @author StuxCrystal
 */
public class ConfigurationParser {

    /**
     * The ConfigurationLoader.
     */
    private final ConfigurationLoader loader;

    public ConfigurationParser(ConfigurationLoader loader) {
        this.loader = loader;
    }

    /**
     * Parses an object.
     *
     * @param object The object that are being parsed.
     * @param field  The field of the object that is being parsed.
     * @param cls    The type of the object.
     * @param node   The node to parse.
     * @return The parsed object.
     */
    public Object parseObject(Object object, Field field, Type cls, Node<?> node) throws ReflectiveOperationException, ValueException {
        if (node == null)
            throw new IllegalStateException("The given node is null: " + field.getName());

        for (ValueType<?> type : this.loader.types) {
            if (type.isValidType(object, field, cls)) {
                return type.parse(object, field, this, cls, node);
            }
        }

        throw new ValueException("The parser does not know how to parse the object...");
    }

    /**
     * Dumps an object.
     *
     * @param object The object the parser is currently parsing.
     * @param field  The field of the object that is parsed.
     * @param cls    The class that is to be called.
     * @param o      The object that has to be dumped.
     * @return a node.
     * @throws ReflectiveOperationException If a reflective Operation fails.
     * @throws net.stuxcrystal.configuration.exceptions.ValueException
     *                                      Incorrect usage.
     */
    public Node<?> dumpObject(Object object, Field field, Type cls, Object o) throws ReflectiveOperationException, ValueException {

        for (ValueType<?> type : this.loader.types) {
            if (type.isValidType(object, field, cls)) {
                return type.dump(object, field, this, cls, o);
            }
        }
        throw new ValueException("The parser does not know how to parse the object...");
    }

    private void warn(String where, String message) {
        this.loader.warn(where, message);
    }
}
