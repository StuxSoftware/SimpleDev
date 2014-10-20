package net.stuxcrystal.commandhandler.compat.bukkit;

import net.stuxcrystal.commandhandler.CommandHandler;
import net.stuxcrystal.commandhandler.commands.CommandContainer;
import net.stuxcrystal.commandhandler.commands.CommandLoader;
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
