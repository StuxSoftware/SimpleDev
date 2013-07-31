package net.stuxcrystal.commandhandler.compat.canary;

import net.canarymod.chat.MessageReceiver;
import net.canarymod.plugin.Plugin;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.CommandHandler;
import org.apache.commons.lang.ArrayUtils;

/**
 * CommandHandler for Canary Mod.
 */
public class CanaryCommandHandler extends CommandHandler {
    /**
     * The Constructor for base-commands.
     *
     * @param plugin The backend that uses this library.
     */
    public CanaryCommandHandler(Plugin plugin) {
        super(new CanaryPluginBackend(plugin));
    }

    /**
     * Executes the command as a subcommand.
     *
     * @param caller The caller.
     * @param args   The given arguments.
     */
    public void executeSubCommand(MessageReceiver caller, String[] args) {
        CommandExecutor executor = ((CanaryPluginBackend) getServerBackend()).wrapReceiver(caller);
        args = (String[]) ArrayUtils.remove(args, 0);

        String name;
        if (args.length == 0) {
            name = "";
            args = new String[0];
        } else {
            name = args[0];
            args = (String[]) ArrayUtils.remove(args, 0);
        }

        if (!this.execute(executor, name, args))
            executor.sendMessage(_(executor, "cmd.notfound"));
        ;
    }
}
