package net.stuxcrystal.simpledev.commandhandler.arguments.types;

import net.stuxcrystal.simpledev.commandhandler.CommandBackend;
import net.stuxcrystal.simpledev.commandhandler.CommandExecutor;
import net.stuxcrystal.simpledev.commandhandler.arguments.ArgumentType;

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
