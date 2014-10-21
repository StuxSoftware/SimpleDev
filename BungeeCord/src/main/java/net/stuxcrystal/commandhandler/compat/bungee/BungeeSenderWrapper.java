package net.stuxcrystal.commandhandler.compat.bungee;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.CommandHandler;


/**
 * Wraps an Command-Sender.
 */
public class BungeeSenderWrapper extends CommandExecutor<CommandSender> {

    /**
     * The handler.
     *
     * @param handler The handler providing the permission handler.
     */
    public BungeeSenderWrapper(CommandHandler handler, CommandSender sender) {
        super(sender, handler);
    }

    @Override
    public String getName() {
        return this.getHandle().getName();
    }


    @Override
    public void sendMessage(String... message) {
        for (String line : message) {
            this.getHandle().sendMessage(TextComponent.fromLegacyText(line));
        }
    }

    @Override
    public boolean isPlayer() {
        return !this.getHandle().equals(ProxyServer.getInstance().getConsole());
    }

    @Override
    public boolean isOp() {
        return !this.isPlayer();
    }
}
