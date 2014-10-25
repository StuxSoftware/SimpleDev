package net.stuxcrystal.simpledev.commands.arguments.types;

import net.stuxcrystal.simpledev.commands.CommandBackend;
import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.arguments.ArgumentType;
import net.stuxcrystal.simpledev.commands.utils.ReflectionUtils;

/**
 * Implementation for enumerations.
 */
public class EnumType implements ArgumentType {

    @Override
    public boolean isTypeSupported(Class<?> cls) {
        return ReflectionUtils.toClass(cls).isEnum();
    }

    @Override
    public Object convert(String value, Class<?> toClass, CommandExecutor executor, CommandBackend backend) {
        return Enum.valueOf((Class<Enum>) toClass, value);
    }
}
