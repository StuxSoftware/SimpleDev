package net.stuxcrystal.commandhandler.compat.canary;

import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;
import net.stuxcrystal.commandhandler.CommandExecutor;

/**
 * Wrapper for the Canary Sender.
 */
public class CanarySenderWrapper extends CommandExecutor<MessageReceiver> {

    final MessageReceiver receiver;

    public CanarySenderWrapper(MessageReceiver receiver) {
        this.receiver = receiver;
    }

    @Override
    public String getName() {
        return this.receiver.getName();
    }

    @Override
    public void sendMessage(String... message) {
        for (String line : message)
            this.receiver.message(parseMessage(line));
    }

    @Override
    public boolean hasPermission(String node) {
        return this.receiver.hasPermission(node);
    }

    @Override
    public boolean isPlayer() {
        return this.receiver instanceof Player;
    }

    @Override
    public boolean isOp() {
        if (!this.isPlayer()) return true;
        return ((Player) this.receiver).isAdmin();
    }

    @Override
    public MessageReceiver getHandle() {
        return this.receiver;
    }

    public String parseMessage(String msg) {
        if (!this.isPlayer())
            return msg.replaceAll("\u00A7[0-9a-fA-Fk-oK-OrR]", "");
        return msg;
    }
}
