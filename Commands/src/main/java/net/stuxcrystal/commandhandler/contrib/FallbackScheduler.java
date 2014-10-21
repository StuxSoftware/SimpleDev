package net.stuxcrystal.commandhandler.contrib;

import net.stuxcrystal.commandhandler.CommandHandler;

/**
 * The fallback scheduler
 */
public class FallbackScheduler {

    private boolean shown = false;

    public FallbackScheduler() {}

    public void schedule(CommandHandler handler, Runnable runnable) {
        if (!shown) {
            handler.getServerBackend().getLogger().warning(
                    handler.getTranslationManager().translate(
                            handler.getServerBackend().getConsole(),
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
