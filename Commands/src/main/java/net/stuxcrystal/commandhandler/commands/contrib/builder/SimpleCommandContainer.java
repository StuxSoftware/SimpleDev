package net.stuxcrystal.commandhandler.commands.contrib.builder;

import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.arguments.ArgumentParser;
import net.stuxcrystal.commandhandler.commands.CommandContainer;

import java.util.List;

/**
 * Simple command container.
 */
public abstract class SimpleCommandContainer implements CommandContainer {

    private final String name;

    private final List<String> aliases;

    private final String permission;

    private final String description;

    private final boolean allowPlayers;

    private final boolean allowConsole;

    private final boolean opCommand;

    private final boolean async;

    private final String flags;

    private final int minSize;

    private final int maxSize;

    protected SimpleCommandContainer(
            String name, List<String> aliases, String permission, String description,
            boolean allowPlayers, boolean allowConsole, boolean opCommand,
            boolean async,
            String flags, int minSize, int maxSize
    ) {
        this.name = name;
        this.aliases = aliases;
        this.permission = permission;
        this.description = description;
        this.allowPlayers = allowPlayers;
        this.allowConsole = allowConsole;
        this.opCommand = opCommand;
        this.async = async;
        this.flags = flags;
        this.minSize = minSize;
        this.maxSize = maxSize;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public List<String> getAliases() {
        return this.aliases;
    }

    @Override
    public String getPermission() {
        return this.permission;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public boolean allowPlayers() {
        return this.allowPlayers;
    }

    @Override
    public boolean allowConsole() {
        return this.allowConsole;
    }

    @Override
    public boolean isOperatorCommand() {
        return this.opCommand;
    }

    @Override
    public boolean isAsyncCommand() {
        return this.async;
    }

    @Override
    public String getSupportedFlags() {
        return this.flags;
    }

    @Override
    public int getMinimalArgumentCount() {
        return this.minSize;
    }

    @Override
    public int getMaximalArgumentCount() {
        return this.maxSize;
    }

    @Override
    public boolean parseArguments() {
        return true;
    }

    @Override
    public void execute(CommandExecutor executor, String[] args) {}
}
