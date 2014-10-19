package net.stuxcrystal.commandhandler.commands.contrib.builder;

import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.arguments.ArgumentParser;
import net.stuxcrystal.commandhandler.commands.CommandContainer;

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
