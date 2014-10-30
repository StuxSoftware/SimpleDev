package net.stuxcrystal.simpledev.commands.contrib.scheduler.fallback;

import net.stuxcrystal.simpledev.commands.CommandBackend;
import net.stuxcrystal.simpledev.commands.CommandHandler;
import net.stuxcrystal.simpledev.commands.contrib.scheduler.Task;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;

/**
 * Defines a task.
 */
public class BasicTask implements Task {

    /**
     * Runnable that will be passed to the scheduler.
     */
    private class TaskRunner implements Runnable {

        /**
         * The backend for commands.
         */
        private final CommandBackend backend;

        /**
         * Creates the backend.
         * @param backend The backend.
         */
        private TaskRunner(CommandBackend backend) {
            this.backend = backend;
        }

        /**
         * Runs the task.
         */
        @Override
        public void run() {
            BasicTask.this.execute(this.backend.getCommandHandler().getRootCommandHandler());
        }
    }

    /**
     * Internal bootstrapper for the task execution.
     */
    private class Runner implements Runnable {

        /**
         * The command backend for the task.
         */
        private final CommandBackend backend;

        private Runner(CommandBackend backend) {
            this.backend = backend;
        }

        @Override
        public void run() {
            BasicTask.this._execute(this.backend);
        }
    }

    /**
     * Sets the next execution time.
     */
    private AtomicLong nextExecutionTime = new AtomicLong(-1);

    /**
     * Contains the runnable to run.
     */
    private final Runnable runnable;

    /**
     * Contains {@code true} if the task has been cancelled.
     */
    private AtomicBoolean cancelled = new AtomicBoolean(false);

    /**
     * Contains {@code true} if the task has been executed.
     * Always {@code false} if the task is repeating...
     */
    private AtomicBoolean completed = new AtomicBoolean(false);

    /**
     * Stores if the task is asynchronously executed.
     */
    private final Boolean async;

    /**
     * {@code 0} if the task should repeat <b>not</b> itself.
     */
    private final int repeat;

    /**
     * Creates a new task.
     * @param runnable  The runnable that should be called.
     * @param async     Should the task be executed asynchronously?
     * @param repeat    The repeat period time.
     */
    public BasicTask(Runnable runnable, Boolean async, int repeat) {
        this.runnable = runnable;
        this.async = async;
        this.repeat = repeat;
    }

    /**
     * Execute the task.
     */
    void execute(CommandHandler handler) {
        // Prevent executing cancelled tasks.
        if (this.cancelled.get())
            return;

        // Make sure the task will be executed.
        if (this.async == null) {
            this._execute(handler.getServerBackend());
        } else if (this.async) {
            handler.getServerBackend().scheduleAsync(new Runner(handler.getServerBackend()));
        } else {
            handler.getServerBackend().scheduleSync(new Runner(handler.getServerBackend()));
        }
    }

    /**
     * Makes this task a runnable.
     * @param backend The backend to pass.
     * @return The runnable.
     */
    Runnable getRunnable(CommandBackend backend) {
        return new TaskRunner(backend);
    }

    /**
     * Actually executes the runnables.
     */
    private void _execute(CommandBackend backend) {
        // Executes the runnables.
        try {
            this.runnable.run();
        } catch (Throwable e) {
            backend.getLogger().log(Level.WARNING, "Error in task.", e);
        }

        this.taskCompleted();
    }

    /**
     * Returns the period between each execution.
     * @return The period between each execution.
     */
    public int getRepeatPeriod() {
        return this.repeat;
    }

    /**
     * Signals the task that it has been completed.
     */
    private void taskCompleted() {
        if (this.getRepeatPeriod() > 0)
            this.completed.set(true);
    }

    /**
     * Cancels the task.
     */
    public void cancel() {
        this.cancelled.set(true);
    }

    /**
     * Checks if the task has been cancelled.
     * @return {@code true} if the task has been completed.
     */
    public boolean isCancelled() {
        return this.cancelled.get();
    }

    /**
     * Checks if the task is currently completed.
     * @return {@code true} if the task is completed.
     */
    public boolean isCompleted() {
        return this.completed.get();
    }
}
