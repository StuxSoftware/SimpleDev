package net.stuxcrystal.simpledev.commands.commands.contrib.annotations.autoparse;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.arguments.ArgumentList;
import net.stuxcrystal.simpledev.commands.commands.contrib.annotations.Command;
import net.stuxcrystal.simpledev.commands.commands.contrib.annotations.simple.LeafAnnotationCommand;
import net.stuxcrystal.simpledev.commands.utils.HandleWrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The leaf annotation command.
 */
public class ParsingAnnotationCommand extends LeafAnnotationCommand {

    /**
     * The dummy object.
     */
    private static final Object DUMMY = new Object();

    /**
     * Creates a new annotation based command.
     *
     * @param command  The command metadata.
     * @param method   The method to execute.
     * @param instance The instance of the command metadata.
     */
    public ParsingAnnotationCommand(Command command, Method method, Object instance) {
        super(command, method, instance);
    }

    /**
     * Invokes the command and parses the parameters.
     * @param executor The executor that should execute the command
     * @param list     The argument list shat should be used.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void _invoke(CommandExecutor executor, ArgumentList list)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        // Prepare the values.
        Object[] values = new Object[this.method.getParameterCount()];
        Annotation[][] annotations = this.method.getParameterAnnotations();
        Class<?>[] cls = this.method.getParameterTypes();

        for (int i = 0; i<values.length; i++) {
            Annotation[] pAnnotations = annotations[i];
            Class<?> paramType = cls[0];

            Object currentValue = ParsingAnnotationCommand.DUMMY;

            for (Annotation annotation : pAnnotations) {

                // Handle argument lists.
                if (annotation instanceof Argument) {
                    // Get the desired argument type.
                    Class parsingParamType = ((Argument) annotation).type();
                    if (parsingParamType.equals(void.class))
                        parsingParamType = paramType;

                    // This is an array, we will enter array parsing mode.
                    if (parsingParamType.isArray()) {
                        int start = ((Argument) annotation).start();
                        Integer stop = ((Argument) annotation).stop();
                        int step = ((Argument) annotation).step();

                        if (stop == Integer.MAX_VALUE)
                            stop = null;

                        currentValue = list.slice(start, stop, step).as(parsingParamType).toArray();

                    // We deal with simple objects.
                    } else {
                        // We deal with an required argument.
                        if ("\t".equals(((Argument) annotation).defaultValue())) {
                            currentValue = list.get(((Argument) annotation).start(), parsingParamType);

                        // We deal with an optional argument.
                        } else {
                            Object def = executor.getCommandHandler().getArgumentHandler().convertType(
                                    ((Argument) annotation).defaultValue(),
                                    parsingParamType,
                                    executor,
                                    executor.getBackend()
                            );
                            currentValue = list.get(((Argument) annotation).start(), parsingParamType, def);
                        }
                    }

                // Handle backends.
                } else if (annotation instanceof Backend) {
                    currentValue = executor.getBackend();

                // Return the executor of the function.
                } else if (annotation instanceof Executor) {
                    currentValue = executor;
                }
            }

            if (currentValue == null)
                throw new IllegalArgumentException("Undescribed parameter found: " + i);

            // Handle the component annotation.
            for (Annotation annotation : pAnnotations) {
                if (annotation instanceof Component) {
                    if (!(currentValue instanceof HandleWrapper))
                        continue;

                    currentValue = ((HandleWrapper) currentValue).getComponent(paramType);
                }
            }

            values[i] = currentValue;
        }

        this.method.invoke(this.instance, values);
    }

    /**
     * Checks if the leaf anootation command can be parsed.
     * @param method The method to parse.
     * @return The injection command.
     */
    private static boolean isInjectionCommand(Method method) {
        for (Annotation[] annotations : method.getParameterAnnotations())
            for (Annotation annotation : annotations)
                if ((annotation instanceof Argument) || (annotation instanceof Backend) || (annotation instanceof Executor))
                    return true;
        return false;
    }
}
