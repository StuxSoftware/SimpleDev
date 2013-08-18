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

package net.stuxcrystal.commandhandler.compat.forge;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.common.MinecraftForge;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.CommandHandler;

/**
 * Represents a command handler in forge.
 */
public class ForgeCommandExecutor extends CommandExecutor<ICommandSender> {

    /**
     * The reference to the command sender.
     */
    private final ICommandSender sender;

    /**
     * The handler.
     *
     * @param handler The handler providing the permission handler.
     */
    public ForgeCommandExecutor(ICommandSender sender, CommandHandler handler) {
        super(handler);
        this.sender = sender;
    }

    @Override
    public String getName() {
        return sender.getCommandSenderName();
    }

    @Override
    public void sendMessage(String... message) {
        for (String line : message) {
            sender.sendChatToPlayer(ChatMessageComponent.func_111066_d(line));
        }
    }

    /**
     * Checks if the sender-type derives from EntityPlayer.
     * @return true if the player is instance of EntityPlayer.
     */
    @Override
    public boolean isPlayer() {
        return (sender instanceof EntityPlayer);
    }

    /**
     * Checks if the player is in the operator list.
     * @return true if the player is either single-player or is operator on the server.
     */
    @Override
    public boolean isOp() {
        return MinecraftServer.getServer().getConfigurationManager().getOps().contains(this.getName());
    }

    @Override
    public ICommandSender getHandle() {
        return this.sender;
    }
}
