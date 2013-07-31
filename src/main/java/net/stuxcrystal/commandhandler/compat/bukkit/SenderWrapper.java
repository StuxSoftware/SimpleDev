package net.stuxcrystal.commandhandler.compat.bukkit;

import net.stuxcrystal.commandhandler.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Wraps a CommandSender.
 */
public class SenderWrapper extends CommandExecutor<CommandSender> {

    final CommandSender sender;

    SenderWrapper(CommandSender sender) {
        this.sender = sender;
    }


    @Override
    public String getName() {
        return this.sender.getName();
    }

    @Override
    public void sendMessage(String... message) {
        this.sender.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String node) {
        return this.sender.hasPermission(node);
    }

    @Override
    public boolean isPlayer() {
        return this.sender instanceof Player;
    }

    @Override
    public boolean isOp() {
        return this.sender.isOp();
    }

    @Override
    public CommandSender getHandle() {
        return this.sender;
    }
}
