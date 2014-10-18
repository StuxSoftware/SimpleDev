package net.stuxcrystal.commandhandler.arguments.types;

import net.stuxcrystal.commandhandler.CommandBackend;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.arguments.ArgumentType;
import net.stuxcrystal.commandhandler.utils.ReflectionUtils;

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
