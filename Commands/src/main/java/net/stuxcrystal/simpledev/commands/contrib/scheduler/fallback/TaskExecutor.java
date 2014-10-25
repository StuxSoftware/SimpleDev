package net.stuxcrystal.simpledev.commands.contrib.scheduler.fallback;

import net.stuxcrystal.simpledev.commands.CommandHandler;

/**
 * Creates a new task.
 */
public class TaskExecutor implements Runnable {

    /**
     * Contains the task to execute.
     */
    private final BasicTask task;

    /**
     * The handler that executes the task.
     */
    private final CommandHandler handler;

    /**
     * Creates a new task executor.
     * @param task    The task executor.
     * @param handler The handler that executes the task.
     */
    public TaskExecutor(BasicTask task, CommandHandler handler) {
        this.task = task;
        this.handler = handler;
    }

    /**
     * Runs the task.
     */
    @Override
    public void run() {
        this.task.execute(this.handler);
    }
}
