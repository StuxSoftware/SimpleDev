package net.stuxcrystal.configuration.types;

import net.stuxcrystal.configuration.ConfigurationParser;
import net.stuxcrystal.configuration.ValueType;
import net.stuxcrystal.configuration.exceptions.ValueException;
import net.stuxcrystal.configuration.node.DataNode;
import net.stuxcrystal.configuration.node.Node;
import net.stuxcrystal.configuration.utils.ReflectionUtil;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

/**
 * Parses a byte array.
 */
public class ByteArrayType implements ValueType<byte[]> {

    @Override
    public boolean isValidType(Object object, Field field, Type cls) throws ReflectiveOperationException {
        Class<?> rawCls = ReflectionUtil.toClass(cls);
        return rawCls.isArray() && (rawCls.getComponentType().equals(Byte.class) || rawCls.getComponentType().equals(Byte.TYPE));

    }

    @Override
    public byte[] parse(Object object, Field field, ConfigurationParser parser, Type cls, Node<?> value) throws ReflectiveOperationException, ValueException {
        return Base64Coder.decode(((Node<String>) value).getData());
    }

    @Override
    public Node<?> dump(Object object, Field field, ConfigurationParser parser, Type cls, Object data) throws ReflectiveOperationException, ValueException {
        return new DataNode(new String(Base64Coder.encode((byte[]) data)));
    }
}
