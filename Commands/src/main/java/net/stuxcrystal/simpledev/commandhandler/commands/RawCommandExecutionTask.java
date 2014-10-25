package net.stuxcrystal.simpledev.commandhandler.commands;

import net.stuxcrystal.simpledev.commandhandler.CommandExecutor;

/**
 * Raw task for execution.
 */
public class RawCommandExecutionTask extends CommandExecutionTask {

    /**
     * The arguments of the command.
     */
    private final String[] args;

    /**
     * The task to execute.
     * @param container The command to execute.
     * @param executor  The executor to use.
     * @param args      The arguments of the command.
     */
    protected RawCommandExecutionTask(CommandContainer container, CommandExecutor executor, String[] args) {
        super(container, executor);
        this.args = args;
    }

    @Override
    public void run() {
        this.container.execute(this.executor, this.args);
    }
}
