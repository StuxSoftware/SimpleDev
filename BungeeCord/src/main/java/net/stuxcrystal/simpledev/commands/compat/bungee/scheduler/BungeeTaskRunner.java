package net.stuxcrystal.simpledev.commands.compat.bungee.scheduler;

/**
 * The runner for Bungee-Tasks.
 */
public class BungeeTaskRunner implements Runnable {

    private final Runnable runnable;

    private final BungeeTaskWrapper btw;

    public BungeeTaskRunner(Runnable runnable, BungeeTaskWrapper btw) {
        this.runnable = runnable;
        this.btw = btw;
    }

    @Override
    public void run() {
        this.runnable.run();
        this.btw.completed.set(true);
    }
}
