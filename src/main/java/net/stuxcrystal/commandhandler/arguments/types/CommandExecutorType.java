package net.stuxcrystal.commandhandler.arguments.types;

import net.stuxcrystal.commandhandler.CommandBackend;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.arguments.ArgumentType;

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
