package net.stuxcrystal.commandhandler.commands.contrib.builder;

import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.CommandHandler;
import net.stuxcrystal.commandhandler.arguments.ArgumentParser;

import java.util.List;

/**
 * Container that contains a sub-command.
 */
public class SubCommandContainer extends SimpleCommandContainer {

    private final CommandHandler subhandler;

    protected SubCommandContainer(String name, List<String> aliases, String permission, String description, boolean allowPlayers, boolean allowConsole, boolean opCommand, boolean async, String flags, int minSize, int maxSize, CommandHandler subhandler) {
        super(name, aliases, permission, description, allowPlayers, allowConsole, opCommand, async, flags, minSize, maxSize);
        this.subhandler = subhandler;
    }

    @Override
    public void execute(CommandExecutor executor, ArgumentParser parser) {
        String name;
        String[] args;

        // Parse the command.
        if (parser.count() == 0) {
            // An empty name represents a call without an argument
            name = CommandHandler.FALLBACK_COMMAND_NAME;
            args = new String[0];
        } else if (parser.count() == 1) {
            name = parser.getString(0);
            args = new String[0];
        } else {
            name = parser.getString(0);
            args = parser.getArguments(1);
        }

        this.subhandler.execute(executor, name, args);
    }
}
