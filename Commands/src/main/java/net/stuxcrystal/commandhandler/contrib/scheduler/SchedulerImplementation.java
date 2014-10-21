package net.stuxcrystal.commandhandler.contrib.scheduler;

import net.stuxcrystal.commandhandler.CommandBackend;
import net.stuxcrystal.commandhandler.component.ComponentContainer;

/**
 * Defines an implementation for a scheduler.
 */
public interface SchedulerImplementation extends ComponentContainer {

    /**
     * <p>Internal function to determine if a scheduler has been added to the system.</p>
     * <p>
     *     Just leave the method body empty.
     * </p>
     */
    public void __scheduler_exists();

    /**
     * Just runs a task in the main thread.
     * @param backend  The backend the task runs on.
     * @param runnable The runnable to schedule.
     * @return A new task object.
     */
    public Task runTaskAsync(CommandBackend backend, Runnable runnable);

    /**
     * Just runs a task.
     * @param backend  The backend the task runs on.
     * @param runnable The runnable to schedule.
     * @return A new task object.
     */
    public Task runTask(CommandBackend backend, Runnable runnable);

    /**
     * Schedules a new asynchronous task.
     * @param backend   The backend the task runs on.
     * @param runnable  The runnable to schedule.
     * @param period    The period to wait.
     * @return The task.
     */
    public Task scheduleRepetitiveAsynchronousTask(CommandBackend backend, Runnable runnable, int period);

    /**
     * Schedules a new repetitive task.
     *
     * @param backend   The backend the task runs on.
     * @param runnable  The runnable to schedule.
     * @param period    The time each execution will be waited.
     * @return The task.
     */
    public Task scheduleRepetitiveTask(CommandBackend backend, Runnable runnable, int period);

    /**
     * Schedules a new asynchronous task.
     * @param backend   The backend the task runs on.
     * @param runnable  The runnable to schedule.
     * @param period    The period to wait.
     * @return The task.
     */
    public Task scheduleRepetitiveAsynchronousTask(CommandBackend backend, Runnable runnable, int delay, int period);

    /**
     * Schedules a new repetitive task.
     *
     * @param backend   The backend the task runs on.
     * @param runnable  The runnable to schedule.
     * @param period    The time each execution will be waited.
     * @return The task.
     */
    public Task scheduleRepetitiveTask(CommandBackend backend, Runnable runnable, int delay, int period);

    /**
     * Schedules a new synchronous task.
     * @param backend   The backend the task runs on.
     * @param runnable  The runnable to schedule.
     * @param delay     The time to wait in milliseconds.
     * @return The task.
     */
    public Task scheduleTaskAsynchronously(CommandBackend backend, Runnable runnable, int delay);

    /**
     * Schedules a new synchronous task.
     * @param backend   The backend the task runs on.
     * @param runnable  The runnable to schedule.
     * @param delay     The time to wait in milliseconds.
     * @return The task.
     */
    public Task scheduleTask(CommandBackend backend, Runnable runnable, int delay);

    /**
     * Schedules a new task.
     * @param backend   The backend the task runs on.
     * @param runnable  The runnable to schedule.
     * @param delay     The time to wait in milliseconds.
     * @param repeat    The repetition period (0 means it won't be repeated)
     * @param async     {@code true} and it runs in it's own thread | {@code false} and it runs in the main thread.
     * @return The task.
     */
    public Task scheduleTask(CommandBackend backend, Runnable runnable, int delay, int repeat, boolean async);
}
