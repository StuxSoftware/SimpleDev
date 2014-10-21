package net.stuxcrystal.commandhandler.compat.bungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.arguments.ArgumentParser;
import net.stuxcrystal.commandhandler.commands.CommandContainer;

import java.util.Arrays;
import java.util.List;

/**
 * Wrapper for a bungeecord command class.
 */
public class BungeeCommandContainer implements CommandContainer {

    private final Command command;

    public BungeeCommandContainer(Command command) {
        this.command = command;
    }

    @Override
    public String getName() {
        return this.command.getName();
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList(this.command.getAliases());
    }

    @Override
    public String getPermission() {
        return this.command.getPermission();
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public boolean allowPlayers() {
        return true;
    }

    @Override
    public boolean allowConsole() {
        return true;
    }

    @Override
    public boolean isOperatorCommand() {
        return false;
    }

    @Override
    public boolean isAsyncCommand() {
        return false;
    }

    @Override
    public String getSupportedFlags() {
        return "";
    }

    @Override
    public int getMinimalArgumentCount() {
        return -1;
    }

    @Override
    public int getMaximalArgumentCount() {
        return -1;
    }

    @Override
    public boolean parseArguments() {
        return false;
    }

    @Override
    public void execute(CommandExecutor executor, ArgumentParser parser) {

    }

    @Override
    public void execute(CommandExecutor executor, String[] args) {
        if (!(executor.getHandle() instanceof CommandSender)) {
            executor.sendMessage("Unsupported message color.");
            return;
        }
        this.command.execute((CommandSender)executor.getHandle(), args);
    }
}