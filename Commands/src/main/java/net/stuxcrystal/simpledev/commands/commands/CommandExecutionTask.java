package net.stuxcrystal.simpledev.commands.commands;

import net.stuxcrystal.simpledev.commands.CommandExecutor;

/**
 * Simple task that executes a command.
 */
public abstract class CommandExecutionTask implements Runnable {

    /**
     * The command executor that is being called.
     */
    protected final CommandContainer container;

    /**
     * The executor that executes the task.
     */
    protected final CommandExecutor executor;

    /**
     * The task to execute.
     * @param container The command to execute.
     * @param executor  The executor to use.
     */
    protected CommandExecutionTask(CommandContainer container, CommandExecutor executor) {
        this.container = container;
        this.executor = executor;
    }


}
