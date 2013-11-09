/*
 * Copyright 2013 StuxCrystal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

package net.stuxcrystal.commandhandler;

import net.stuxcrystal.commandhandler.annotations.Command;
import net.stuxcrystal.commandhandler.annotations.SubCommand;
import net.stuxcrystal.commandhandler.arguments.ArgumentHandler;
import net.stuxcrystal.commandhandler.arguments.ArgumentParser;
import net.stuxcrystal.commandhandler.exceptions.ExceptionHandler;
import net.stuxcrystal.commandhandler.translations.TranslationManager;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The handler for commands.
 *
 * @author StuxCrystal
 */
public class CommandHandler {

    /**
     * List of commands.
     */
    private final List<CommandData> commands = new ArrayList<>();

    /**
     * List of registered sub-command-handlers.
     */
    private final List<CommandHandler> subCommandHandler = new ArrayList<>();

    /**
     * The java backend to register tasks.
     */
    protected final CommandBackend backend;

    /**
     * Data for the subcommand.
     */
    private SubCommand subcommand = null;

    /**
     * Manages translations
     */
    private final TranslationManager manager;

    /**
     * Reference to the parent command handler.
     */
    private CommandHandler parent;

    /**
     * The handler retrieving the permissions.
     */
    private PermissionHandler permissionHandler = null;

    /**
     * Represents tha ArgumentHandler
     */
    private ArgumentHandler argument = null;

    /**
     * Stores all exception handlers.
     */
    private final Map<Class<? extends Throwable>, ExceptionHandler<? extends Throwable>> exceptionHandlers = new HashMap<>();

    /**
     * The Constructor for base-commands.
     *
     * @param backend The backend that handles the command handler.
     */
    public CommandHandler(CommandBackend backend) {
        this(backend, null, new TranslationManager(), null);
    }

    /**
     * Use this constructor to instantiate a new subordinate command handler.<p />
     *
     * The CommandHandler passed to this constructor is <i>not</i> the parent handler.<p />
     *
     * This constructor does not register itself as the subordinate CommandHandler to the passed CommandHandler.
     *
     * @param handler The handler to copy it's attributes from.
     */
    public CommandHandler(CommandHandler handler) {
        this(handler.getServerBackend(), null, handler.getTranslationManager(), null);
        this.setArgumentHandler(handler.getArgumentHandler());

    }

    /**
     * Internal constructor for subcommand-support.
     *
     * @param backend    The backend handling the server side stuff.
     * @param subcommand The subcommand that this handler handles.
     * @param manager    The translation manager.
     * @param parent     The parent command handler.
     */
    private CommandHandler(CommandBackend backend, SubCommand subcommand, TranslationManager manager, CommandHandler parent) {
        this.backend = backend;
        this.subcommand = subcommand;
        this.manager = manager;
        this.parent = parent;

        // Intitialize Backend.
        if (parent == null) {
            this.backend.setCommandHandler(this);
            this.argument = new ArgumentHandler();
        }
    }

    public TranslationManager getTranslationManager() {
        return this.manager;
    }

    /**
     * Translates a value.
     *
     * @param sender The sender that sent the message
     * @param msg    The message itself.
     * @return The translated message.
     */
    protected String _(CommandExecutor sender, String msg) {
        return this.manager.translate(sender, msg);
    }

