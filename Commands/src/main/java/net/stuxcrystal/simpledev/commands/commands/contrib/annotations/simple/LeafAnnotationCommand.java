package net.stuxcrystal.simpledev.commands.commands.contrib.annotations.simple;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.arguments.ArgumentList;
import net.stuxcrystal.simpledev.commands.commands.contrib.annotations.AnnotationBasedCommand;
import net.stuxcrystal.simpledev.commands.commands.contrib.annotations.Command;
import net.stuxcrystal.simpledev.commands.commands.contrib.annotations.MethodInvoker;

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
     * @param invoker    The way how the method will be invoked.
     */
    public LeafAnnotationCommand(Command command, Method method, Object instance, MethodInvoker invoker) {
        super(command, method, instance, invoker);
    }

    /**
     * For the LeafAnnotationCommand
     * @param sender    The executor that has executed the command.
     * @param parser    The parser that parsed the commands.
     */
    @Override
    public void execute(CommandExecutor sender, ArgumentList parser) {
        this._execute(sender, parser);
    }
}
