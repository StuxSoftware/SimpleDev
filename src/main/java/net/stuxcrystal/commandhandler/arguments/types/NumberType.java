package net.stuxcrystal.commandhandler.arguments.types;

import net.stuxcrystal.commandhandler.CommandBackend;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.arguments.ArgumentType;
import net.stuxcrystal.configuration.exceptions.ValueException;

import java.util.logging.Level;

/**
 * Converts a number.
 */
public class NumberType<T> extends ArgumentType<T> {

    /**
     * The type of the argument.
     *
     * @param type The type the string will be converted to.
     */
    public NumberType(Class<T> type) {
        super(type);
    }

    /**
     * Converts a value.
     * @param value      The argument passed as a string.
     * @param executor   The executor who passed the argument.
     * @param backend    The backend that executed the command.
     * @return The returned value.
     */
    @Override
    public T convert(String value, CommandExecutor executor, CommandBackend backend) {
        try {
            return net.stuxcrystal.configuration.types.NumberType.parseValue(this.getType(), value);
        } catch (ValueException e) {
            backend.getLogger().log(Level.WARNING, "Failed to convert value", e);
            return null;
        }
    }
}
