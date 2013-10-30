package net.stuxcrystal.commandhandler.compat.bungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Plugin;
import net.stuxcrystal.commandhandler.CommandBackend;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.CommandHandler;
import org.apache.commons.lang.ArrayUtils;

/**
 * Command Handler for BungeeCord.
 */
public class BungeeCommandHandler extends CommandHandler {

    /**
     * The Constructor for base-commands.
     */
    public BungeeCommandHandler(Plugin plugin) {
        super(new BungeePluginBackend(plugin));
    }

    /**
     * Command switch for multiple commands.
     * @param sender    The sender.
     * @param command   The command name.
     * @param arguments The arguments.
     */
    public void executeCommandSwitch(CommandSender sender, String command, String[] arguments) {
        this.execute(this.getServerBackend().wrapPlayer(sender), command, arguments);
    }

    /**
     * Command switch for subcommands.
     * @param sender The sender.
     * @param args   The arguments.
     */
    public void executeSubCommand(CommandSender sender, String[] args) {
        CommandExecutor executor = this.getServerBackend().wrapPlayer(sender);
        args = (String[]) ArrayUtils.remove(args, 0);

        String name;
        if (args.length == 0) {
            name = " ";
            args = new String[0];
        } else {
            name = args[0];
            args = (String[]) ArrayUtils.remove(args, 0);
        }

        if (!this.execute(executor, name, args))
            executor.sendMessage(_(executor, "cmd.notfound"));

    }
}