package net.stuxcrystal.simpledev.commands.commands;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.CommandHandler;
import net.stuxcrystal.simpledev.commands.arguments.ArgumentList;
import net.stuxcrystal.simpledev.commands.commands.contrib.annotations.AnnotationCommandLoader;
import net.stuxcrystal.simpledev.commands.commands.contrib.raw.CommandContainerLoader;
import net.stuxcrystal.simpledev.commands.translations.TranslationManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The command manager that contains all commands.
 */
public class CommandManager {

    /**
     * The list of all supported command.
     */
    private List<CommandContainer> commands = new ArrayList<>();

    /**
     * The list of loaders.
     */
    private List<CommandLoader> loaders = new ArrayList<>(Arrays.asList(
            // Implementation for old @Command classes.
            new AnnotationCommandLoader(),

            // Implementation for raw command containers.
            new CommandContainerLoader()
    ));

    /**
     * Executes this specific command.
     * @param command    The command to execute.
     * @param executor   The executor that executes the command
     * @param args       The arguments.
     */
    private void execute(CommandContainer command, CommandExecutor executor, String[] args) {
        if (command.parseArguments())
            this.executeParsed(command, executor, args);
        else
            this.call(new RawCommandExecutionTask(command, executor, args));
    }

    /**
     * Executes the command and parses its arguments.
     * @param command   The command to execute.
     * @param executor  The executor.
     * @param args      The arguments.
     */
    private void executeParsed(CommandContainer command, CommandExecutor executor, String[] args) {
        TranslationManager mgr = executor.getCommandHandler().getTranslationManager();

        // Check argument data.
        ArgumentList parser = new ArgumentList(executor, executor.getCommandHandler(), args);

        // Check if only these flags are in the flag list
        if (!command.getSupportedFlags().isEmpty() &&
                !parser.getFlags().matches("[" + command.getSupportedFlags() + "]*")) {
            executor.sendMessage(mgr.translate(executor, "cmd.check.flag"));
            return;
        }

        if (command.getMinimalArgumentCount() != -1 && parser.size() < command.getMinimalArgumentCount()) {
            executor.sendMessage(mgr.translate(executor, "cmd.check.args.min"));
            return;
        }

        if (command.getMaximalArgumentCount() != -1 && parser.size() < command.getMaximalArgumentCount()) {
            executor.sendMessage(mgr.translate(executor, "cmd.check.args.max"));
            return;
        }

        this.call(new ParsedCommandExecutionTask(command, executor, parser));
    }

    /**
     * <p>Executes the command container.</p>
     * <p>
     *     Makes sure the task is actually run in the correct thread.
     * </p>
     * @param task The task to execute.
     */
    private void call(CommandExecutionTask task) {
        if (task.container.isAsyncCommand()) {
            task.executor.getBackend().scheduleAsync(task);
        } else {
            // Make sure the task is executed synchronously.
            if (!task.executor.getBackend().inMainThread()) {
                task.executor.getBackend().scheduleSync(task);
            } else {
                task.run();
            }
        }
    }

    /**
     * Checks if the executor can execute the specified command.
     * @param command  The container to use.
     * @param executor The executor that executes the command
     * @return {@code true} if the container can execute the command
     */
    private boolean canExecute(CommandContainer command, CommandExecutor executor) {
        // Check sender type.
        if (executor.isPlayer()) {
            if (!command.allowPlayers()) {
                return false;
            }
        } else {
            if (!command.allowConsole()) {
                return false;
            }
        }

        // Check permissions.
        if (executor.getCommandHandler().isPermissionsSupported(executor)) {
            if (!command.getPermission().isEmpty() && !executor.hasPermission(command.getPermission())) {
                return false;
            }
        } else {
            // Check op only
            if (command.isOperatorCommand() && !executor.isOp()) {
                return false;
            }
        }

        return true;
    }

    /**
     * Executes the command with the given name.
     *
     * @param executor   The executor that executes the command
     * @param name       The name of the command to execute.
     * @param args       The arguments of the command.
     * @return {@code true} if the command was executed.
     */
    public boolean execute(CommandExecutor executor, String name, String[] args) {
        // Filter out commands that the executor cannot execute!
        List<CommandContainer> allowedCommands = new ArrayList<>(this.commands.size());
        for (CommandContainer command : commands) {
            if (!this.canExecute(command, executor))
                continue;
            allowedCommands.add(command);
        }

        // Prefer Exact Matches first.
        for (CommandContainer command : allowedCommands) {
            if (command.getName().equals(name)) {
                this.execute(command, executor, args);
                return true;
            }
        }

        // Then ignore the case.
        for (CommandContainer command : allowedCommands) {
            if (command.getName().equalsIgnoreCase(name)) {
                this.execute(command, executor, args);
                return true;
            }
        }

        // Exact matches to aliases.
        for (CommandContainer command : allowedCommands) {
            for (String alias : command.getAliases()) {
                if (alias.equals(name)) {
                    this.execute(command, executor, args);
                    return true;
                }
            }
        }

        // Match aliases without caring for the case.
        for (CommandContainer command : allowedCommands) {
            for (String alias : command.getAliases()) {
                if (alias.equalsIgnoreCase(name)) {
                    this.execute(command, executor, args);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Registers all commands the command-loader can find insite the function.
     * @param registrar The handler that registers the commands.
     * @param obj       The object that contains the commands.
     */
    public void registerCommands(CommandHandler registrar, Object obj) {
        for (CommandLoader loader : loaders) {
            List<CommandContainer> containers = loader.register(registrar, obj);
            if (containers == null)
                continue;
            this.commands.addAll(containers);
        }
    }

    /**
     * Registers a loader for commands.
     * @param loader The loader to register.
     */
    public void registerLoader(CommandLoader loader) {
        this.loaders.add(loader);
    }

    /**
     * Removes a loader from the manager.
     * @param loader The loader to unregister.
     */
    public void unregisterLoader(CommandLoader loader) {
        this.loaders.remove(loader);
    }

    public List<CommandContainer> getCommands() {
        return this.commands;
    }
}
