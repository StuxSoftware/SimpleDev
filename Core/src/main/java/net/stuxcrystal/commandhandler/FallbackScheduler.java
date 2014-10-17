package net.stuxcrystal.commandhandler;

/**
 * Represents a fallback scheduler.
 */
public class FallbackScheduler {

    /**
     * The backend that issues the warning.
     */
    private final CommandHandler backend;

    /**
     * Has the user already been warned?
     */
    private boolean warned = false;

    /**
     * Creates a new fallback scheduler.
     * @param backend The actual backend.
     */
    public FallbackScheduler(CommandHandler backend) {
        this.backend = backend;
    }

    /**
     * Schedules a new asynchronous task.
     * @param runnable The runnable to run.
     */
    public void schedule(Runnable runnable) {
        if (!this.warned) {
            this.backend.getServerBackend().getLogger().warning(
                    this.backend.getTranslationManager().translate(
                            this.backend.getServerBackend().getConsole(), "internal.threading.no-scheduler"
                    )
            );
            this.warned = true;
        }
        new Thread(runnable).start();
    }
}
