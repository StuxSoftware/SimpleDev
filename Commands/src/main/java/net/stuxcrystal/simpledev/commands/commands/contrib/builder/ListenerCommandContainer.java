package net.stuxcrystal.simpledev.commands.commands.contrib.builder;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.arguments.ArgumentParser;

import java.util.List;

/**
 * The command container that listener.
 */
public class ListenerCommandContainer extends SimpleCommandContainer {

    private final CommandListener listener;

    protected ListenerCommandContainer(
            String name, List<String> aliases, String permission, String description,
            boolean allowPlayers, boolean allowConsole, boolean opCommand,
            boolean async,
            String flags, int minSize, int maxSize,
            CommandListener listener) {
        super(name, aliases, permission, description, allowPlayers, allowConsole, opCommand, async, flags, minSize, maxSize);
        this.listener = listener;
    }


    @Override
    public void execute(CommandExecutor executor, ArgumentParser parser) {
        this.listener.execute(this, executor, parser);
    }
}
