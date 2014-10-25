package net.stuxcrystal.simpledev.commandhandler.component;

import net.stuxcrystal.simpledev.commandhandler.utils.HandleWrapper;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Proxy for components.
 */
public class ComponentProxy implements InvocationHandler {

    /**
     * The command handle for the component proxy.
     */
    private final HandleWrapper handle;

    /**
     * Creates a new component proxy.
     * @param self The handle to implement.
     */
    public ComponentProxy(HandleWrapper self) {
        this.handle = self;
    }

    /**
     * Proxy for components.
     * @param proxy   The proxy for components.
     * @param method  The method that is proxied.
     * @param args    The arguments.
     * @return The object to invoke.
     * @throws Throwable If the underlying component function fails.
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return this.handle.getCommandHandler().callComponent(method.getName(), this.handle, args);
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
