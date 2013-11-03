package net.stuxcrystal.commandhandler;

import net.stuxcrystal.commandhandler.annotations.Command;
import net.stuxcrystal.commandhandler.annotations.SubCommand;
import net.stuxcrystal.commandhandler.arguments.ArgumentParser;
import net.stuxcrystal.commandhandler.exceptions.DoNotExecuteException;
import net.stuxcrystal.commandhandler.exceptions.ExceptionHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

/**
 * The internal datastructure.
 *
 * @author StuxCrystal
 */
class CommandData {

    /**
     * The Command-Annotation.
     */
    final Command command;

    /**
     * Subcommand handler.
     */
    private final CommandHandler subcommands;

    /**
     * Current Command handler
     */
    private final CommandHandler current;

    /**
     * The Method.
     */
    private final Method method;

    /**
     * The Instance used with the method.
     */
    private final Object instance;

    /**
     * The JavaPlugin for the logs.
     */
    private final CommandBackend backend;

    public CommandData(Command command, Method method, Object instance, CommandBackend backend, CommandHandler subcommands, CommandHandler current) {
        this.command = command;
        this.method = method;
        this.instance = instance;
        this.backend = backend;
        this.subcommands = subcommands;
        this.current = current;
    }

    /**
     * Executes the command.<p />
     * Calls the subcommand if needed.
     *
     * @param sender
     * @param arguments
     */
    public void execute(CommandExecutor sender, ArgumentParser arguments) {
        if (this.subcommands == null || this.subcommands.getSubCommand().time() == CallTime.PRE) {
            if (!_execute(sender, arguments)) {
                if (this.subcommands != null)
                    sender.sendMessage(subcommands._(sender, "cmd.notfound"));
                return;
            }
        }

        if (this.subcommands == null)
            return;

        // Parse the command list.
        SubCommand command = this.subcommands.getSubCommand();
        String name;
        String[] args;

        // Parse the command.
        if (arguments.count() == 0) {
            // An empty name represents a call without an argument
            name = "";
            args = new String[0];
        } else if (arguments.count() == 1) {
            name = arguments.getString(0);
            args = new String[0];
        } else {
            name = arguments.getString(0);
            args = arguments.getArguments(1);
        }

        // Executes the subcommand.
        if (!this.subcommands.execute(sender, name, args) && command.time() == CallTime.FALLBACK)
            if (!_execute(sender, arguments))
                sender.sendMessage(subcommands._(sender, "cmd.notfound"));

        // Call the arguments.
        if (command.time() == CallTime.POST)
            _execute(sender, arguments);
    }

    /**
     * Invokes the command.
     *
     * @param sender
     * @param arguments
     * @return
     */
    private boolean _execute(CommandExecutor sender, ArgumentParser arguments) {
        try {
            this.method.invoke(this.instance, new Object[]{this.backend, sender, arguments});
        } catch (IllegalAccessException | IllegalArgumentException e) {
            sender.sendMessage(subcommands._(sender, "cmd.call.fail"));
            backend.getLogger().log(Level.WARNING, "Failed to execute command.", e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof DoNotExecuteException) {
                return false; // Don't execute the subcommand.
            }

            ExceptionHandler handler = this.current.getExceptionHandler(e.getCause().getClass());
            if (handler == null) {
                // If the exception couldn't be handled, throw this generic exception.
                sender.sendMessage(this.current._(sender, "cmd.exception"));
                backend.getLogger().log(Level.SEVERE, "Exception while calling command.", e.getCause());
            } else {
                // Handle the exception.
                handler.exception(e.getCause(), this.command.value(), sender, arguments);
            }
        }

        return true;
    }

    /**
     * @return The name of the command.
     */
    public String getName() {
        if (this.command.value().isEmpty())
            return this.method.getName();

        return this.command.value();
    }
}
