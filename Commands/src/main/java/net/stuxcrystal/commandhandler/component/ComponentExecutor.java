package net.stuxcrystal.commandhandler.component;

import java.util.concurrent.Callable;

/**
 * Executes a callable method.
 */
public class ComponentExecutor<T> implements Callable<T> {

    /**
     * The component method to execute.
     */
    private final ComponentMethod method;

    /**
     * The object that contains the extension method.
     */
    private final Object self;

    /**
     * The parameters that have been passed.
     */
    private final Object[] params;

    /**
     * Creates a new component executor.
     * @param method The method to execute.
     * @param self   The object that contains the extension method.
     * @param params The parameters that have been passed.
     */
    public ComponentExecutor(ComponentMethod method, Object self, Object[] params) {
        this.method = method;
        this.self = self;
        this.params = params;
    }

    @Override
    public T call() throws Exception {
        return this.method.call(this.self, this.params);
    }
}
