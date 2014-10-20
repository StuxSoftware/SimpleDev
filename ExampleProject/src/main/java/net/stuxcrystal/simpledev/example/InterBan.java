package net.stuxcrystal.simpledev.example;

import net.stuxcrystal.commandhandler.CommandHandler;
import net.stuxcrystal.commandhandler.commands.contrib.builder.CommandBuilder;
import net.stuxcrystal.commandhandler.contrib.history.Action;
import net.stuxcrystal.configuration.ConfigurationLoader;
import net.stuxcrystal.configuration.parser.exceptions.ConfigurationException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Platform-Independent Ban and Kick-System that allows the player to undoAction and redoAction their or
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
        // Make this a singleton.
        if (InterBan.INSTANCE != null)
            throw new UnsupportedOperationException("Singleton already instantiated.");
        InterBan.INSTANCE = this;

        // Set the values.
        this.commands = handler;
        this.cf = cl;

        // Extract the logger.
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
            // Use the history if enabled.
            action.getOwner().getHistory().executeAction(action);
        } else {
            // Otherwise, directly execute the action.
            action.firstExecution();
        }
    }

    /**
     * Enables the interban plugin.
     */
    public void enable() {
        // Read (and create when necessary) the configuration.
        try {
            this.ibc = this.cf.loadAndUpdate(null, null, InterBanConfig.class);
        } catch (ConfigurationException e) {
            this.logger.log(Level.SEVERE, "Failed to load and update configuration.", e);
            return;
        }

        // Register Commands.
        this.commands.registerCommands(InterBanCommands.class);

        // We use the command builder to build a simple subcommand.
        if (this.ibc.enableHistory) {
            CommandHandler ch = this.commands.createChildHandler();
            ch.registerCommands(InterBanHistoryCommands.class);
            this.commands.registerCommands(new CommandBuilder().name("banhistory").player(true).console(true).create(ch));
        }
    }

}
