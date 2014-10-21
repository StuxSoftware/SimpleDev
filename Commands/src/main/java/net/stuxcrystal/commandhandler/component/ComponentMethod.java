package net.stuxcrystal.commandhandler.component;

import org.apache.commons.lang.ArrayUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Contains the actual component method.
 */
public class ComponentMethod {


    /**
     * May be used for other things later.
     */
    Component component;

    /**
     * Represents the method to call.
     */
    Method method;

    /**
     * The container of the component.
     */
    ComponentContainer container;

    /**
     * Contains the parameter types.
     */
    Class<?>[] params;

    /**
     * Creates a new container for the component data.
     * @param method     The method.
     * @param container  The container.
     */
    public ComponentMethod(Method method, ComponentContainer container, Component component) {
        this.method = method;
        this.container = container;
        this.component = component;
        this.params = (Class<?>[]) ArrayUtils.remove(this.method.getParameterTypes(), 0);
    }

    /**
     * Returns the name of the component.
     * @return The name of the component.
     */
    public String getName() {
        return this.method.getName();
    }

    /**
     * Returns the actual type of the self parameter.
     * @return The type of the argument.
     */
    public Class<?> getSelfParameter() {
        return this.method.getParameterTypes()[0];
    }

    /**
     * Returns all arguments.
     * @return All arguments.
     */
    public Class<?>[] getParameters() {
        return this.params;
    }

    /**
     * Calls the method.
     * @param selfArg     The object which extension function will be called.
     * @param params      The parameters for the function.
     * @param <T>         Trick to allow dynamic return types.
     * @return            The result of the method.
     * @throws ReflectiveOperationException If an reflective operation exception occurs.
     */
    @SuppressWarnings("unchecked")
    public <T> T call(Object selfArg, Object[] params) throws ReflectiveOperationException {
        if (!this.method.isAccessible())
            this.method.setAccessible(true);

        Object self;
        if (Modifier.isStatic(this.method.getModifiers()))
            self = null;
        else if (container != null)
            self = container;
        else
            throw new ReflectiveOperationException("Failed to get component instance.");

        return (T)this.method.invoke(self, ArrayUtils.add(params, 0, selfArg));
    }

}
