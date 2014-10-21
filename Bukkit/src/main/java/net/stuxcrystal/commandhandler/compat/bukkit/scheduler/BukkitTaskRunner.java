package net.stuxcrystal.commandhandler.compat.bukkit.scheduler;

import org.bukkit.Bukkit;

/**
 * The runner for tasks.
 */
public class BukkitTaskRunner implements Runnable {

    private final Runnable child;

    private BukkitTaskWrapper btw;

    public BukkitTaskRunner(Runnable child, BukkitTaskWrapper btw) {
        this.child = child;
        this.btw = btw;
    }

    @Override
    public void run() {
        this.child.run();
        this.btw.completed.set(true);
    }
}
