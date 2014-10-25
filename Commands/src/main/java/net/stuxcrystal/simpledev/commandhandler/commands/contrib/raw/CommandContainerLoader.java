package net.stuxcrystal.simpledev.commandhandler.commands.contrib.raw;

import net.stuxcrystal.simpledev.commandhandler.CommandHandler;
import net.stuxcrystal.simpledev.commandhandler.commands.CommandContainer;
import net.stuxcrystal.simpledev.commandhandler.commands.CommandLoader;

import java.util.Arrays;
import java.util.List;

/**
 * A simple loader that uses command containers.
 */
public class CommandContainerLoader implements CommandLoader {

    @Override
    public List<CommandContainer> register(CommandHandler registrar, Object container) {
        if (!(container instanceof CommandContainer))
            return null;
        return Arrays.asList((CommandContainer)container);
    }
}
