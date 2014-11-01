package net.stuxcrystal.simpledev.commands.commands.contrib.annotations;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.arguments.ArgumentList;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The invoker for a method.
 */
public abstract class MethodInvoker {

    /**
     * The default method invoker.
     */
    public static final MethodInvoker DEFAULT = new DefaultMethodInvoker();

    /**
     * The default implementation of the method invoker.
     */
    private static class DefaultMethodInvoker extends MethodInvoker {
        /**
         * Singleton.
         */
        private DefaultMethodInvoker() {}

        /**
         * Just invoke the method.
         * @param method    The method that should be invoked.
         * @param instance  The instance of the method
         * @param executor  The executor that executes the function.
         * @param list      The argument list that should be used.
         * @throws IllegalAccessException     If we failed to access the command.
         * @throws IllegalArgumentException   If the wrong arguments were passed.
         * @throws InvocationTargetException  If the invoker throwed an exception.
         */
        @Override
        public void invoke(Method method, Object instance, CommandExecutor executor, ArgumentList list)
                throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
            method.invoke(instance, executor, list);
        }
    }

    /**
     * Invokes a method.
     * @param method    The method that should be invoked.
     * @param instance  The instance of the method
     * @param executor  The executor that executes the function.
     * @param list      The argument list that should be used.
     * @throws IllegalAccessException     If we failed to access the command.
     * @throws IllegalArgumentException   If the wrong arguments were passed.
     * @throws InvocationTargetException  If the invoker throwed an exception.
     */
    public abstract void invoke(Method method, Object instance, CommandExecutor executor, ArgumentList list)
            throws IllegalAccessException, IllegalArgumentException, InvocationTargetException;

}
