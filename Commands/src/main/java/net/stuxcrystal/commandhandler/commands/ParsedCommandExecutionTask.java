package net.stuxcrystal.commandhandler.commands;

import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.arguments.ArgumentParser;

/**
 * Task for parsed commands.
 */
public class ParsedCommandExecutionTask extends CommandExecutionTask {

    private final ArgumentParser parser;

    /**
     * The task to execute.
     * @param container The command to execute.
     * @param executor  The executor to use.
     * @param parser    The parsed arguments.
     */
    protected ParsedCommandExecutionTask(CommandContainer container, CommandExecutor executor, ArgumentParser parser) {
        super(container, executor);
        this.parser = parser;
    }

    /**
     * Executes the command.
     */
    @Override
    public void run() {
        this.container.execute(this.executor, this.parser);
    }
}
