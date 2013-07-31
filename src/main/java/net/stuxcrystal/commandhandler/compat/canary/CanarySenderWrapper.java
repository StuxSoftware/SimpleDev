/*
 * Copyright 2013 StuxCrystal
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */

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
