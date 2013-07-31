package net.stuxcrystal.commandhandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * The handler for commands.
 *
 * @author StuxCrystal
 */
public class CommandHandler {

    /**
     * The internal datastructure.
     *
     * @author StuxCrystal
     */
    static class CommandData {

        /**
         * The Command-Annotation.
         */
        private final Command command;

        /**
         * Subcommand handler.
         */
        private final CommandHandler subcommands;

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
        private final Backend backend;

        public CommandData(Command command, Method method, Object instance, Backend backend, CommandHandler subcommands) {
            this.command = command;
            this.method = method;
            this.instance = instance;
            this.backend = backend;
            this.subcommands = subcommands;
        }

        /**
         * Executes the command.<p />
         * Calls the subcommand if needed.
         *
         * @param sender
         * @param arguments
         */
        public void execute(CommandExecutor sender, ArgumentParser arguments) {
            if (this.subcommands == null || this.subcommands.getSubCommand().time() == CallTime.PRE)
                if (!_execute(sender, arguments)) {
                    if (this.subcommands != null)
                        sender.sendMessage(subcommands._(sender, "cmd.notfound"));
                    return;
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

                ExceptionHandler handler = this.subcommands.getExceptionHandler(e.getCause().getClass());
                if (handler == null) {
                    // If the exception couldn't be handled, throw this generic exception.
                    sender.sendMessage(subcommands._(sender, "cmd.exception"));
                    backend.getLogger().log(Level.SEVERE, "Exception while calling command.", e.getCause());
                } else {
                    // Handle the exception.
                    handler.exception(e.getCause(), this.command.value(), sender, arguments);
                }
            }

            return true;
        }
    }


    /**
     * List of commands.
     */
    private final List<CommandData> commands = new ArrayList<>();

    /**
     * The java backend to register tasks.
     */
    protected final Backend backend;

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
    private final CommandHandler parent;

    /**
     * Stores all exception handlers.
     */
    private final Map<Class<? extends Throwable>, ExceptionHandler<? extends Throwable>> exceptionHandlers =
            new HashMap<Class<? extends Throwable>, ExceptionHandler<? extends Throwable>>();

    /**
     * The Constructor for base-commands.
     *
     * @param backend
     */
    public CommandHandler(Backend backend) {
        this(backend, null, new TranslationManager(), null);
    }

    /**
     * Internal constructor for subcommand-support.
     *
     * @param backend    The backend handling the server side stuff.
     * @param subcommand The subcommand that this handler handles.
     * @param manager    The translation manager.
     * @param parent     The parent command handler.
     */
    private CommandHandler(Backend backend, SubCommand subcommand, TranslationManager manager, CommandHandler parent) {
        this.backend = backend;
        this.subcommand = subcommand;
        this.manager = manager;
        this.parent = null;
    }

    public TranslationManager getTranslationManager() {
        return this.manager;
    }

    /**
     * Translates a value.
     *
     * @param sender The sender that sent the message
     * @param msg    The message itself.
     * @return
     */
    protected String _(CommandExecutor sender, String msg) {
        return this.manager.translate(sender, msg);
    }

    /**
     * Registers the commands.<p />
     * Also prepares subcommands.
     *
     * @param container
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
            commands.add(new CommandData(method.getAnnotation(Command.class), method, container, this.backend, subhandler));
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
     * <p/>
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
            if (data.command.value().equals(name)) {
                executeCommand(sender, data, arguments);
                return true;
            }
        }

        // Then ignore the case.
        for (CommandData data : commands) {
            if (data.command.value().equalsIgnoreCase(name)) {
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

        return false;
    }

    /**
     * Returns the name of the description.
     *
     * @param name
     * @return
     */
    public String getDescription(String name) {
        for (CommandData data : commands) {
            if (data.command.value().equals(name)) {
                return data.command.description();
            }
        }

        return null;
    }

    /**
     * Returns a lift of all descriptors.
     *
     * @return
     */
    public List<Command> getDescriptors() {
        List<Command> commands = new ArrayList<>();

        for (CommandData data : this.commands) {
            commands.add(data.command);
        }

        return commands;
    }

    /**
     * Executes the command.
     *
     * @param sender    The sender
     * @param data      The internal data of the command.
     * @param arguments The arguments.
     */
    private void executeCommand(CommandExecutor sender, CommandData data, String[] arguments) {

        // Check op only
        if (data.command.opOnly() && !sender.isOp()) {
            sender.sendMessage(_(sender, "cmd.check.oponly"));
            return;
        }

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
        if (!data.command.permission().isEmpty() && !sender.hasPermission(data.command.permission())) {
            sender.sendMessage(_(sender, "cmd.check.permission"));
            return;
        }

        // Check argument data.
        ArgumentParser parser = new ArgumentParser(arguments);

        // Check if only these flags are in the flag list
        if (!data.command.flags().isEmpty() && !parser.getFlags().matches("[" + data.command.flags() + "]*")) {
            sender.sendMessage(_(sender, "cmd.check.flag"));
            return;
        }

        if (data.command.minSize() != -1 && parser.count() < data.command.minSize()) {
            sender.sendMessage(_(sender, "cmd.flag.args.min"));
            return;
        }

        if (data.command.maxSize() != -1 && parser.count() > data.command.maxSize()) {
            sender.sendMessage(_(sender, "cmd.flag.args.max"));
            return;
        }

        // Execute Command.
        if (data.command.async())
            // Asynchronous Execution if Command.async is true
            backend.schedule(new CommandExecutionTask(data, sender, parser));
        else
            // Synchronous execution if Command.async is false.
            data.execute(sender, parser);
    }

    /**
     * A server backend handles the internal stuff that needs the API of the plugin system.
     *
     * @return The instance to the server backend.
     */
    public Backend getServerBackend() {
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
        while (result == null || current != null) {
            result = this.exceptionHandlers.get(cls);
            current = cls.getSuperclass();
        }

        if (result == null)
            return this.parent.getExceptionHandler(cls);
        return result;
    }

    /**
     * Registers an ExceptionHandler.
     *
     * @param cls     The type of exception that the exception handler handles.
     * @param handler
     */
    public void registerExceptionHandler(Class<? extends Throwable> cls, ExceptionHandler<? extends Throwable> handler) {
        this.exceptionHandlers.put(cls, handler);
    }

    /**
     * If this handler handles a subcommand, use this function to get the data about the subcommand.
     *
     * @return
     */
    SubCommand getSubCommand() {
        return subcommand;
    }

}
