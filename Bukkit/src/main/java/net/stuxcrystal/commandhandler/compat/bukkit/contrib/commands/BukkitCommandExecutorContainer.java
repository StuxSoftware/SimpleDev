package net.stuxcrystal.commandhandler.compat.bukkit.contrib.commands;

import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.arguments.ArgumentParser;
import net.stuxcrystal.commandhandler.commands.CommandContainer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import java.util.List;

/**
 * Registers a bukkit command as an actual command.
 */
public class BukkitCommandExecutorContainer implements CommandContainer {

    private final PluginCommand command;

    private final org.bukkit.command.CommandExecutor executor;

    private BukkitCommandExecutorContainer(PluginCommand command, org.bukkit.command.CommandExecutor executor) {
        this.command = command;
        this.executor = executor;
    }

    public BukkitCommandExecutorContainer(PluginCommand command) {
        this(command, command.getExecutor());
    }

    public BukkitCommandExecutorContainer(String name, org.bukkit.command.CommandExecutor executor) {
        this(Bukkit.getPluginCommand(name), executor);
    }

    @Override
    public String getName() {
        return command.getName();
    }

    @Override
    public List<String> getAliases() {
        return command.getAliases();
    }

    @Override
    public String getPermission() {
        return command.getPermission();
    }

    @Override
    public String getDescription() {
        return command.getDescription();
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
        Object handle = executor.getHandle();
        if (!(handle instanceof CommandSender))
            executor.sendMessage("Unsupported executor type.");

        // Simple, but effective.
        this.executor.onCommand((CommandSender)executor.getHandle(), this.command, this.getName(), args);
    }
}
