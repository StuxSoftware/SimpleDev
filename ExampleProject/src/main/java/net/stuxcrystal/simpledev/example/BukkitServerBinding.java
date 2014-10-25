package net.stuxcrystal.simpledev.example;

import net.stuxcrystal.simpledev.commandhandler.CommandExecutor;
import net.stuxcrystal.simpledev.commandhandler.compat.bukkit.BukkitCommandHandler;
import net.stuxcrystal.simpledev.commandhandler.component.Component;
import net.stuxcrystal.simpledev.commandhandler.component.ComponentContainer;
import net.stuxcrystal.simpledev.configuration.compat.BukkitConfigurationLoader;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Binding to bukkit.
 */
public class BukkitServerBinding extends JavaPlugin implements ComponentContainer {

    /**
     * The command handler that contains the commands for us.
     */
    BukkitCommandHandler ch;

    @Override
    public void onEnable() {
        // Create a new one.
        this.ch = new BukkitCommandHandler(this);

        // Since this class defines the extension methods, we register our class to the command-handler.
        this.ch.registerComponent(this);

        // Create the InterBan instance.
        new InterBan(this.ch, new BukkitConfigurationLoader(this)).enable();
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String args[]) {
        // Forward all command calls to the command handler to handle the commands.
        return this.ch.onCommand(sender, command, label, args);
    }

    /**
     * Implements {@link net.stuxcrystal.simpledev.example.BanExtension#ban(boolean)}
     *
     * @param executor The player to ban.
     * @param newState The new state of the player.
     */
    @Component
    public void ban(CommandExecutor<CommandSender> executor, boolean newState) {
        if (!executor.isPlayer())
            return;

        ((Player)executor.getHandle()).setBanned(newState);
    }

    /**
     * Implements {@link net.stuxcrystal.simpledev.example.BanExtension#isBanned()}
     *
     * @param executor The player to query.
     * @return The state of the player.
     */
    @Component
    public boolean isBanned(CommandExecutor<CommandSender> executor) {
        if (!executor.isPlayer())
            return false;
        return ((Player)executor.getHandle()).isBanned();
    }
}
