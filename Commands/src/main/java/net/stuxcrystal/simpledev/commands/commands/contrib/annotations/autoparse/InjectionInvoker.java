package net.stuxcrystal.simpledev.commands.commands.contrib.annotations.autoparse;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.arguments.ArgumentList;
import net.stuxcrystal.simpledev.commands.arguments.iterators.ArgumentContainer;
import net.stuxcrystal.simpledev.commands.commands.contrib.annotations.MethodInvoker;
import net.stuxcrystal.simpledev.commands.utils.HandleWrapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The leaf annotation command.
 */
public class InjectionInvoker extends MethodInvoker {

    /**
     * The dummy object.
     */
    private static final Object DUMMY = new Object();

    /**
     * The injection invoker.
     */
    public static MethodInvoker INJECTIONS = new InjectionInvoker();

    /**
     * Singleton
     */
    private InjectionInvoker() {}

    /**
     * Invokes the command and parses the parameters.
     * @param executor The executor that should execute the command
     * @param list     The argument list shat should be used.
     */
    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Method method, Object instance, CommandExecutor executor, ArgumentList list)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {

        // Prepare the values.
        Object[] values = new Object[method.getParameterCount()];
        Annotation[][] annotations = method.getParameterAnnotations();
        Class<?>[] cls = method.getParameterTypes();

        for (int i = 0; i<values.length; i++) {
            Annotation[] pAnnotations = annotations[i];
            Class<?> paramType = cls[0];

            Object currentValue = InjectionInvoker.DUMMY;

            for (Annotation annotation : pAnnotations) {

                // Handle argument lists.
                if (annotation instanceof Argument) {
                    // Get the desired argument type.
                    Class parsingParamType = ((Argument) annotation).type();
                    if (parsingParamType.equals(void.class))
                        parsingParamType = paramType;

                    // This is an array, we will enter array parsing mode.
                    if (parsingParamType.isArray() || parsingParamType.equals(ArgumentContainer.class)) {
                        int start = ((Argument) annotation).value();
                        Integer stop = ((Argument) annotation).stop();
                        int step = ((Argument) annotation).step();

                        if (stop == Integer.MAX_VALUE)
                            stop = null;

                        currentValue = list.slice(start, stop, step);
                        if (parsingParamType.isArray())
                            currentValue = ((ArgumentContainer) currentValue).as(parsingParamType.getComponentType()).toArray();

                    // We deal with simple objects.
                    } else {
                        // We deal with an required argument.
                        if ("§".equals(((Argument) annotation).defaultValue())) {
                            currentValue = list.get(((Argument) annotation).value(), parsingParamType);

                        // We deal with an optional argument.
                        } else {
                            Object def = executor.getCommandHandler().getArgumentHandler().convertType(
                                    ((Argument) annotation).defaultValue(),
                                    parsingParamType,
                                    executor,
                                    executor.getBackend()
                            );
                            currentValue = list.get(((Argument) annotation).value(), parsingParamType, def);
                        }
                    }

                // Handle backends.
                } else if (annotation instanceof Backend) {
                    currentValue = executor.getBackend();

                // Return the executor of the function.
                } else if (annotation instanceof Executor) {
                    currentValue = executor;
                } else if (annotation instanceof Flag) {
                    currentValue = list.hasFlag(((Flag) annotation).value());
                }
            }

            if (currentValue == null)
                throw new IllegalArgumentException("Undescribed parameter found: " + i);

            // Handle the component annotation.
            for (Annotation annotation : pAnnotations) {
                if (annotation instanceof Component) {

                    if (currentValue.getClass().isArray()) {
                        if (!(HandleWrapper.class.isAssignableFrom(currentValue.getClass().getComponentType())))
                            continue;

                        int length = Array.getLength(currentValue);
                        Object o = Array.newInstance(paramType.getComponentType(), length);
                        for (int index = 0; index<length; index++)
                            Array.set(
                                    o,
                                    index,
                                    ((HandleWrapper) Array.get(currentValue, index)).getComponent(
                                            paramType.getComponentType()
                                    )
                            );
                        currentValue = o;
                    } else {
                        if (!(currentValue instanceof HandleWrapper))
                            continue;

                        currentValue = ((HandleWrapper) currentValue).getComponent(paramType);
                    }


                }
            }

            values[i] = currentValue;
        }

        method.invoke(instance, values);
    }

    /**
     * Checks if the leaf anootation command can be parsed.
     * @param method The method to parse.
     * @return The injection command.
     */
    public static boolean isInjectionCommand(Method method) {
        for (Annotation[] annotations : method.getParameterAnnotations())
            for (Annotation annotation : annotations)
                if ((annotation instanceof Argument) || (annotation instanceof Backend) || (annotation instanceof Executor))
                    return true;
        return false;
    }
}
