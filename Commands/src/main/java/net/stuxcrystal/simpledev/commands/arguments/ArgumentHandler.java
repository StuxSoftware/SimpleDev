package net.stuxcrystal.simpledev.commands.arguments;

import net.stuxcrystal.simpledev.commands.CommandBackend;
import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.arguments.splitter.NoSplit;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles arguments.
 */
public class ArgumentHandler {

    /**
     * The registered argument-types.
     */
    private List<ArgumentType> argumentTypes = new ArrayList<>();

    /**
     * The argument-splitter for this handler.
     */
    private ArgumentSplitter splitter = new NoSplit();

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
    public void registerArgumentTypes(ArgumentType... types) {
        for (ArgumentType type : types) {
            argumentTypes.add(type);
        }
    }

    /**
     * Checks if the type is supported.
     * @param cls The type of the class.
     * @return true.
     */
    public boolean supportsType(Class<?> cls) {
        for (ArgumentType type : argumentTypes) {
            if (type.isTypeSupported(cls)) return true;
        }
        return false;
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
        for (ArgumentType aType : argumentTypes) {
            if (aType.isTypeSupported(type))
                return (T) aType.convert(value, type, executor, handler);
        }

        throw new IllegalArgumentException("Unsupported type");
    }

    /**
     * @return The ArgumentSplitter for this handler.
     */
    public ArgumentSplitter getArgumentSplitter() {
        return this.splitter;
    }

    /**
     * Sets the ArgumentSplitter.
     * @param splitter The new ArgumentSplitter.
     */
    public void setArgumentSplitter(ArgumentSplitter splitter) {
        this.splitter = splitter;
    }

}
