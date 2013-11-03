package net.stuxcrystal.commandhandler.arguments.types;

import net.stuxcrystal.commandhandler.CommandBackend;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.arguments.ArgumentType;

/**
 * Represents a type for strings.
 */
public class StringType extends ArgumentType<String> {

    /**
     * The type of the argument.
     */
    public StringType() {
        super(String.class);
    }

    @Override
    public String convert(String value, CommandExecutor executor, CommandBackend backend) {
        return value;
    }
}
