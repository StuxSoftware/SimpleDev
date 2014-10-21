package net.stuxcrystal.commandhandler.compat.bungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.CommandHandler;
import net.stuxcrystal.commandhandler.compat.bungee.contrib.BungeeCommand;
import net.stuxcrystal.commandhandler.compat.bungee.contrib.commands.BungeeCommandLoader;
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
        this.initHandler();
    }

    /**
     * The constructor for base-commands.
     * @param handler The handler to copy the values from.
     */
    public BungeeCommandHandler(BungeeCommandHandler handler) {
        super(handler);
        this.initHandler();
    }

    /**
     * Adds Bungee-Specific command-loaders.
     */
    private void initHandler() {
        this.registerCommandLoader(new BungeeCommandLoader());
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
            name = CommandHandler.FALLBACK_COMMAND_NAME;
            args = new String[0];
        } else {
            name = args[0];
            args = (String[]) ArrayUtils.remove(args, 0);
        }

        if (!this.execute(executor, name, args))
            executor.sendMessage(T(executor, "cmd.notfound"));

    }

    /**
     * Makes a command from this command handler.
     * @param name The name of the command.
     * @return The command instance.
     */
    public Command asCommand(String name) {
        return new BungeeCommand(name, this);
    }
}
