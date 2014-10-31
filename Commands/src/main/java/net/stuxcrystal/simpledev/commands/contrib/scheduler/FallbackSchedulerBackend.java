package net.stuxcrystal.simpledev.commands.contrib.scheduler;

import net.stuxcrystal.simpledev.commands.CommandHandler;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>The fallback scheduler</p>
 * <p>
 *     We use our own thread pool now. To set the amount of threads
 *     to use in the threadpool, use -Dnet.stuxcrystal.simpledev.defaultscheduler.size=&lt;Your Desired Size&gt;
 * </p>
 */
public class FallbackSchedulerBackend {

    /**
     * This class identifies the main thread by attaching an UUID to each thread and comparing it with the
     * UUID of the main thread.
     */
    private static class MainThreadHelper {

        /**
         * The container for the thread-locale.
         */
        private static final ThreadLocal<UUID> container = new ThreadLocal<>();

        /**
         * The uuid of the main thread.
         */
        private static UUID mainThreadUUID = null;

        /**
         * Sets the main thread.
         */
        public static void setMainThread() {
            if (mainThreadUUID != null)
                throw new IllegalStateException("There is already a main thread.");
            mainThreadUUID = MainThreadHelper.getThreadUUID();
        }

        /**
         * Get the UUID of the current thread.
         * @return The UUID of the current thread.
         */
        private static UUID getThreadUUID() {
            UUID uuid = MainThreadHelper.container.get();
            if (uuid == null)
                MainThreadHelper.container.set(uuid = UUID.randomUUID());
            return uuid;
        }

        /**
         * Checks if the current thread is the main thread.
         * @return {@code true} if so.
         */
        public static boolean isMainThread() {
            if (MainThreadHelper.mainThreadUUID == null)
                return false;
            return MainThreadHelper.mainThreadUUID.equals(MainThreadHelper.getThreadUUID());
        }
    }

    /**
     * The size of the thread-pool. If the size is 0, we will always use a thread-pool.
     */
    private static final int THREAD_POOL_SIZE = 0;

    /**
     * Has the warning message been shown?
     */
    private boolean shown = false;

    /**
     * The thread-pool that will be used.
     */
    private ExecutorService service;

    /**
     * Creates the fallback scheduler thread.
     */
    public FallbackSchedulerBackend() {
        MainThreadHelper.setMainThread();
        this.service = this.getThreadPool();
    }

    /**
     * Creates the thread-pool.
     * @return The thread-pool for our work.
     */
    public ExecutorService getThreadPool() {
        int size = FallbackSchedulerBackend.getThreadPoolSize();
        if (size > 0)
            return Executors.newFixedThreadPool(FallbackSchedulerBackend.getThreadPoolSize());
        else
            return Executors.newCachedThreadPool();
    }

    /**
     * Returns the size of the default thread-pool.
     * @return The size of the default thread-pool.
     */
    private static int getThreadPoolSize() {
        String size = System.getProperty(
                "net.stuxcrystal.simpledev.defaultscheduler.size",
                ""+FallbackSchedulerBackend.THREAD_POOL_SIZE
        );

        int sz;
        try {
            sz = Integer.valueOf(size);
        } catch (NumberFormatException e) {
            return FallbackSchedulerBackend.THREAD_POOL_SIZE;
        }

        if (sz < 0)
            return FallbackSchedulerBackend.THREAD_POOL_SIZE;

        return sz;
    }

    /**
     * Schedule a new thread into the thread-pool.
     * @param handler   The command-handler that will be used.
     * @param runnable  The scheduler that will be running the given object.
     */
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

        this.service.submit(runnable);
    }

    /**
     * Checks if we are currently running in the mainThreadUUID thread.
     * @return The the mainThreadUUID-thread.
     */
    public boolean isMainThread() {
        return MainThreadHelper.isMainThread();
    }
}
