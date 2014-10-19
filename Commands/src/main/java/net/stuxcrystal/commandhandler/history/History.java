package net.stuxcrystal.commandhandler.history;

import java.util.List;

/**
 * Defines the history of the player.
 */
public interface History {

    /**
     * Undos the last action.
     */
    public boolean undo();

    /**
     * Redos the next action.
     */
    public boolean redo();

    /**
     * Execute this action.
     *
     * @param action The action to execute.
     */
    public void executeAction(Action action);

    /**
     * Get all actions in the history.
     * @return A list of all actions.
     */
    public List<Action> getActions();

    /**
     * The last executed action that has not been undone.
     * @return The last action.
     */
    public Action getLastAction();

    /**
     * Returns the next action that would have been executed
     * when {@link #redo()} is being executed.
     *
     * @return {@code null} if there is no action to redo.
     */
    public Action getNextAction();
}
