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

package net.stuxcrystal.simpledev.commands;

import net.stuxcrystal.simpledev.commands.arguments.ArgumentParser;
import net.stuxcrystal.simpledev.commands.commands.CommandContainer;
import net.stuxcrystal.simpledev.commands.commands.CommandLoader;
import net.stuxcrystal.simpledev.commands.commands.CommandManager;
import net.stuxcrystal.simpledev.commands.arguments.ArgumentHandler;
import net.stuxcrystal.simpledev.commands.component.ComponentContainer;
import net.stuxcrystal.simpledev.commands.component.ComponentManager;
import net.stuxcrystal.simpledev.commands.contrib.DefaultPermissionHandler;
import net.stuxcrystal.simpledev.commands.exceptions.ExceptionHandler;
import net.stuxcrystal.simpledev.commands.translations.TranslationManager;
import net.stuxcrystal.simpledev.commands.utils.HandleWrapper;
import org.apache.commons.lang.ArrayUtils;

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
     * The default fallback command name.
     */
    public static final String FALLBACK_COMMAND_NAME = " ";

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Contains the manager for commands.
     */
    private CommandManager commands = new CommandManager();

    /**
     * List of registered sub-command-handlers.
     */
    private final List<CommandHandler> subCommandHandler = new ArrayList<>();

    /**
     * The java backend to register tasks.
     */
    protected final CommandBackend backend;

    /**
     * Container for components.
     */
    private ComponentManager components = null;

    /**
     * Manages translations
     */
    private final TranslationManager localization;

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

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The Constructor for base-commands.
     *
     * @param backend The backend that handles the command handler.
     */
    public CommandHandler(CommandBackend backend) {
        this(backend, new TranslationManager(), null);
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
        this(handler.getServerBackend(), handler.getTranslationManager(), null);
        this.setArgumentHandler(handler.getArgumentHandler());

    }

    /**
     * Internal constructor for subcommand-support.
     *
     * @param backend    The backend handling the server side stuff.
     * @param localization    The translation manager.
     * @param parent     The parent command handler.
     */
    private CommandHandler(CommandBackend backend, TranslationManager localization, CommandHandler parent) {
        this.backend = backend;
        this.localization = localization;
        this.parent = parent;

        // Intitialize Backend.
        if (parent == null) {
            this.backend.setCommandHandler(this);
            this.argument = new ArgumentHandler();
            this.components = new ComponentManager(this);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * <p>Creates a new child handler from this command handler.</p>
     *
     * <p>Can be used in sub-commands and child command handlers.</p>
     *
     * @return The child handler.
     */
    public CommandHandler createChildHandler() {
        return new CommandHandler(this.getServerBackend(), this.getTranslationManager(), this);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the manager that is responsible for translations.
     * @return The manager that is responsible for translations.
     */
    public TranslationManager getTranslationManager() {
        return this.localization;
    }

    /**
     * Translates a string
     *
     * @param sender  The sender that should receive the message.
     * @param key     The key that receives the message.
     * @param values  The values that are used in the message.
     * @return The translated message.
     */
    protected String T(CommandExecutor sender, String key, Object... values) {
        return this.getTranslationManager().translate(sender, key, values);
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Registers the commands.
     * Also prepares subcommands.
     *
     * @param container The container for the methods.
     */
    public void registerCommands(Object container) {
        this.commands.registerCommands(this, container);
    }

    /**
     * Executes the command.
     * @param executor  The executor that executes the command.
     * @param name      The name of the command.
     * @param args      The arguments for the command.
     * @return {@code true} if the command was found and has been executed.
     */
    public boolean execute(CommandExecutor executor, String name, String[] args) {
        if (this.commands.execute(executor, name, args))
            return true;

        for (CommandHandler subhandler : this.subCommandHandler) {
            if (subhandler.execute(executor, name, args))
                return true;
        }

        return false;
    }

    /**
     * Implementation of the execute method that uses the first argument as its command name.
     *
     * @param executor The executor that executes the command.
     * @param rawArgs  The raw arguments.
     */
    public void execute(CommandExecutor executor, String[] rawArgs) {
        String name;
        if (rawArgs.length == 0) {
            name = CommandHandler.FALLBACK_COMMAND_NAME;
            rawArgs = new String[0];
        } else {
            name = rawArgs[0];
            rawArgs = (String[]) ArrayUtils.remove(rawArgs, 0);
        }

        if (!this.execute(executor, name, rawArgs))
            executor.sendMessage(T(executor, "cmd.notfound"));
    }

    /**
     * Short for CommandHandler.execute(executor, parser.getArguments(0));
     *
     * @param executor The executor that executes the command.
     * @param parser   The already parsed argumetns.
     */
    public void execute(CommandExecutor executor, ArgumentParser parser) {
        this.execute(executor, parser.getArguments(0));
    }

    /**
     * Returns a lift of all descriptors.
     *
     * @return A list of all descriptors for methods.
     */
    public List<CommandContainer> getCommands() {
        return new ArrayList<>(this.commands.getCommands());
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * A server backend handles the internal stuff that needs the API of the plugin system.
     *
     * @return The instance to the server backend.
     */
    public CommandBackend getServerBackend() {
        return this.backend;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The parent handler of the command handler.
     *
     * @return A CommandHandler object.
     */
    public CommandHandler getParentHandler() {
        return this.parent;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
     * Checks if permissions are supported.
     * @return true if so.
     */
    public boolean isPermissionsSupported(CommandExecutor sender) {
        if (sender != null && !(this.getPermissionHandler() instanceof DefaultPermissionHandler)) return true;
        return (this.getServerBackend().hasPermission(sender, "chandler.testpermission") != null);
    }


    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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
     * An exception handler handles a single exception.
     *
     * @param cls The class that handles the exception.
     * @return An {@link net.stuxcrystal.simpledev.commands.exceptions.ExceptionHandler}
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

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

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

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Registers a command loader.
     * @param loader The loader to register.
     */
    public void registerCommandLoader(CommandLoader loader) {
        this.commands.registerLoader(loader);
    }

    /**
     * Unregisters a command loader.
     * @param loader The loader to unregister.
     */
    public void unregisterCommandLoader(CommandLoader loader) {
        this.commands.unregisterLoader(loader);
    }

    /**
     * Returns the root-command handler.
     * @return The root commandhandler.
     */
    public CommandHandler getRootCommandHandler() {
        CommandHandler current = this;
        while (current.parent != null)
            current = current.parent;
        return current;
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Adds all extension methods to the command handler.
     * @param component The component to add.
     */
    public void registerComponent(ComponentContainer component) {
        this.getRootCommandHandler().components.registerComponents(component);
    }

    /**
     * Adds all static extension methods to the command handler.
     * @param component The class for the container.
     */
    public void registerComponent(Class<? extends ComponentContainer> component) {
        this.getRootCommandHandler().components.registerComponents(component);
    }

    /**
     * Checks if the component has been registered.
     * @param component The component.
     * @return {@code true} if the component has been registered.
     */
    public boolean isComponentRegistered(Class<? extends ComponentContainer> component) {
        return this.getRootCommandHandler().components.isRegistered(component);
    }

    /**
     * Calls a component method.
     * @param name      The name of the method.
     * @param self      The object that is associated with the object.
     * @param params    The parameters.
     * @param <T>       The return type.
     * @return The result of the method.
     */
    public <T> T callComponent(String name, HandleWrapper self, Object... params) throws Throwable {
        return this.getRootCommandHandler().components.call(name, self, params);
    }

    /**
     * Checks if the function has already been registered.
     * @param name      The name of the function.
     * @param wrapper   The class the function extends.
     * @param types     The types of the class.
     * @return {@code true} if the function has been registered.
     */
    public boolean hasFunction(String name, Class<? extends HandleWrapper> wrapper, Class<?>... types) {
        return this.getRootCommandHandler().components.hasMethod(name, wrapper, types);
    }

}
