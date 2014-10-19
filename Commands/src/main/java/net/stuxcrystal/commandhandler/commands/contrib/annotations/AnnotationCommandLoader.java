package net.stuxcrystal.commandhandler.commands.contrib.annotations;

import net.stuxcrystal.commandhandler.CommandHandler;
import net.stuxcrystal.commandhandler.commands.CommandContainer;
import net.stuxcrystal.commandhandler.commands.CommandLoader;

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
                commands.add(new BranchAnnotationCommand(method.getAnnotation(Command.class), method, container, command, subhandler));
            } else {
                // Just register the command.
                commands.add(new LeafAnnotationCommand(method.getAnnotation(Command.class), method, container));
            }
        }

        return commands;
    }
}