    /**
     * Registers the commands.<p />
     * Also prepares subcommands.
     *
     * @param container The container for the methods.
     */
    public void registerCommands(Object container) {
        for (Method method : container.getClass().getDeclaredMethods()) {

            // The method has to be accessible.
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }

            // The method have to be annotated by Command.
            if (!method.isAnnotationPresent(Command.class)) continue;

            CommandHandler subhandler = null;
            if (method.isAnnotationPresent(SubCommand.class)) {
                SubCommand command = method.getAnnotation(SubCommand.class);
                subhandler = new CommandHandler(this.backend, command, this.manager, this);

                Class<?>[] classes = command.value();
                Object current;
                for (Class<?> cls : classes) {
                    current = newInstance(cls);
                    if (current != null)
                        subhandler.registerCommands(newInstance(cls));
                }
            }

            // Add the command.
            commands.add(new CommandData(method.getAnnotation(Command.class), method, container, this.backend, subhandler, this));
        }
    }

    /**
     * Registers commands using classes. The constructor must not have any arguments.
     *
     * @param container The class that contains the methods.
     */
    public void registerCommands(Class<?> container) {
        Object o = newInstance(container);
        this.registerCommands(o);
    }

    /**
     * Constructs a new instance.
     *
     * @param cls The class to construct.
     * @return A new object.
     */
    private Object newInstance(Class<?> cls) {
        try {
            return cls.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Executes the command.<p />
     * Additionally searches for a command.
     *
     * @param sender    The sender that executes the command.
     * @param name      The name of the command.
     * @param arguments The arguments.
     * @return false if the command couldn't be found.
     */
    protected boolean execute(CommandExecutor sender, String name, String[] arguments) {
        // Prefer Exact Matches first.
        for (CommandData data : commands) {
            if (data.getName().equals(name)) {
                executeCommand(sender, data, arguments);
                return true;
            }
        }

        // Then ignore the case.
        for (CommandData data : commands) {
            if (data.getName().equalsIgnoreCase(name)) {
                executeCommand(sender, data, arguments);
                return true;
            }
        }

        // Exact matches to aliases.
        for (CommandData data : commands) {
            for (String alias : data.command.aliases()) {
                if (alias.equals(name)) {
                    executeCommand(sender, data, arguments);
                    return true;
                }
            }
        }

        // Match aliases without caring for the case.
        for (CommandData data : commands) {
            for (String alias : data.command.aliases()) {
                if (alias.equalsIgnoreCase(name)) {
                    executeCommand(sender, data, arguments);
                    return true;
                }
            }
        }

        // Try subordinate CommandHandlers.
        for (CommandHandler subhandler : this.subCommandHandler) {
            if (subhandler.execute(sender, name, arguments))
                return true;
        }

        return false;
    }

    /**
     * Returns a lift of all descriptors.
     *
     * @return A list of all descriptors for methods.
     */
    public List<Command> getDescriptors() {
        List<Command> commands = new ArrayList<>();

        for (CommandData data : this.getCommandData()) {
            commands.add(data.command);
        }

        return commands;
    }

    /**
     * Returns the descriptors for methods.
     * @return An array of internal representations of Commands.
     */
    private List<CommandData> getCommandData() {
        List<CommandData> commandData = new ArrayList<>(this.commands);

        for (CommandHandler subHandler : this.subCommandHandler) {
            commandData.addAll(subHandler.getCommandData());
        }

        return commandData;
    }

    /**
     * Returns the CommandHandler handling the SubCommands.
     * @param name The name of the command.
     * @return The CommandHandler for the given SubCommand or null.
     */
    public CommandHandler getSubCommandHandler(String name) {

        for (CommandData data : this.getCommandData()) {
            if (data.getName().equals(name)) {
                return data.getSubCommands();
            }
        }

        return null;

    }

    /**
     * Executes the command.<p />
     *
     * Before the command will be executed, some checks will be done to ensure
     * the player/console is allowed to execute the command with the correct syntax.
     *
     * @param sender    The sender
     * @param data      The internal data of the command.
     * @param arguments The arguments.
     */
    private void executeCommand(CommandExecutor sender, CommandData data, String[] arguments) {

        // Check sender type.
        if (sender.isPlayer()) {
            if (!data.command.asPlayer()) {
                sender.sendMessage(_(sender, "cmd.check.noplayer"));
                return;
            }
        } else {
            if (!data.command.asConsole()) {
                sender.sendMessage(_(sender, "cmd.check.noconsole"));
                return;
            }
        }

        // Check permissions.
        if (this.isPermissionsSupported(sender)) {
            if (!data.command.permission().isEmpty() && !sender.hasPermission(data.command.permission())) {
                sender.sendMessage(_(sender, "cmd.check.permission"));
                return;
            }
        } else {
            // Check op only
            if (data.command.opOnly() && !sender.isOp()) {
                sender.sendMessage(_(sender, "cmd.check.oponly"));
                return;
            }
        }

        // Check argument data.
        ArgumentParser parser = new ArgumentParser(sender, this, arguments);

        // Check if only these flags are in the flag list
        if (!data.command.flags().isEmpty() && !parser.getFlags().matches("[" + data.command.flags() + "]*")) {
            sender.sendMessage(_(sender, "cmd.check.flag"));
            return;
        }

        if (data.command.minSize() != -1 && parser.count() < data.command.minSize()) {
            sender.sendMessage(_(sender, "cmd.check.args.min"));
            return;
        }

        if (data.command.maxSize() != -1 && parser.count() > data.command.maxSize()) {
            sender.sendMessage(_(sender, "cmd.check.args.max"));
            return;
        }

        // Execute Command.
        if (data.command.async())
            // Asynchronous execution if Command.async is true
            backend.schedule(new CommandExecutionTask(data, sender, parser));
        else
            // Synchronous execution if Command.async is false.
            data.execute(sender, parser);
    }

    /**
     * Checks if permissions are supported.
     * @return true if so.
     */
    private boolean isPermissionsSupported(CommandExecutor sender) {
        if (sender != null && !(this.getPermissionHandler() instanceof DefaultPermissionHandler)) return true;
        return (this.getServerBackend().hasPermission(sender, "chandler.testpermission") != null);
    }

    /**
     * A server backend handles the internal stuff that needs the API of the plugin system.
     *
     * @return The instance to the server backend.
     */
    public CommandBackend getServerBackend() {
        return this.backend;
    }

    /**
     * The parent handler of the command handler.
     *
     * @return A CommandHandler object.
     */
    public CommandHandler getParentHandler() {
        return this.parent;
    }

    /**
     * An exception handler handles a single exception.
     *
     * @param cls The class that handles the exception.
     * @return An {@link ExceptionHandler}
     */
    public ExceptionHandler<?> getExceptionHandler(Class<?> cls) {
        ExceptionHandler result = null;
        Class<?> current = cls;
        while (result == null && current != null) {
            result = this.exceptionHandlers.get(current);
            current = current.getSuperclass();
        }

        if (result == null && this.parent != null)
            return this.parent.getExceptionHandler(cls);
        return result;
    }

    /**
     * Returns the permission-handler.
     * @return The PermissionHandler.
     */
    public PermissionHandler getPermissionHandler() {
        return this.permissionHandler;
    }

    /**
     * Returns the permission-handler.
     * @param handler The handler to use now.
     */
    public void setPermissionHandler(PermissionHandler handler) {
        this.permissionHandler = handler;
    }

    /**
     * Registers an ExceptionHandler.
     *
     * @param cls     The type of exception that the exception handler handles.
     * @param handler The handler to use.
     */
    public void registerExceptionHandler(Class<? extends Throwable> cls, ExceptionHandler<? extends Throwable> handler) {
        this.exceptionHandlers.put(cls, handler);
    }

    /**
     * Returns the current ArgumentHandler.
     * @return The current-argument handler.
     */
    public ArgumentHandler getArgumentHandler() {
        if (this.argument == null)
            return this.parent.getArgumentHandler();

        return this.argument;
    }

    /**
     * Sets the ArgumentHandler for this CommandHandler.
     * @param handler The new ArgumentHandler.
     */
    public void setArgumentHandler(ArgumentHandler handler) {
        this.argument = handler;
    }

    /**
     * If this handler handles a subcommand, use this function to get the data about the subcommand.
     *
     * @return The Sub-Command-Annotation that is being handled by this CommandHandler.
     */
    SubCommand getSubCommand() {
        return subcommand;
    }

    /**
     * Registers an subordinate CommandHandler.
     * @param index         The index where the new command-handler should be registered.
     * @param subHandler    The subordinate command handler.
     */
    private void registerCommandHandler(int index, CommandHandler subHandler) {
        if (index == -1) {
            index = this.subCommandHandler.size();
        }

        this.subCommandHandler.add(index, subHandler);
    }

    /**
     * Removes a subordinate CommandHandler.
     * @param subHandler The SubHandler to remove.
     */
    private void unregisterCommandHandler(CommandHandler subHandler) {
        this.subCommandHandler.remove(subHandler);
    }

    /**
     * Returns the CommandHandler using the given index.
     * @param index The index of the SubHandler to remove.
     * @return The CommandHandler on this index.
     */
    private CommandHandler getCommandHandler(int index) {
        return this.subCommandHandler.get(index);
    }

}
