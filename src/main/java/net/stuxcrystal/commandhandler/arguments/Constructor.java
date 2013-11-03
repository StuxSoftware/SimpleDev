package net.stuxcrystal.commandhandler.arguments;

import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.arguments.types.CommandExecutorType;
import net.stuxcrystal.commandhandler.arguments.types.NumberType;
import net.stuxcrystal.commandhandler.arguments.types.StringType;

/**
 * Constructor for an ArgumentHandler.
 */
public class Constructor {

    /**
     * Types to add by a subclass.
     * @param handler The handler to add types.
     */
    protected void addedTypes(ArgumentHandler handler) {}

    /**
     * Adds types used by {@link ArgumentParser}.
     * @param handler
     */
    public final void addTypes(ArgumentHandler handler) {
        addedTypes(handler);

        for (Class<?> wrapper : net.stuxcrystal.configuration.types.NumberType.WRAPPER_TYPES) {
            if (handler.supportsType(wrapper)) continue;
            handler.registerArgumentTypes(new NumberType(wrapper));
        }

        if (!handler.supportsType(String.class)) handler.registerArgumentTypes(new StringType());
        if (!handler.supportsType(CommandExecutor.class)) handler.registerArgumentTypes(new CommandExecutorType());
    }

}
