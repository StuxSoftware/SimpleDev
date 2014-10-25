package net.stuxcrystal.simpledev.commandhandler.compat.bungee.scheduler;

import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.stuxcrystal.simpledev.commandhandler.contrib.scheduler.Task;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Wrapper for bungeecord tasks.
 */
public class BungeeTaskWrapper implements Task {

    AtomicBoolean completed = new AtomicBoolean(false);

    private AtomicBoolean cancelled = new AtomicBoolean(false);

    ScheduledTask task;

    public BungeeTaskWrapper() {
        this.task = task;
    }

    @Override
    public void cancel() {
        this.task.cancel();
        this.cancelled.set(true);
    }

    @Override
    public boolean isCancelled() {
        return this.cancelled.get();
    }

    @Override
    public boolean isCompleted() {
        return this.completed.get();
    }
}
