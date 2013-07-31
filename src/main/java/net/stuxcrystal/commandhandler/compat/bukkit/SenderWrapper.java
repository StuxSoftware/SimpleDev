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

package net.stuxcrystal.commandhandler.compat.bukkit;

import net.stuxcrystal.commandhandler.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Wraps a CommandSender.
 */
public class SenderWrapper extends CommandExecutor<CommandSender> {

    final CommandSender sender;

    SenderWrapper(CommandSender sender) {
        this.sender = sender;
    }


    @Override
    public String getName() {
        return this.sender.getName();
    }

    @Override
    public void sendMessage(String... message) {
        this.sender.sendMessage(message);
    }

    @Override
    public boolean hasPermission(String node) {
        return this.sender.hasPermission(node);
    }

    @Override
    public boolean isPlayer() {
        return this.sender instanceof Player;
    }

    @Override
    public boolean isOp() {
        return this.sender.isOp();
    }

    @Override
    public CommandSender getHandle() {
        return this.sender;
    }
}
