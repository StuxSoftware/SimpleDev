package net.stuxcrystal.simpledev.commands.arguments.types;

import net.stuxcrystal.simpledev.commands.CommandBackend;
import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.arguments.ArgumentType;

/**
 * Parsing the string and makes it to a CommandExecutor.
 */
public class CommandExecutorType implements ArgumentType {

    @Override
    public boolean isTypeSupported(Class<?> cls) {
        return CommandExecutor.class.isAssignableFrom(cls);
    }

    @Override
    public Object convert(String value, Class<?> toClass, CommandExecutor executor, CommandBackend backend) {
        return backend.getExecutor(value);
    }
}
