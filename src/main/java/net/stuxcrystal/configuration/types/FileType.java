package net.stuxcrystal.configuration.types;

import net.stuxcrystal.configuration.ConfigurationParser;
import net.stuxcrystal.configuration.ValueType;
import net.stuxcrystal.configuration.exceptions.ValueException;
import net.stuxcrystal.configuration.node.DataNode;
import net.stuxcrystal.configuration.node.Node;
import net.stuxcrystal.configuration.utils.ReflectionUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class FileType implements ValueType<File> {

    @Override
    public boolean isValidType(Object object, Field field, Type cls) throws ReflectiveOperationException {
        return File.class.isAssignableFrom(ReflectionUtil.toClass(cls));
    }

    @Override
    @SuppressWarnings("unchecked")
    public File parse(Object object, Field field, ConfigurationParser parser, Type type, Node<?> value) throws ReflectiveOperationException, ValueException {
        if (!value.hasChildren())
            throw new ValueException("The node has no values.");
        return new File(((Node<String>) value).getData());
    }

    @Override
    public Node<?> dump(Object object, Field field, ConfigurationParser parser, Type type, Object data) throws ReflectiveOperationException, ValueException {
        return new DataNode(((File) data).getPath());
    }


}
