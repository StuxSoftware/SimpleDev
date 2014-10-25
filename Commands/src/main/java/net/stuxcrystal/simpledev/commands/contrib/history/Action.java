package net.stuxcrystal.simpledev.commands.contrib.history;

import net.stuxcrystal.simpledev.commands.CommandExecutor;

/**
 * Defines an action that can be undone and redone.
 */
public abstract class Action {

    /**
     * The executor of the action.
     */
    private final CommandExecutor owner;

    /**
     * Initializes the action.
     * @param owner The owner of the action.
     */
    protected Action(CommandExecutor owner) {
        this.owner = owner;
    }

    /**
     * The executor of the action.
     * @return The executor of the action.
     */
    public CommandExecutor getOwner() {
        return this.owner;
    }

    /**
     * Called on the first execution of the action.<p />
     *
     * Default: Calls redoAction.
     */
    public void firstExecution() {
        this.redo();
    }

    /**
     * Undo the action.
     */
    public abstract void undo();

    /**
     * Redo the action.
     */
    public abstract void redo();

    /**
     * Returns the description for an action.
     * @param executor The executor that requests the description
     * @return The description.
     */
    public abstract String getDescription(CommandExecutor executor);

    /**
     * Returns the description of the action.
     * @return The description.
     */
    public String getDescription() {
        return this.getDescription(this.getOwner().getBackend().getConsole());
    }

}
