package net.stuxcrystal.commandhandler.arguments;

import net.stuxcrystal.commandhandler.arguments.types.CommandExecutorType;
import net.stuxcrystal.commandhandler.arguments.types.StringType;
import net.stuxcrystal.configuration.types.EnumType;
import net.stuxcrystal.configuration.types.NumberType;

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

        // Handle numbers.
        handler.registerArgumentTypes(new NumberType());
        handler.registerArgumentTypes(new StringType());
        handler.registerArgumentTypes(new EnumType());
        handler.registerArgumentTypes(new CommandExecutorType());
    }

}