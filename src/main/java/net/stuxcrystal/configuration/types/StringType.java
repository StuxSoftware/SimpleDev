package net.stuxcrystal.configuration.types;

import net.stuxcrystal.configuration.ConfigurationParser;
import net.stuxcrystal.configuration.ValueType;
import net.stuxcrystal.configuration.exceptions.ValueException;
import net.stuxcrystal.configuration.node.DataNode;
import net.stuxcrystal.configuration.node.Node;
import net.stuxcrystal.configuration.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Parses {@link String}s.<p />
 * {@link CharSequence}s are not allowed.
 *
 * @author StuxCrystal
 */
public class StringType implements ValueType<String> {

    @Override
    public boolean isValidType(Object object, Field field, Type cls) throws ReflectiveOperationException {
        return String.class.isAssignableFrom(ReflectionUtil.toClass(cls));
    }

    /**
     * Makes the object a value.
     */
    @Override
    @SuppressWarnings("unchecked")
    public String parse(Object object, Field field, ConfigurationParser parser, Type type, Node<?> value) throws ReflectiveOperationException, ValueException {
        return ((Node<String>) value).getData();
    }

    /**
     * The data does not need to be converted.
     */
    @Override
    public Node<?> dump(Object object, Field field, ConfigurationParser parser, Type type, Object data) throws ReflectiveOperationException, ValueException {
        return new DataNode(data.toString());
    }

}
