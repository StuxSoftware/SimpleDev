package net.stuxcrystal.commandhandler.arguments.types;

import net.stuxcrystal.commandhandler.CommandBackend;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.arguments.ArgumentType;

/**
 * Represents a type for strings.
 */
public class StringType implements ArgumentType {

    @Override
    public boolean isTypeSupported(Class<?> cls) {
        return String.class.isAssignableFrom(cls);
    }

    @Override
    public String convert(String value, Class<?> toClass, CommandExecutor executor, CommandBackend backend) {
        return value;
    }
}
