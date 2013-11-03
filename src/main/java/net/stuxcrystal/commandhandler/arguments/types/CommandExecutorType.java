package net.stuxcrystal.commandhandler.arguments.types;

import net.stuxcrystal.commandhandler.CommandBackend;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.arguments.ArgumentType;

/**
 * Parsing the string and makes it to a CommandExecutor.
 */
public class CommandExecutorType extends ArgumentType<CommandExecutor> {

    /**
     * The type of the argument.
     */
    public CommandExecutorType() {
        super(CommandExecutor.class);
    }

    @Override
    public CommandExecutor convert(String value, CommandExecutor executor, CommandBackend backend) {
        return backend.getExecutor(value);
    }
}
