package net.stuxcrystal.simpledev.commandhandler.arguments.types;

import net.stuxcrystal.simpledev.commandhandler.CommandBackend;
import net.stuxcrystal.simpledev.commandhandler.CommandExecutor;
import net.stuxcrystal.simpledev.commandhandler.arguments.ArgumentType;

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
        if (value.equalsIgnoreCase("CONSOLE")) return backend.getConsole();
        return backend.getExecutor(value);
    }
}
