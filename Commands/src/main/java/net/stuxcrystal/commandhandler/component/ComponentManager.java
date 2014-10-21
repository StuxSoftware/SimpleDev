package net.stuxcrystal.commandhandler.component;

import net.stuxcrystal.commandhandler.CommandHandler;
import net.stuxcrystal.commandhandler.utils.HandleWrapper;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * A manager for components.
 */
public class ComponentManager {

    /**
     * Components of the command handler.
     */
    private List<ComponentMethod> methods = new ArrayList<>();

    /**
     * Actual registered classes.
     */
    private HashSet<Class<? extends ComponentContainer>> classes = new HashSet<>();

    /**
     * The command handler this manager belongs to.
     */
    private final CommandHandler handler;

    /**
     * Creates a new instance of the component manager.
     * @param handler The handler that belongs to the component manager.
     */
    public ComponentManager(CommandHandler handler) {
        this.handler = handler;
    }

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
    @SuppressWarnings("unchecked")
    public <T> T call(String name, HandleWrapper self, Object[] params) throws Throwable {
        // Find suitable method for handler wrapper.
        ComponentMethod method = null;

        component_iterator:
        for (ComponentMethod m : this.methods) {
            // Check name.
            if (!m.getName().equals(name))
                continue;

            // Check self parameter.
            if (!m.getSelfParameter().isInstance(self))
                continue;

            Class<?>[] types = m.getParameters();
            // Check parameter length.
            if (params.length != types.length)
                continue;

            // Check instances.
            for (int i = 0; i<types.length; i++) {
                if (!types[i].isInstance(params[i]))
                    continue component_iterator;
            }

            method = m;
            break;
        }

        // If none was found, throw IllegalArgumentException.
        if (method == null)
            throw new IllegalArgumentException("Unknown extension method.");

        return (T)this.call(method, self, params);
    }

    /**
     * Checks if the method exists.
     *
     * @param name          The name of the extension function.
     * @param wrapper       The wrapper.
     * @param paramTypes    The param types.
     * @return {@code true} if so.
     */
    public boolean hasMethod(String name, Class<? extends HandleWrapper> wrapper, Class<?>... paramTypes) {
        component_iterator:
        for (ComponentMethod m : this.methods) {
            // Check name.
            if (!m.getName().equals(name))
                continue;

            // Check self parameter.
            if (!m.getSelfParameter().isAssignableFrom(wrapper))
                continue;

            Class<?>[] types = m.getParameters();
            // Check parameter length.
            if (paramTypes.length != types.length)
                continue;

            // Check instances.
            for (int i = 0; i<types.length; i++) {
                if (!types[i].isAssignableFrom(paramTypes[i]))
                    continue component_iterator;
            }

            return true;
        }

        return false;
    }

    /**
     * Actually calls the method.
     * @param method The method that has been called.
     * @param params The params that were passed.
     * @return The result of the call. (Or a future in case of asynchronous functions)
     */
    @SuppressWarnings({"rawtype", "unchecked"})
    private Object call(ComponentMethod method, Object self, Object[] params) throws Throwable {
        // If we don't care if we're in another thread, we will just call the method.
        if (method.component.syncstate() == SynchronizationState.IGNORE)
            return method.call(self, params);

        ComponentExecutor executor = new ComponentExecutor(method, self, params);

        if (method.component.syncstate() == SynchronizationState.SYNCHRONOUS) {
            // If the task is already running in the main thread, just call the method.
            if (this.handler.getServerBackend().inMainThread())
                return executor.call();

            FutureTask future = new FutureTask(executor);

            // Call the method.
            this.handler.getServerBackend().scheduleSync(future);

            // Await the result.
            try {
                return future.get();
            } catch (ExecutionException e) {
                // Rethrow the old exception.
                throw e.getCause();
            }
        } else {
            // Just execute the task asynchronously.
            FutureTask future = new FutureTask(executor);
            this.handler.getServerBackend().scheduleAsync(future);
            return future;
        }
    }
}
