package net.stuxcrystal.simpledev.commands.contrib.scheduler.fallback;

import net.stuxcrystal.simpledev.commands.CommandBackend;
import net.stuxcrystal.simpledev.commands.CommandHandler;
import net.stuxcrystal.simpledev.commands.component.Component;
import net.stuxcrystal.simpledev.commands.component.ComponentContainer;
import net.stuxcrystal.simpledev.commands.contrib.scheduler.SchedulerImplementation;
import net.stuxcrystal.simpledev.commands.contrib.scheduler.Task;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

/**
 * <p>Platform independent implementation of the Scheduler.</p>
 *
 * <p>
 *     Plase note that this implementation is not exactly accurate and should not be
 *     used when accuracy is of primary importance to the task you use.
 * </p>
 */
public class TaskComponent implements ComponentContainer, SchedulerImplementation {

    /**
     * The task queue created lazily.
     */
    private ScheduledExecutorService queue;

    /**
     * Creates a new task-component.
     */
    public TaskComponent() {
        this.queue = Executors.newSingleThreadScheduledExecutor();
    }
    /**
     * Schedules a new task.
     * @param backend   The backend the task runs on.
     * @param runnable  The runnable to schedule.
     * @param delay     The time to wait in milliseconds.
     * @param repeat    The repetition period (0 means it won't be repeated)
     * @param async     {@code true} and it runs in it's own thread | {@code false} and it runs in the main thread.
     * @return The task.
     */
    @Component
    public synchronized Task scheduleTask(CommandBackend backend, Runnable runnable, int delay, int repeat, boolean async) {
        BasicTask task = new BasicTask(runnable, async, repeat);
        if (repeat == 0) {
            this.queue.schedule(task.getRunnable(backend), delay, TimeUnit.MILLISECONDS);
        } else {
            this.queue.scheduleAtFixedRate(task.getRunnable(backend), delay, repeat, TimeUnit.MILLISECONDS);
        }
        return task;
    }

    /**
     * Schedules a new synchronous task.
     * @param backend   The backend the task runs on.
     * @param runnable  The runnable to schedule.
     * @param delay     The time to wait in milliseconds.
     * @return The task.
     */
    @Component
    public Task scheduleTask(CommandBackend backend, Runnable runnable, int delay) {
        return this.scheduleTask(backend, runnable, delay, 0, false);
    }

    /**
     * Schedules a new synchronous task.
     * @param backend   The backend the task runs on.
     * @param runnable  The runnable to schedule.
     * @param delay     The time to wait in milliseconds.
     * @return The task.
     */
    @Component
    public Task scheduleTaskAsynchronously(CommandBackend backend, Runnable runnable, int delay) {
        return this.scheduleTask(backend, runnable, delay, 0, true);
    }

    /**
     * Schedules a new repetitive task.
     *
     * @param backend   The backend the task runs on.
     * @param runnable  The runnable to schedule.
     * @param period    The time each execution will be waited.
     * @return The task.
     */
    @Component
    public Task scheduleRepetitiveTask(CommandBackend backend, Runnable runnable, int delay, int period) {
        return this.scheduleTask(backend, runnable, delay, period, false);
    }

    /**
     * Schedules a new asynchronous task.
     * @param backend   The backend the task runs on.
     * @param runnable  The runnable to schedule.
     * @param period    The period to wait.
     * @return The task.
     */
    @Component
    public Task scheduleRepetitiveAsynchronousTask(CommandBackend backend, Runnable runnable, int delay, int period) {
        return this.scheduleTask(backend, runnable, delay, period, true);
    }

    /**
     * Schedules a new repetitive task.
     *
     * @param backend   The backend the task runs on.
     * @param runnable  The runnable to schedule.
     * @param period    The time each execution will be waited.
     * @return The task.
     */
    @Component
    public Task scheduleRepetitiveTask(CommandBackend backend, Runnable runnable, int period) {
        return this.scheduleTask(backend, runnable, 0, period, false);
    }

    /**
     * Schedules a new asynchronous task.
     * @param backend   The backend the task runs on.
     * @param runnable  The runnable to schedule.
     * @param period    The period to wait.
     * @return The task.
     */
    @Component
    public Task scheduleRepetitiveAsynchronousTask(CommandBackend backend, Runnable runnable, int period) {
        return this.scheduleTask(backend, runnable, 0, period, true);
    }

    /**
     * Just runs a task synchronized.
     * @param backend  The backend the task runs on.
     * @param runnable The runnable to schedule.
     * @return A new task object.
     */
    @Component
    public Task runTask(CommandBackend backend, Runnable runnable) {
        BasicTask task = new BasicTask(runnable, null, 0);

        // Make our life simpler by directly executing the task when in main thread.
        if (!backend.inMainThread())
            backend.scheduleSync(task.getRunnable(backend));
        else {
            try {
                task.execute(backend.getCommandHandler());
            } catch (Throwable e) {
                backend.getLogger().log(Level.WARNING, "Error in task.", e);
            }
        }

        return task;
    }

    /**
     * Just runs a task in the main thread.
     * @param backend  The backend the task runs on.
     * @param runnable The runnable to schedule.
     * @return A new task object.
     */
    @Component
    public Task runTaskAsync(CommandBackend backend, Runnable runnable) {
        BasicTask task = new BasicTask(runnable, null, 0);
        backend.scheduleAsync(task.getRunnable(backend));
        return task;
    }

    /**
     * Internal function to determine if a scheduler has been added to the system.
     */
    @Component
    public void __scheduler_exists() {}
}
