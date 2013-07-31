package net.stuxcrystal.commandhandler;

/**
 * A handler for exceptions.<p />
 * <p/>
 * Needed to allow the plugin to handle some exceptions itself.
 *
 * @param <T> The class this exception handles.
 */
public interface ExceptionHandler<T extends Throwable> {

    /**
     * If an exception is thrown that cannot be handled by the CommandHandler, an passable exception-handler will
     * be searched.
     *
     * @param exception The exception that was thrown.
     * @param name      The name of the command.
     * @param executor  The sender who executed the command.
     * @param arguments The arguments that were passed to the command.
     */
    public void exception(T exception, String name, CommandExecutor<?> executor, ArgumentParser arguments);

}
