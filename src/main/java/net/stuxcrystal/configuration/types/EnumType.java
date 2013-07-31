package net.stuxcrystal.configuration.types;

import net.stuxcrystal.configuration.ConfigurationParser;
import net.stuxcrystal.configuration.ValueType;
import net.stuxcrystal.configuration.exceptions.ValueException;
import net.stuxcrystal.configuration.node.DataNode;
import net.stuxcrystal.configuration.node.Node;
import net.stuxcrystal.configuration.utils.ReflectionUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class EnumType implements ValueType<Enum<?>> {

    @Override
    public boolean isValidType(Object object, Field field, Type cls) throws ReflectiveOperationException {
        return ReflectionUtil.toClass(cls).isEnum();
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public Enum<?> parse(Object object, Field field, ConfigurationParser parser, Type type, Node<?> value) throws ReflectiveOperationException, ValueException {
        Class<?> cls = ReflectionUtil.toClass(type);
        return (Enum<?>) Enum.valueOf((Class<Enum>) cls, ((Node<String>) value).getData());
    }

    @Override
    public Node<?> dump(Object object, Field field, ConfigurationParser parser, Type type, Object data) throws ReflectiveOperationException, ValueException {
        return new DataNode(data.toString());
    }

}
