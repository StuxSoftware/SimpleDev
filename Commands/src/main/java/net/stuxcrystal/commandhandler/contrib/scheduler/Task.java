package net.stuxcrystal.commandhandler.contrib.scheduler;

/**
 * Base class for a task.
 */
public interface Task {

    /**
     * Returns the period between each execution.
     * @return The period between each execution.
     */
    public int getRepeatPeriod();

    /**
     * Cancels the task.
     */
    public void cancel();

    /**
     * Checks if the task has been cancelled.
     * @return {@code true} if the task has been completed.
     */
    public boolean isCancelled();

    /**
     * Checks if the task is currently completed.
     * @return {@code true} if the task is completed.
     */
    public boolean isCompleted();

}
