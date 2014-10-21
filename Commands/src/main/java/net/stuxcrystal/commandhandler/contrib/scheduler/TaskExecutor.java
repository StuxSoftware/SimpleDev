package net.stuxcrystal.commandhandler.contrib.scheduler;

import net.stuxcrystal.commandhandler.CommandHandler;

/**
 * Creates a new task.
 */
public class TaskExecutor implements Runnable {

    /**
     * Contains the task to execute.
     */
    private final Task task;

    /**
     * The handler that executes the task.
     */
    private final CommandHandler handler;

    /**
     * Creates a new task executor.
     * @param task    The task executor.
     * @param handler The handler that executes the task.
     */
    public TaskExecutor(Task task, CommandHandler handler) {
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
