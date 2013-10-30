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

import cpw.mods.fml.common.FMLLog;
import net.minecraft.command.ICommandSender;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.CommandHandler;
import org.apache.commons.lang.ArrayUtils;

import java.util.logging.Logger;

/**
 * CommandHandler for forge.
 */
public class ForgeCommandHandler<T> extends CommandHandler {

    /**
     * Constructor for the command handler.
     * @param mod The mod.
     */
    public ForgeCommandHandler(T mod) {
        this(mod, FMLLog.getLogger());
    }

    /**
     * The Constructor for the CommandHandler.
     * @param mod    The mod.
     * @param logger The logger to use.
     */
    public ForgeCommandHandler(T mod, Logger logger) {
        super(new ForgePluginBackend(mod, logger));
        ((ForgePluginBackend<T>) this.backend).setCommandHandler(this);
    }

    /**
     * Executes a command.
     * @param sender  The sender who triggered the command.
     * @param rawArgs The raw arguments.
     */
    public void execute(ICommandSender sender, String[] rawArgs) {
        CommandExecutor executor = ((ForgePluginBackend<T>) this.backend).wrapExecutor(sender);

        String name;
        String[] args;

        if (rawArgs.length == 0) {
            name = " ";
            args = new String[0];
        } else {
            name = rawArgs[0];
            args = (String[]) ArrayUtils.remove(rawArgs, 0);
        }

        this.execute(executor, name, args);
    }
}
