package net.stuxcrystal.commandhandler.arguments;

import net.stuxcrystal.commandhandler.CommandBackend;
import net.stuxcrystal.commandhandler.CommandExecutor;

/**
 * Represents a type applicable by ArgumentType.
 */
public abstract class ArgumentType<T> {

    /**
     * The type of the argument
     */
    private final Class<T> type;

    /**
     * The type of the argument.
     * @param type The type the string will be converted to.
     */
    public ArgumentType(Class<T> type) {
        this.type = type;
    }

    /**
     * @return The type the argument will be converted to.
     */
    public final Class<T> getType() {
        return this.type;
    }

    /**
     * Converts a value into the given type.
     * @param value      The argument passed as a string.
     * @param executor   The executor who passed the argument.
     * @param backend    The backend that executed the command.
     * @return The converted value.
     */
    public abstract T convert(String value, CommandExecutor executor, CommandBackend backend);

}
