package net.stuxcrystal.commandhandler.compat.bungee.contrib;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.stuxcrystal.commandhandler.compat.bungee.BungeeCommandHandler;

/**
 * Represents a command for BungeeCord.
 */
public class BungeeCommand extends Command {

    private BungeeCommandHandler bch;

    public BungeeCommand(String name, BungeeCommandHandler bch) {
        super(name);
        this.bch = bch;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        this.bch.executeSubCommand(sender, args);
    }
}
