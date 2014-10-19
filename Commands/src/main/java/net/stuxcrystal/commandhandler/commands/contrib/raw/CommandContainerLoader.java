package net.stuxcrystal.commandhandler.commands.contrib.raw;

import net.stuxcrystal.commandhandler.CommandHandler;
import net.stuxcrystal.commandhandler.commands.CommandContainer;
import net.stuxcrystal.commandhandler.commands.CommandLoader;

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
