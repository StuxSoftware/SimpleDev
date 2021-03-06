package net.stuxcrystal.simpledev.commands.contrib.history;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.component.Component;
import net.stuxcrystal.simpledev.commands.component.ComponentContainer;

import java.util.List;

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
    public static boolean undoAction(CommandExecutor executor) {
        HistoryContainer container = executor.getSessions().getSession(HistoryContainer.class);
        return container.undo();
    }

    /**
     * Redoes the next action.
     * @param executor The executor which action should be done.
     * @return {@code true} if the action has been redone.
     */
    @Component
    public static boolean redoAction(CommandExecutor executor) {
        HistoryContainer container = executor.getSessions().getSession(HistoryContainer.class);
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
        HistoryContainer container = executor.getSessions().getSession(HistoryContainer.class);
        container.execute(action);
    }

    /**
     * Lists all actions.
     *
     * @param executor The executor to query.
     * @return A list of all actions.
     */
    @Component
    public static List<Action> getActions(CommandExecutor executor) {
        HistoryContainer container = executor.getSessions().getSession(HistoryContainer.class);
        return container.getActions();
    }

    /**
     * The last executed action that has not been undone.
     *
     * @param executor The executor to query.
     * @return The last action.
     */
    @Component
    public static Action getLastAction(CommandExecutor executor) {
        HistoryContainer container = executor.getSessions().getSession(HistoryContainer.class);
        return container.getLastAction();
    }

    /**
     * Returns the next action that would have been executed
     * when {@link #redoAction(net.stuxcrystal.simpledev.commands.CommandExecutor)} is being executed.
     *
     * @return {@code null} if there is no action to redoAction.
     */
    @Component
    public static Action getNextAction(CommandExecutor executor) {
        HistoryContainer container = executor.getSessions().getSession(HistoryContainer.class);
        return container.getNextAction();
    }

}
