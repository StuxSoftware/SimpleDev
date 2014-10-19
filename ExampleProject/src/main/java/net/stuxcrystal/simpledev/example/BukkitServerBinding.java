package net.stuxcrystal.simpledev.example;

import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.CommandHandler;
import net.stuxcrystal.commandhandler.compat.bukkit.BukkitCommandHandler;
import net.stuxcrystal.commandhandler.component.Component;
import net.stuxcrystal.commandhandler.component.ComponentContainer;
import net.stuxcrystal.configuration.compat.BukkitConfigurationLoader;
import net.stuxcrystal.simpledev.example.InterBan;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Binding to bukkit.
 */
public class BukkitServerBinding extends JavaPlugin implements ComponentContainer {

    BukkitCommandHandler ch;

    @Override
    public void onEnable() {
        this.ch = new BukkitCommandHandler(this);
        this.ch.registerComponent(this);
        new InterBan(this.ch, new BukkitConfigurationLoader(this));
    }

    @Override
    public void onDisable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String args[]) {
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
