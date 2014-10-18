package net.stuxcrystal.commandhandler.history;

import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.CommandHandler;
import net.stuxcrystal.commandhandler.component.Component;
import net.stuxcrystal.commandhandler.component.ComponentContainer;

/**
 * Defines the history component.
 */
public class HistoryComponent implements ComponentContainer {

    /**
     * Undoes the last action.
     * @param executor The last action that has been undone.
     * @return {@code true} if the action has been undone.
     */
    @Component
    public static boolean undo(CommandExecutor executor) {
        HistoryContainer container = (HistoryContainer)executor.getSession(HistoryContainer.class);
        return container.undo();
    }

    /**
     * Redoes the next action.
     * @param executor The executor which action should be done.
     * @return {@code true} if the action has been redone.
     */
    @Component
    public static boolean redo(CommandExecutor executor) {
        HistoryContainer container = (HistoryContainer)executor.getSession(HistoryContainer.class);
        return container.redo();
    }

    /**
     * Executes the action
     *
     * @param executor The executor which executes the action.
     * @param action   The action to execute.
     */
    @Component
    public static void executeAction(CommandExecutor executor, Action action) {
        HistoryContainer container = (HistoryContainer)executor.getSession(HistoryContainer.class);
        container.execute(action);
    }

}
