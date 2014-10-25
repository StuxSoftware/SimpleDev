package net.stuxcrystal.simpledev.commands.commands;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.arguments.ArgumentParser;

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
