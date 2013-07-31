package net.stuxcrystal.commandhandler;

import java.util.logging.Logger;

/**
 * Backend of the command handler. Used to communicate with the program.
 *
 * @param <T> The type of the backend.
 */
public interface Backend<T> {

    /**
     * Called if the logger has to be used.
     *
     * @return Reference to the logger.
     */
    public Logger getLogger();

    /**
     * Schedules an asynchronous task.
     *
     * @param runnable
     */
    public void schedule(Runnable runnable);

    /**
     * Returns an CommandExecutor by the given name.
     *
     * @param name The name of the executor. If null or empty, the console executor will be returned.
     */
    public CommandExecutor<?> getExecutor(String name);

    /**
     * @return The handle.
     */
    public T getHandle();

}
