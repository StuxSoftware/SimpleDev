package net.stuxcrystal.commandhandler.component;

import net.stuxcrystal.commandhandler.CommandExecutor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Proxy for components.
 */
public class ComponentProxy implements InvocationHandler {

    /**
     * The command executor for the component proxy.
     */
    private final CommandExecutor executor;

    /**
     * Creates a new component proxy.
     * @param executor The executor to implement.
     */
    public ComponentProxy(CommandExecutor executor) {
        this.executor = executor;
    }

    /**
     * Proxy for components.
     * @param proxy   The proxy for components.
     * @param method  The method that is proxied.
     * @param args    The arguments.
     * @return The object to invoke.
     * @throws Throwable If an I/O-Operation fails.
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return this.executor.getCommandHandler().callComponent(method.getName(), this.executor, args);
    }

    /**
     * Creates a new interface that implements the given extension methods.
     *
     * @param interfaceInstance The interface to implement.
     * @param <T>               The type of the interface.
     * @return The implemented interface.
     */
    @SuppressWarnings("unchecked")
    public <T> T createInstance(Class<T> interfaceInstance) {
        return (T)Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class<?>[]{interfaceInstance}, this);
    }

}
