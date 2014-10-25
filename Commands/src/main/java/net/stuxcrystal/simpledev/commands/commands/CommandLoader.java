package net.stuxcrystal.simpledev.commands.commands;

import net.stuxcrystal.simpledev.commands.CommandHandler;

import java.util.List;

/**
 * Loader for commands. It is meant to allow multiple ways to allow
 * multiple ways to register commands.
 */
public interface CommandLoader {

    /**
     * Registers all commands that are supported by this command.<p />
     *
     * If this object is not supported, simply return null.
     *
     * @param registrar The command handler that wants to register the commands.
     * @param container All containers of the objects.
     * @return All commands that were found.
     */
    public List<CommandContainer> register(CommandHandler registrar, Object container);

}
