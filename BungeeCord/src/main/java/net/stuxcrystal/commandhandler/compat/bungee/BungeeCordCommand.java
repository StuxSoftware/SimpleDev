package net.stuxcrystal.commandhandler.compat.bungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

/**
 * Represents a command for BungeeCord.
 */
public class BungeeCordCommand extends Command {

    private BungeeCommandHandler bch;

    public BungeeCordCommand(String name, BungeeCommandHandler bch) {
        super(name);
        this.bch = bch;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        this.bch.executeSubCommand(sender, args);
    }
}
