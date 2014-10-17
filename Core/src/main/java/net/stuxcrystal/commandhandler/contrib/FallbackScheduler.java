package net.stuxcrystal.commandhandler.contrib;

import net.stuxcrystal.commandhandler.CommandHandler;

/**
 * The fallback scheduler
 */
public class FallbackScheduler {

    private final CommandHandler handler;

    private boolean shown = false;

    public FallbackScheduler(CommandHandler handler) {
        this.handler = handler;
    }

    public void schedule(Runnable runnable) {
        if (!shown) {
            this.handler.getServerBackend().getLogger().warning(
                    this.handler.getTranslationManager().translate(
                            this.handler.getServerBackend().getConsole(),
                            "internal.threading.no-scheduler"
                    )
            );
            shown = true;
        }

        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();
    }
}
