package net.stuxcrystal.simpledev.commandhandler.arguments.splitter;

import net.stuxcrystal.simpledev.commandhandler.arguments.ArgumentSplitter;
import org.apache.commons.lang.ArrayUtils;

/**
 * Like the default splitting on bukkit.
 */
public class NoSplit implements ArgumentSplitter {

    @Override
    public String[] split(String rawArgs) {
        String[] args = rawArgs.split(" ");
        String[] arguments;
        String flags;

        if (args.length == 0) {
            flags = "";
            arguments = args;
        } else if (args[0].startsWith("-")) {
            flags = args[0].substring(1);
            arguments = (String[]) ArrayUtils.remove(args, 0);
        } else {
            flags = "";
            arguments = args;
        }

        return (String[]) ArrayUtils.add(arguments, 0, flags);
    }
}
