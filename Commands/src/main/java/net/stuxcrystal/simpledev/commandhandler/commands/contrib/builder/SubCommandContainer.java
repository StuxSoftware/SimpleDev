package net.stuxcrystal.simpledev.commandhandler.commands.contrib.builder;

import net.stuxcrystal.simpledev.commandhandler.CommandExecutor;
import net.stuxcrystal.simpledev.commandhandler.CommandHandler;
import net.stuxcrystal.simpledev.commandhandler.arguments.ArgumentParser;

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
        // Use the execute method created for this use
        this.subhandler.execute(executor, parser.getArguments(0));
    }
}
