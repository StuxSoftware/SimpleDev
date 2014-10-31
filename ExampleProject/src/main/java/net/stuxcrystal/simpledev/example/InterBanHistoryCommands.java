package net.stuxcrystal.simpledev.example;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.arguments.ArgumentList;
import net.stuxcrystal.simpledev.commands.commands.contrib.annotations.Command;
import net.stuxcrystal.simpledev.commands.commands.contrib.annotations.CommandListener;

/**
 * The commands for the history in interban.
 */
public class InterBanHistoryCommands implements CommandListener{

    /**
     * Undo the last action.
     * @param executor         The last action of the executor.
     * @param argumentParser   The arguments.
     */
    @Command(minSize = 0, maxSize = 0, permission = "interban.history.undoAction")
    public void undo(CommandExecutor executor, ArgumentList argumentParser) {
        executor.getHistory().undoAction();
    }

    /**
     * Rdo the last action.
     * @param executor         The last action of the executor.
     * @param argumentParser   The arguments.
     */
    @Command(minSize = 0, maxSize = 0, permission = "interban.history.redoAction")
    public void redo(CommandExecutor executor, ArgumentList argumentParser) {
        executor.getHistory().redoAction();
    }

}
