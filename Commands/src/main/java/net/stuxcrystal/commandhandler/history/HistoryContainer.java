package net.stuxcrystal.commandhandler.history;

import net.stuxcrystal.commandhandler.Session;

/**
 * Defines a session that can undo and redo things.
 */
public class HistoryContainer extends Session {

    /**
     * Stores an action.
     * (Note: It uses the garbage collector to make sure no object is not collected.)
     */
    private class HistoryAction {

        /**
         * Reference to the action.
         */
        final Action action;

        /**
         * The previous action.
         */
        HistoryAction previous = null;

        /**
         * The next action.
         */
        HistoryAction next = null;

        /**
         * Creates a new link.
         * @param action The new action.
         */
        public HistoryAction(Action action) {
            this.action = action;
        }

    }

    /**
     * The first object in the history.
     */
    final private HistoryAction baseAction = new HistoryAction(null);

    /**
     * The current action.
     */
    private HistoryAction currentAction = baseAction;

    /**
     * The lock used to synchronize action accesses.
     */
    private final Object lock = new Object();

    /**
     * Make sure we can create a history container instance.
     */
    public HistoryContainer() {}

    /**
     * Executes the next action.<p />
     *
     * The action will be executed automatically.
     *
     * @param action The action to execute.
     */
    public void execute(Action action) {
        this.updateAccessTime();

        synchronized (this.lock) {
            HistoryAction newAction = new HistoryAction(action);
            currentAction.next = newAction;
            newAction.previous = currentAction;
            currentAction = newAction;
        }
        action.redo();
    }

    /**
     * Redo the next action.
     * @return false if there was no action to redo, true otherwise.
     */
    public boolean redo() {
        this.updateAccessTime();

        HistoryAction nextAction;
        synchronized (this.lock) {
            if (currentAction.next == null)
                return false;
            nextAction = currentAction.next;
            currentAction = nextAction;
        }
        nextAction.action.redo();
        return true;
    }

    /**
     * Undo the last action.
     * @return true if there was no action to undo, true otherwise.
     */
    public boolean undo() {
        this.updateAccessTime();

        HistoryAction lastAction;
        synchronized (this.lock) {
            lastAction = currentAction;
            if (lastAction.action == null)
                return false;
            currentAction = lastAction.previous;
        }
        lastAction.action.undo();
        return true;
    }


}
