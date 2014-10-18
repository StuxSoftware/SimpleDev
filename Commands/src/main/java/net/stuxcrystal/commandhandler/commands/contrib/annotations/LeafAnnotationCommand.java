package net.stuxcrystal.commandhandler.commands.contrib.annotations;

import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.arguments.ArgumentParser;

import java.lang.reflect.Method;

/**
 * A simple command without sub-commands.
 */
public class LeafAnnotationCommand extends AnnotationBasedCommand {

    /**
     * Creates a new annotation based command.
     *
     * @param command  The command metadata.
     * @param method   The method to execute.
     * @param instance The instance of the command metadata.
     */
    public LeafAnnotationCommand(Command command, Method method, Object instance) {
        super(command, method, instance);
    }

    /**
     * For the LeafAnnotationCommand
     * @param sender    The executor that has executed the command.
     * @param parser    The parser that parsed the commands.
     */
    @Override
    public void execute(CommandExecutor sender, ArgumentParser parser) {
        this._execute(sender, parser);
    }
}
