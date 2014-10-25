package net.stuxcrystal.simpledev.commands.commands.contrib.builder;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.arguments.ArgumentParser;
import net.stuxcrystal.simpledev.commands.commands.CommandContainer;

/**
 * Listener for commands executions.
 */
public interface CommandListener {

    /**
     * Executes the command.
     *
     * @param command    The command that has been executd.
     * @param executor   The executor that executed the command.
     * @param arguments  The arguments that have been passed to this command.
     */
    public void execute(CommandContainer command, CommandExecutor executor, ArgumentParser arguments);

}
