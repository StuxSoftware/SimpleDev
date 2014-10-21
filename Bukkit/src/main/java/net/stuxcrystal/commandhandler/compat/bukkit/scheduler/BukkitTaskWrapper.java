package net.stuxcrystal.commandhandler.compat.bukkit.scheduler;

import net.stuxcrystal.commandhandler.contrib.scheduler.Task;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Wraps a task in bukkit.
 */
public class BukkitTaskWrapper implements Task {

    AtomicBoolean completed = new AtomicBoolean(false);

    private AtomicBoolean cancelled = new AtomicBoolean(false);

    private int id;

    public BukkitTaskWrapper() {

    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public void cancel() {
        Bukkit.getScheduler().cancelTask(this.id);
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
