package net.stuxcrystal.simpledev.commands.compat.bukkit.contrib.commands;

import net.stuxcrystal.simpledev.commands.CommandHandler;
import net.stuxcrystal.simpledev.commands.commands.CommandContainer;
import net.stuxcrystal.simpledev.commands.commands.CommandLoader;
import org.bukkit.command.PluginCommand;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Loader for 
 */
public class BukkitCommandLoader implements CommandLoader {

    @Override
    public List<CommandContainer> register(CommandHandler registrar, Object container) {
        if (!(container instanceof PluginCommand)) {
            return Collections.emptyList();
        }
        return Arrays.asList((CommandContainer)new BukkitCommandExecutorContainer((PluginCommand)container));
    }
}
