package net.stuxcrystal.simpledev.example;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.arguments.ArgumentList;
import net.stuxcrystal.simpledev.commands.commands.contrib.annotations.Command;
import net.stuxcrystal.simpledev.commands.commands.contrib.annotations.CommandListener;

/**
 * Listener for commands.
 */
public class InterBanCommands implements CommandListener {

    /**
     * Bans a user.
     * @param executor   The executor.
     * @param arguments  The arguments.
     */
    @Command(minSize = 1, maxSize = 1, permission = "interban.ban")
    public void ban(CommandExecutor executor, ArgumentList arguments) {
        // Get the player who is to be banned.
        CommandExecutor ce = arguments.getArgument(0, CommandExecutor.class);

        // Execute the action.
        InterBan.getInstance().executeAction(new BanAction(executor, ce, true));
    }

    /**
     * Unbans a user.
     * @param executor   The executor.
     * @param arguments  The arguments.
     */
    @Command(minSize = 1, maxSize = 1, permission = "interban.unban")
    public void unban(CommandExecutor executor, ArgumentList arguments) {
        // Get the player who is to be unbanned
        CommandExecutor ce = arguments.getArgument(0, CommandExecutor.class);

        // Execute the action.
        InterBan.getInstance().executeAction(new BanAction(executor, ce, true));
    }
}
