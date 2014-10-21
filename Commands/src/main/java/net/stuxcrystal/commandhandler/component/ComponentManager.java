package net.stuxcrystal.commandhandler.component;

import net.stuxcrystal.commandhandler.utils.HandleWrapper;
import org.apache.commons.lang.ArrayUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * A manager for components.
 */
public class ComponentManager {

    /**
     * Container for components.
     */
    class ComponentMethod {

        /**
         * May be used for other things later.
         */
        private Component component;

        /**
         * Represents the method to call.
         */
        private Method method;

        /**
         * The container of the component.
         */
        private ComponentContainer container;

        /**
         * Creates a new container for the component data.
         * @param method     The method.
         * @param container  The container.
         */
        public ComponentMethod(Method method, ComponentContainer container, Component component) {
            this.method = method;
            this.container = container;
            this.component = component;
        }

        /**
         * Returns the name of the component.
         * @return The name of the component.
         */
        public String getName() {
            return this.method.getName();
        }

        /**
         * Returns the actual type of the self arguments.
         * @return The type of the argument.
         */
        public Class<?> getSelfArgument() {
            return this.method.getParameterTypes()[0];
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

    /**
     * Components of the command handler.
     */
    private List<ComponentMethod> methods = new ArrayList<>();

    /**
     * Actual registered classes.
     */
    private HashSet<Class<? extends ComponentContainer>> classes = new HashSet<>();

    /**
     * Registers the methods of the component.
     *
     * @param componentType The type of the component.
     * @param component     The component instance itself.
     */
    @SuppressWarnings("unchecked")
    private void registerComponents(Class<? extends ComponentContainer> componentType, ComponentContainer component) {
        Class<?> supercls = componentType.getSuperclass();
        if (ComponentContainer.class.isAssignableFrom(supercls)) {
            this.registerComponents((Class<? extends ComponentContainer>) supercls, component);
        }

        for (Method method : componentType.getDeclaredMethods()) {
            if (!method.isAccessible())
                method.setAccessible(true);

            if (!Modifier.isStatic(method.getModifiers()) && component==null)
                continue;

            if (!method.isAnnotationPresent(Component.class))
                continue;

            Class<?>[] params = method.getParameterTypes();

            // Empty function parameters are not supported.
            if (params.length == 0)
                throw new IllegalArgumentException(
                        "Illegal method format of '" + method.getName() + "': " +
                                "No parameters."
                );

            // The first argument must be the self argument.
            if (!HandleWrapper.class.isAssignableFrom(params[0]))
                throw new IllegalArgumentException(
                        "Illegal method format of '" + method.getName() + "': " +
                                "First parameter must be a subclass of HandlerWrapper"
                );

            this.methods.add(new ComponentMethod(method, component, method.getAnnotation(Component.class)));
        }

        this.classes.add(componentType);
    }

    /**
     * Registers only static extension methods to all command executors.
     * @param componentType Components.
     */
    public void registerComponents(Class<? extends ComponentContainer> componentType) {
        this.registerComponents(componentType, null);
    }

    /**
     * Registers all extension methods of this container.
     * @param container The component.
     */
    public void registerComponents(ComponentContainer container) {
        this.registerComponents(container.getClass(), container);
    }

    /**
     * Checks if the extension has been registered.
     * @param cls The class to register.
     * @return {@code true} if the extension has been registered.
     */
    public boolean isRegistered(Class<? extends ComponentContainer> cls) {
        return this.classes.contains(cls);
    }

    /**
     * Calls the extension function.
     *
     * @param name      The name of the extension function.
     * @param self      The object which is the context of the commands.
     * @param params    The additional parameters.
     * @param <T> The return type.
     * @return The function return.
     */
    public <T> T call(String name, HandleWrapper self, Object[] params) throws Throwable {
        // Find suitable method for handler wrapper.
        ComponentMethod method = null;
        for (ComponentMethod m : this.methods) {
            if (!m.getName().equals(name))
                continue;
            if (!m.getSelfArgument().isInstance(self))
                continue;

            method = m;
            break;
        }

        // If none was found, throw IllegalArgumentException.
        if (method == null)
            throw new IllegalArgumentException("Unknown extension method.");

        // Call the method.
        try {
            return method.call(self, params);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        } catch (ReflectiveOperationException e) {
            throw new ComponentAccessException(e);
        }
    }
}
