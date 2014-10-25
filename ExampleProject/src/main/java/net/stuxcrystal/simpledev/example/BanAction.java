package net.stuxcrystal.simpledev.example;

import net.stuxcrystal.simpledev.commandhandler.CommandExecutor;
import net.stuxcrystal.simpledev.commandhandler.contrib.history.Action;

/**
 * The actual ban action.
 */
public class BanAction extends Action {

    /**
     * Banned player.
     */
    private final CommandExecutor executor;

    /**
     * <p>The state of the player.</p>
     *
     * <p>{@code true} if banned.</p>
     */
    private final boolean newState;

    /**
     * Initializes the action.
     * @param owner The owner of the action.
     * @param executor  The executor to ban.
     * @param newState  The new state of the executor after the ban.
     */
    protected BanAction(CommandExecutor owner, CommandExecutor executor, boolean newState) {
        super(owner);
        this.executor = executor;
        this.newState = newState;
    }

    @Override
    public void undo() {
        // Use getComponent(BanExtension.class) to get our ban function.
        ((BanExtension)this.executor.getComponent(BanExtension.class)).ban(!this.newState);
    }

    @Override
    public void redo() {
        // Use getComponent(BanExtension.class) to get our ban function.
        ((BanExtension)this.executor.getComponent(BanExtension.class)).ban(this.newState);
    }

    @Override
    public String getDescription(CommandExecutor executor) {
        return (this.newState?"Ban":"Unban") + " player " + this.executor.getName();
    }
}
