package net.stuxcrystal.commandhandler.compat.bungee.scheduler;

import net.stuxcrystal.commandhandler.CommandBackend;
import net.stuxcrystal.commandhandler.compat.bungee.BungeePluginBackend;
import net.stuxcrystal.commandhandler.component.Component;
import net.stuxcrystal.commandhandler.contrib.scheduler.SchedulerImplementation;
import net.stuxcrystal.commandhandler.contrib.scheduler.Task;

import java.util.concurrent.TimeUnit;

/**
 * Binding for the scheduler in BungeeCord.
 */
public class BungeeSchedulerWrapper implements SchedulerImplementation {

    /**
     * Contains the bungee-plugin backend.
     */
    private final BungeePluginBackend bpb;

    /**
     * The wrapper.
     * @param bpb The backend.
     */
    public BungeeSchedulerWrapper(BungeePluginBackend bpb) {
        this.bpb = bpb;
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

    @Override
    public Task scheduleTask(CommandBackend backend, Runnable runnable, int delay, int repeat, boolean async) {
        BungeeTaskWrapper btw = new BungeeTaskWrapper();
        btw.task = bpb.getHandle().getProxy().getScheduler().schedule(
                bpb.getHandle(),
                new BungeeTaskRunner(runnable, btw),
                delay, repeat, TimeUnit.MILLISECONDS
        );
        return btw;
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

    @Override
    public void __scheduler_exists() {

    }

    @Override
    public Task runTaskAsync(CommandBackend backend, Runnable runnable) {
        return this.scheduleTask(backend, runnable, 0, 0, true);
    }

    @Override
    public Task runTask(CommandBackend backend, Runnable runnable) {
        return this.scheduleTask(backend, runnable, 0, 0, false);
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
}
