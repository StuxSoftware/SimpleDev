package net.stuxcrystal.commandhandler.arguments;

import net.stuxcrystal.commandhandler.CommandBackend;
import net.stuxcrystal.commandhandler.CommandExecutor;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles arguments.
 */
public class ArgumentHandler {

    /**
     * The registered argument-types.
     */
    private Map<Class<?>, ArgumentType<?>> argumentTypes = new HashMap<>();

    /**
     * Adds the types with the default values.
     */
    public ArgumentHandler() {
        this(new Constructor());
    }

    /**
     * Adds the types using the given default constructor.
     * @param constructor The constructor.
     */
    public ArgumentHandler(Constructor constructor) {
        constructor.addTypes(this);
    }

    /**
     * Registers the argument-types for the argument-parser.
     * @param types The new argument-parser.
     */
    public void registerArgumentTypes(ArgumentType<?>... types) {
        for (ArgumentType<?> type : types) {
            argumentTypes.put(type.getType(), type);
        }
    }

    /**
     * Checks if the type is supported.
     * @param cls The type of the class.
     * @return true.
     */
    public boolean supportsType(Class<?> cls) {
        return this.argumentTypes.containsKey(cls);
    }

    /**
     * Converts the string to the given type.
     * @param value     The value to convert.
     * @param type      The type to convert.
     * @param executor  The executor that is used to convert the value.
     * @param handler   The handler that is used to convert a value.
     * @param <T> The type of the value.
     * @return The converted type.
     * @throws IllegalArgumentException If the given type is unsupported.
     */
    public <T> T convertType(String value, Class<T> type, CommandExecutor executor, CommandBackend handler) {
        ArgumentType<T> converter = (ArgumentType<T>) argumentTypes.get(type);
        if (converter == null)
            throw new IllegalArgumentException("Unsupported conversion type.");

        return converter.convert(value, executor, handler);
    }

}
