package net.stuxcrystal.simpledev.commands.contrib.scheduler.fallback;

import net.stuxcrystal.simpledev.commands.CommandHandler;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Manages tasks for the command handler.</p>
 *
 * <p>
 *     Please note that the task queue actually depends on the Root-{@link net.stuxcrystal.simpledev.commands.CommandHandler}
 *     being garbage collected because once it's finalized, it will <b>not</b> continue running the
 *     tasks handled by the task queue.
 * </p>
 *
 * <p>
 *     To make sure the CommandHandler will be collected after the plugin is being disabled
 *     set the
 * </p>
 */
public class TaskQueue extends Thread {

    /**
     * Executes the task.
     */
    private List<BasicTask> tasks = new ArrayList<>();

    /**
     * <p>Contains a weak reference to a command handler.</p>
     * <p>Weak to ensure that when the commandhandler does not exist anymore, the thread stops.</p>
     */
    private WeakReference<CommandHandler> owner;

    /**
     * Creates the new task manager.
     * @param handler The handler that executes these tasks.
     */
    public TaskQueue(CommandHandler handler) {
        this.owner = new WeakReference<>(handler);
        this.setDaemon(true);
    }

    /**
     * Returns the command-handler that actually handles the commands.
     * @return {@code null} if the handler has already been garbage collected.
     */
    public CommandHandler getCommandHandler() {
        return this.owner.get();
    }

    /**
     * Register a new task.
     * @param task The task to register.
     */
    public void addTask(BasicTask task) {
        this.tasks.add(task);
    }

    public void run() {
        // While the CommandHandler exists...
        while (this.owner.get() != null) {
            List<Integer> toRemove = new ArrayList<>();

            long fTime = 50;

            // Check if we want to execute this task.
            long cTime = System.currentTimeMillis();

            BasicTask task;
            for (int i = 0; i<this.tasks.size(); i++) {
                // Gets the task.
                task = this.tasks.get(i);

                // Check if we don't need the task anymore.
                if (task.isCancelled() || task.isCompleted()) {
                    toRemove.add(i);
                    continue;
                }

                long waitTime = task.getNextExecutionTime() - cTime;
                if (waitTime >= 0) {
                    // If the next task is scheduled to executed
                    if (fTime > waitTime)
                        fTime = waitTime;
                    continue;
                }

                // Executes the task.
                task.execute(this.owner.get());
            }

            // Remove completed tasks.
            for (Integer tId : toRemove) {
                this.tasks.remove((int)tId);
            }

            // Make sure we allow other threads to work.
            Thread.yield();
            try {
                // Put the thread to sleep.
                Thread.sleep(fTime);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

}
