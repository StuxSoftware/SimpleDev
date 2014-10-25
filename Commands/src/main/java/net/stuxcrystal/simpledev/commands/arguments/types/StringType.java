package net.stuxcrystal.simpledev.commands.arguments.types;

import net.stuxcrystal.simpledev.commands.CommandBackend;
import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.arguments.ArgumentType;

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
