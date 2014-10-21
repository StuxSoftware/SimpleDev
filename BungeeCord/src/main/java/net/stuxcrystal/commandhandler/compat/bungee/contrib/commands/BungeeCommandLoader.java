package net.stuxcrystal.commandhandler.compat.bungee.contrib.commands;

import net.md_5.bungee.api.plugin.Command;
import net.stuxcrystal.commandhandler.CommandHandler;
import net.stuxcrystal.commandhandler.commands.CommandContainer;
import net.stuxcrystal.commandhandler.commands.CommandLoader;
import net.stuxcrystal.commandhandler.compat.bungee.contrib.commands.BungeeCommandContainer;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Basic command loader for BungeeCord.
 */
public class BungeeCommandLoader implements CommandLoader {

    @Override
    public List<CommandContainer> register(CommandHandler registrar, Object container) {
        if (!(container instanceof Command))
            return Collections.emptyList();
        return Arrays.asList((CommandContainer)new BungeeCommandContainer((Command)container));
    }
}
