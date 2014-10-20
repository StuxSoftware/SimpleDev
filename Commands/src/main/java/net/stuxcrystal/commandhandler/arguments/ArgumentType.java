package net.stuxcrystal.commandhandler.arguments;

import net.stuxcrystal.commandhandler.CommandBackend;
import net.stuxcrystal.commandhandler.CommandExecutor;

/**
 * Represents a type applicable by ArgumentType.
 */
public interface ArgumentType {

    /**
     * Checks if the type is supported by this converter.
     * @param cls The class to convert.
     * @return true if the type is supported.
     */
    public abstract boolean isTypeSupported(Class<?> cls);

    /**
     * Converts a value into the given type.
     * @param value      The argument passed as a string.
     * @param toClass    The class the value should be converted to.
     * @param executor   The executor who passed the argument.
     * @param backend    The backend that executed the command.
     * @return The converted value.
     */
    public abstract Object convert(String value, Class<?> toClass, CommandExecutor executor, CommandBackend backend);

}
