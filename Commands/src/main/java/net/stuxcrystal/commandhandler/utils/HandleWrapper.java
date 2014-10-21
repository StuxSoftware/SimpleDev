package net.stuxcrystal.commandhandler.utils;

import net.stuxcrystal.commandhandler.CommandHandler;
import net.stuxcrystal.commandhandler.component.ComponentProxy;

/**
 * An object that wraps an handle object.<p />
 *
 * It is always possible to attach extension methods to a handle-object.
 */
public abstract class HandleWrapper<T> {

    /**
     * The handle to an instance in the backend.
     */
    private final T handle;

    /**
     * The component proxy for the object.
     */
    private final ComponentProxy proxy = new ComponentProxy(this);

    /**
     * Creates a new handle.
     * @param handle The handle in the backend.
     */
    protected HandleWrapper(T handle) {
        this.handle = handle;
    }

    /**
     * Returns the handle object.
     * @return The object in the backend.
     */
    public final T getHandle() {
        return this.handle;
    }

    /**
     * Returns the command-handler that is controlling the wrapper.
     * @return The command handler.
     */
    public abstract CommandHandler getCommandHandler();

    /**
     * Returns the extension for the player.
     * @param interfaceClass The extension class.
     * @param <R>            The extension class.
     * @return The type.
     */
    public <R> R getComponent(Class<R> interfaceClass) {
        return this.proxy.createInstance(interfaceClass);
    }
}
