package net.stuxcrystal.simpledev.example;

import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.arguments.ArgumentParser;
import net.stuxcrystal.commandhandler.commands.contrib.annotations.Command;
import net.stuxcrystal.commandhandler.commands.contrib.annotations.CommandListener;

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
    public void undo(CommandExecutor executor, ArgumentParser argumentParser) {
        executor.getHistory().undoAction();
    }

    /**
     * Rdo the last action.
     * @param executor         The last action of the executor.
     * @param argumentParser   The arguments.
     */
    @Command(minSize = 0, maxSize = 0, permission = "interban.history.redoAction")
    public void redo(CommandExecutor executor, ArgumentParser argumentParser) {
        executor.getHistory().redoAction();
    }

}
