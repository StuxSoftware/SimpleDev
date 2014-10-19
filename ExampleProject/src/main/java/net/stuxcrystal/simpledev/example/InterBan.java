package net.stuxcrystal.simpledev.example;

import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.CommandHandler;
import net.stuxcrystal.commandhandler.history.Action;
import net.stuxcrystal.configuration.ConfigurationLoader;
import net.stuxcrystal.configuration.parser.exceptions.ConfigurationException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Platform-Independent Ban and Kick-System that allows the player to undo and redo their or
 * other commands.
 */
public class InterBan {

    /**
     * The commands of interban.
     */
    private CommandHandler commands;

    /**
     * The configuration loader of interban.
     */
    private ConfigurationLoader cf;

    /**
     * The logger of interban.
     */
    private Logger logger;

    /**
     * Contains the current interban configuration.
     */
    private InterBanConfig ibc;

    /**
     * The instance of interban
     */
    private static InterBan INSTANCE = null;

    /**
     * The configuration.
     * @param handler The handler.
     * @param cl      The configuration loader.
     */
    public InterBan(CommandHandler handler, ConfigurationLoader cl) {
        if (InterBan.INSTANCE != null)
            throw new UnsupportedOperationException("Singleton already instantiated.");
        InterBan.INSTANCE = this;

        // Set the values.
        this.commands = handler;
        this.cf = cl;

        this.logger = this.commands.getServerBackend().getLogger();
    }

    public static InterBan getInstance() {
        return InterBan.INSTANCE;
    }

    /**
     * Executes the action.
     * @param action     The action to execute.
     */
    public void executeAction(Action action) {
        if (this.ibc.enableHistory) {
            action.getOwner().getHistory().execute(action);
        } else {
            action.firstExecution();
        }
    }

    /**
     * Enables the interban plugin.
     */
    public void enable() {
        try {
            this.ibc = this.cf.loadAndUpdate(null, null, InterBanConfig.class);
        } catch (ConfigurationException e) {
            this.logger.log(Level.SEVERE, "Failed to load and update configuration.", e);
            return;
        }

        this.commands.registerCommands(InterBanCommands.class);
        if (this.ibc.enableHistory)
            this.commands.registerCommands(InterBanHistoryCommands.class);
    }

}
