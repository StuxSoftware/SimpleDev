package net.stuxcrystal.simpledev.commands.commands.contrib.annotations;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.CommandHandler;
import net.stuxcrystal.simpledev.commands.arguments.ArgumentParser;
import net.stuxcrystal.simpledev.commands.translations.TranslationManager;

import java.lang.reflect.Method;

/**
 * A command with subcommands.
 */
public class BranchAnnotationCommand extends AnnotationBasedCommand {

    /**
     * The subcommand metadata
     */
    private final SubCommand subcommand;

    /**
     * The command-handler that contains the sub-command.
     */
    private final CommandHandler cHandler;

    /**
     * Creates a new annotation based command.
     * @param command    The command metadata.
     * @param method     The method to execute.
     * @param instance   The instance of the command metadata.
     * @param subcommand The subcommand metadata.
     * @param cHandler   The command handler.
     */
    public BranchAnnotationCommand(Command command, Method method, Object instance, SubCommand subcommand, CommandHandler cHandler) {
        super(command, method, instance);
        this.subcommand = subcommand;
        this.cHandler = cHandler;
    }

    /**
     * The commands are always async when they contain sub-commands.
     * @return The sub-commands.
     */
    @Override
    public boolean isAsyncCommand() {
        return false;
    }

    @Override
    public void execute(CommandExecutor executor, ArgumentParser parser) {
        CommandHandler handler = executor.getCommandHandler();
        TranslationManager manager = handler.getTranslationManager();

        if (this.subcommand.time() == CallTime.PRE) {
            if (!_execute(executor, parser)) {
                executor.sendMessage(manager.translate(executor, "cmd.notfound"));
                return;
            }
        }

        // Parse the command list.
        SubCommand command = this.subcommand;
        String name;
        String[] args;

        // Parse the command.
        if (parser.count() == 0) {
            // An empty name represents a call without an argument
            name = CommandHandler.FALLBACK_COMMAND_NAME;
            args = new String[0];
        } else if (parser.count() == 1) {
            name = parser.getString(0);
            args = new String[0];
        } else {
            name = parser.getString(0);
            args = parser.getArguments(1);
        }

        // Executes the subcommand.
        if (!this.cHandler.execute(executor, name, args)) {
            boolean notFound = true;
            if (command.time() == CallTime.FALLBACK) {
                notFound = !_execute(executor, parser);
            }

            if (notFound)
                executor.sendMessage(manager.translate(executor, "cmd.notfound"));
        }

        // Call the arguments.
        if (command.time() == CallTime.POST)
            _execute(executor, parser);
    }
}
