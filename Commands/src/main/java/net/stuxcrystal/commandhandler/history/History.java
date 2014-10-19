package net.stuxcrystal.commandhandler.history;

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

}
