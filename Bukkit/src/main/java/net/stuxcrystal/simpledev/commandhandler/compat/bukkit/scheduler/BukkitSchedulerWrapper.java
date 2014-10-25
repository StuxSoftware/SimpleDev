package net.stuxcrystal.simpledev.commandhandler.compat.bukkit.scheduler;

import net.stuxcrystal.simpledev.commandhandler.CommandBackend;
import net.stuxcrystal.simpledev.commandhandler.compat.bukkit.BukkitPluginBackend;
import net.stuxcrystal.simpledev.commandhandler.component.Component;
import net.stuxcrystal.simpledev.commandhandler.contrib.scheduler.SchedulerImplementation;
import net.stuxcrystal.simpledev.commandhandler.contrib.scheduler.Task;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;

/**
 * Wrapper for schedulers.
 */
public class BukkitSchedulerWrapper implements SchedulerImplementation {

    /**
     * Reference to the backend.
     */
    private final BukkitPluginBackend bpb;

    /**
     * The wrapper for the scheduler.
     * @param bpb The backend.
     */
    public BukkitSchedulerWrapper(BukkitPluginBackend bpb) {
        this.bpb = bpb;
    }

    @Override
    @Component
    public void __scheduler_exists() {

    }

    @Override
    @Component
    public Task runTaskAsync(CommandBackend backend, Runnable runnable) {
        BukkitTaskWrapper btw = new BukkitTaskWrapper();
        btw.setId(
                Bukkit.getScheduler().runTaskAsynchronously(
                        (Plugin) backend.getHandle(), new BukkitTaskRunner(runnable, btw)
                ).getTaskId()
        );
        return btw;
    }

    @Override
    @Component
    public Task runTask(CommandBackend backend, Runnable runnable) {
        BukkitTaskWrapper btw = new BukkitTaskWrapper();
        btw.setId(
                Bukkit.getScheduler().runTask(
                        (Plugin) backend.getHandle(), new BukkitTaskRunner(runnable, btw)
                ).getTaskId()
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

    @Override
    @Component
    public Task scheduleTask(CommandBackend backend, Runnable runnable, int delay, int repeat, boolean async) {
        BukkitTaskWrapper btw = new BukkitTaskWrapper();

        // Convert delay and repeat.
        repeat /= 50;
        delay /= 50;

        BukkitScheduler bs = Bukkit.getScheduler();
        int id;
        if (async) {
            if (repeat == 0) {
               id = bs.runTaskLaterAsynchronously(this.bpb.getHandle(), new BukkitTaskRunner(runnable, btw), delay).getTaskId();
            } else {
                id = bs.runTaskTimerAsynchronously(this.bpb.getHandle(), new BukkitTaskRunner(runnable, btw), delay, repeat).getTaskId();
            }
        } else {
            if (repeat == 0) {
                id = bs.runTaskLater(this.bpb.getHandle(), new BukkitTaskRunner(runnable, btw), delay).getTaskId();
            } else {
                id = bs.runTaskTimer(this.bpb.getHandle(), new BukkitTaskRunner(runnable, btw), delay, repeat).getTaskId();
            }
        }
        btw.setId(id);

        return btw;
    }
}
