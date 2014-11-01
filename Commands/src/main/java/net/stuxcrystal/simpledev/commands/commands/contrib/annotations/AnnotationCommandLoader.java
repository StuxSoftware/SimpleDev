package net.stuxcrystal.simpledev.commands.commands.contrib.annotations;

import net.stuxcrystal.simpledev.commands.CommandHandler;
import net.stuxcrystal.simpledev.commands.commands.CommandContainer;
import net.stuxcrystal.simpledev.commands.commands.CommandLoader;
import net.stuxcrystal.simpledev.commands.commands.contrib.annotations.autoparse.InjectionInvoker;
import net.stuxcrystal.simpledev.commands.commands.contrib.annotations.simple.BranchAnnotationCommand;
import net.stuxcrystal.simpledev.commands.commands.contrib.annotations.simple.LeafAnnotationCommand;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * The command loader that uses annotations.
 */
public class AnnotationCommandLoader implements CommandLoader {

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

    @Override
    public List<CommandContainer> register(CommandHandler registrar, Object container) {
        if (container instanceof Class<?>) {
            if (CommandListener.class.isAssignableFrom((Class<?>)container)) {
                container = newInstance((Class<?>) container);
            } else {
                return null;
            }
        }

        if (!(container instanceof CommandListener))
            return null;

        List<CommandContainer> commands = new ArrayList<>();

        for (Method method : container.getClass().getDeclaredMethods()) {

            // The method has to be accessible.
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }

            // The method have to be annotated by Command.
            if (!method.isAnnotationPresent(Command.class)) continue;

            // Determine the correct method invoker.
            MethodInvoker mi;
            if (InjectionInvoker.isInjectionCommand(method))
                mi = InjectionInvoker.INJECTIONS;
            else
                mi = MethodInvoker.DEFAULT;

            // Support for subcommands.
            if (method.isAnnotationPresent(SubCommand.class)) {
                CommandHandler subhandler = null;
                SubCommand command = method.getAnnotation(SubCommand.class);

                // Create a sub-handler.
                subhandler = registrar.createChildHandler();

                // Populate the command handler.
                Class<?>[] classes = command.value();
                Object current;
                for (Class<?> cls : classes) {
                    current = newInstance(cls);
                    if (current != null)
                        subhandler.registerCommands(newInstance(cls));
                }

                // Register the command.
                commands.add(new BranchAnnotationCommand(method.getAnnotation(Command.class), method, container, command, subhandler, mi));
            } else {
                // Just register the command.
                commands.add(new LeafAnnotationCommand(method.getAnnotation(Command.class), method, container, mi));
            }
        }

        return commands;
    }
}
