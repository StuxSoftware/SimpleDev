package net.stuxcrystal.simpledev.commands.compat.bungee.contrib;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.stuxcrystal.simpledev.commands.compat.bungee.BungeeCommandHandler;

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
