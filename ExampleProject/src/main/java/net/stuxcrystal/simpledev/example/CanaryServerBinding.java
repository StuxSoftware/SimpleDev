package net.stuxcrystal.simpledev.example;

import net.canarymod.Canary;
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.commandsys.Command;
import net.canarymod.commandsys.CommandDependencyException;
import net.canarymod.commandsys.CommandListener;
import net.canarymod.plugin.Plugin;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.compat.canary.CanaryCommandHandler;
import net.stuxcrystal.commandhandler.component.Component;
import net.stuxcrystal.commandhandler.component.ComponentContainer;
import net.stuxcrystal.configuration.compat.CanaryConfigurationLoader;

/**
 * The binding to canarymod.
 */
public class CanaryServerBinding extends Plugin implements ComponentContainer, CommandListener {

    /**
     * Contains all commands.
     */
    CanaryCommandHandler canaryCommandHandler;

    @Override
    public boolean enable() {
        // Create the command handler.
        this.canaryCommandHandler = new CanaryCommandHandler(this);

        // Since this class implements our extension methods we register 'this'.
        this.canaryCommandHandler.registerComponent(this);

        // Create the interban instance.
        new InterBan(this.canaryCommandHandler, new CanaryConfigurationLoader(this)).enable();

        try {
            this.registerCommands(this, true);
        } catch (CommandDependencyException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void disable() {

    }

    /**
     * Unfortunately you can't do such nice things like in bukkit.
     *
     * @param receiver    The receiver.
     * @param parameters  The parameters.
     */
    @Command(aliases = {"iban"}, description = "Base-Command of interban", permissions = {}, toolTip = "Base-Command of Interban")
    public void interban(MessageReceiver receiver, String[] parameters) {
        // We have to use subcommands. :(

        // Redirect to our command-handler.
        this.canaryCommandHandler.executeSubCommand(receiver, parameters);
    }

    /**
     * Implements {@link net.stuxcrystal.simpledev.example.BanExtension#ban(boolean)}
     *
     * @param executor The player to ban.
     * @param newState The new state of the player.
     */
    @Component
    public void ban(CommandExecutor<MessageReceiver> executor, boolean newState) {
        if (!executor.isPlayer())
            return;

        if (newState)
            Canary.bans().issueBan((Player)executor.getHandle(), "Banned by InterBans");
        else
            Canary.bans().unban((Player)executor.getHandle());
    }

    /**
     * Implements {@link net.stuxcrystal.simpledev.example.BanExtension#isBanned()}
     *
     * @param executor The player to query.
     * @return The state of the player.
     */
    @Component
    public boolean isBanned(CommandExecutor<MessageReceiver> executor) {
        if (!executor.isPlayer())
            return false;
        return Canary.bans().isBanned(executor.getName());
    }
}
