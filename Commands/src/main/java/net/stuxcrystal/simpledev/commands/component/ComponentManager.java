package net.stuxcrystal.simpledev.commands.component;

import net.stuxcrystal.simpledev.commands.CommandHandler;
import net.stuxcrystal.simpledev.commands.utils.HandleWrapper;

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
     * Contains all methods.
     */
    private LinkedHashMap<Method, ComponentMethod> methods = new LinkedHashMap<>();

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
            // Make sure we didn't already check the method.
            if (this.methods.containsKey(method))
                continue;

            // Make sure we can access the method.
            if (!method.isAccessible())
                method.setAccessible(true);

            // Make sure the method is not abstract.
            if (Modifier.isAbstract(method.getModifiers()))
                continue;

            // Make sure we can actually call the method.
            if (!Modifier.isStatic(method.getModifiers()) && component==null)
                continue;

            // Check if the method is actually a component class.
            if (!method.isAnnotationPresent(Component.class))
                continue;

            // Get parameters.
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

            // Add the method.
            this.methods.put(method, new ComponentMethod(method, component, method.getAnnotation(Component.class)));
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
        // Resolve the parameters.
        Class<?>[] types = new Class[params.length];
        for (int i = 0; i<types.length; i++)
            types[i] = params.getClass();

        // Find most specific method for handler wrapper.
        ComponentMethod method = this.getMethod(true, name, self.getClass(), types);

        // If none was found, throw IllegalArgumentException.
        if (method == null)
            throw new IllegalArgumentException("Unknown method.");

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
        return this.getMethod(false, name, wrapper, paramTypes) != null;
    }

    /**
     * <p>Returns the method we are searching for.</p>
     * <p>
     *     If resolve is true, we will return the most specific method that holds for these parameter types.
     *     Please note that this will drastically slow down method resolution.
     * </p>
     *
     * @param resolve       Just try to resolve the method.
     * @param name          The name of the extension function.
     * @param wrapper       The wrapper.
     * @param paramTypes    The param types.
     * @return The actual method that should be called.
     */
    private ComponentMethod getMethod(boolean resolve, String name, Class<? extends HandleWrapper> wrapper, Class<?>... paramTypes) {
        // The result method.
        HashSet<ComponentMethod> result = new HashSet<>();

        // Check the
        component_iterator:
        for (ComponentMethod m : this.methods.values()) {
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

            // If we don't wanna find the method with the best suitable arguments,
            if (!resolve)
                return m;
            else
                result.add(m);
        }

        // Return null if we found nothing
        if (result.size() == 0)
            return null;

        // Return the only method left
        if (result.size() == 1)
            return result.toArray(new ComponentMethod[1])[0];

        // Start to get the most specific method.
        boolean methodDiscarded = true;
        while (methodDiscarded) {
            methodDiscarded = false;

            // The current list of methods.
            HashSet<ComponentMethod> current = new HashSet<>(result);
            for (ComponentMethod upper : current) {
                // The method has already been discarded.
                if (!result.contains(upper))
                    continue;

                Class<?>[] upperParams = upper.getParameters();

                // Iterate over the lover parameters.
                lower_it:
                for (ComponentMethod lower : current) {
                    // Do not check the same method.
                    if (upper == lower)
                        continue;

                    // The method has already been discarded.
                    if (!result.contains(lower))
                        continue;

                    Class<?>[] lowerParams = lower.getParameters();

                    for (int i = 0; i<upperParams.length; i++) {
                        // Ignore parameters with the same parameter types.
                        if (lowerParams[i].equals(upperParams[i]))
                            continue;

                        // If this function is more specific than the upper one,
                        // do not remove this function.
                        if (!lowerParams[i].isAssignableFrom(upperParams[i]))
                            break;

                        // If this is not the most specific function, discard the function.
                        result.remove(lower);
                        methodDiscarded = true;

                        // And stop iterating over the parameters.
                        break lower_it;
                    }
                }
            }
        }

        ComponentMethod cm = null;

        // Slow version: Determine the most specific method with the self parameter.
        if (result.size() > 1) {
            for (ComponentMethod method : result) {
                // First method.
                if (cm == null)
                    cm = method;

                    // Get the more specific of the two methods.
                else if (cm.getSelfParameter().isAssignableFrom(method.getSelfParameter()))
                    cm = method;
            }

            // cm cannot be null.
            assert cm != null;

            for (ComponentMethod method : result) {
                if (method == cm)
                    continue;
                if (cm.getSelfParameter().equals(method.getSelfParameter()))
                    throw new IllegalArgumentException("Ambiguous methods...");
            }

        // Fast: There is only one, we won't try to determine the actual method.
        } else {
            cm = result.toArray(new ComponentMethod[1])[0];
        }

        return cm;
    }

    /**
     * Actually calls the method.
     * @param method The method that has been called.
     * @param params The params that were passed.
     * @return The result of the call.
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
