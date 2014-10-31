package net.stuxcrystal.simpledev.commands.commands.contrib.annotations;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.CommandHandler;
import net.stuxcrystal.simpledev.commands.arguments.ArgumentList;
import net.stuxcrystal.simpledev.commands.commands.CommandContainer;
import net.stuxcrystal.simpledev.commands.exceptions.DoNotExecuteException;
import net.stuxcrystal.simpledev.commands.exceptions.ExceptionHandler;
import net.stuxcrystal.simpledev.commands.translations.TranslationManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

/**
 * Class with subcommands.
 */
public abstract class AnnotationBasedCommand implements CommandContainer {

    /**
     * Contains the command that should be executed.
     */
    protected final Command command;

    /**
     * Contains the method that should be executed.
     */
    protected final Method method;

    /**
     * The instance of the object.
     */
    protected final Object instance;

    /**
     * Creates a new annotation based command.
     * @param command  The command metadata.
     * @param method   The method to execute.
     * @param instance The instance of the command metadata.
     */
    public AnnotationBasedCommand(Command command, Method method, Object instance) {
        this.command = command;
        this.method = method;
        this.instance = instance;
    }

    @Override
    public String getName() {
        if (!command.value().isEmpty())
            return command.value();
        return method.getName();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(command.aliases());
    }

    @Override
    public String getPermission() {
        return command.permission();
    }

    @Override
    public String getDescription() {
        return command.description();
    }

    @Override
    public boolean allowPlayers() {
        return command.asPlayer();
    }

    @Override
    public boolean allowConsole() {
        return command.asConsole();
    }

    @Override
    public boolean isOperatorCommand() {
        return command.opOnly();
    }

    @Override
    public boolean isAsyncCommand() {
        return command.async();
    }

    @Override
    public String getSupportedFlags() {
        return command.flags();
    }

    @Override
    public int getMinimalArgumentCount() {
        return command.minSize();
    }

    @Override
    public int getMaximalArgumentCount() {
        return command.maxSize();
    }

    @Override
    public boolean parseArguments() {
        return true;
    }

    /**
     * Not used
     * @param executor  The executor that has executed the command.
     * @param args      The arguments that have been passed.
     */
    @Override
    public void execute(CommandExecutor executor, String[] args) {}

    /**
     * Executes the function behind this command.
     * @param sender     The execution that has executed the command.
     * @param arguments  The arguments the have been passed.
     * @return  {@code false} if a DoNotExecuteException has been thrown
     */
    protected boolean _execute(CommandExecutor sender, ArgumentList arguments) {
        CommandHandler handler = sender.getCommandHandler();
        TranslationManager manager = handler.getTranslationManager();

        try {
            this.method.invoke(this.instance, sender, arguments);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            sender.sendMessage(manager.translate(sender, "cmd.call.fail"));
            sender.getBackend().getLogger().log(Level.WARNING, "Failed to execute command.", e);
        } catch (InvocationTargetException e) {
            if (e.getCause() instanceof DoNotExecuteException) {
                return false;
            }

            ExceptionHandler exc = handler.getExceptionHandler(e.getCause().getClass());
            if (exc == null) {
                // If the exception couldn't be handled, throw this generic exception.
                sender.sendMessage(manager.translate(sender, "cmd.exception"));
                sender.getBackend().getLogger().log(Level.SEVERE, "Exception while calling command.", e.getCause());
            } else {
                // Handle the exception.
                exc.exception(e.getCause(), this.command.value(), sender, arguments);
            }
        }

        return true;
    }
}
