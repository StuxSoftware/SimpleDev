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

import net.canarymod.Canary;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.plugin.Plugin;
import net.stuxcrystal.commandhandler.Backend;
import net.stuxcrystal.commandhandler.CommandExecutor;

import java.util.logging.Logger;

/**
 * Backend for the Canary-Mod-Server.
 */
public class CanaryPluginBackend implements Backend<Plugin> {

    final Plugin plugin;

    public CanaryPluginBackend(Plugin plugin) {

        this.plugin = plugin;
        this.plugin.getLogman().warning("CanaryMod Recode does not support asynchronous tasks out of the box. Falling back to threads.");
    }

    @Override
    public Logger getLogger() {
        return this.plugin.getLogman();
    }

    @Override
    public void schedule(Runnable runnable) {
        new Thread(runnable).start();
    }

    @Override
    public CommandExecutor<?> getExecutor(String name) {
        if (name == null || name.isEmpty()) {
            return wrapReceiver(Canary.getServer());
        }

        return wrapReceiver(Canary.getServer().getPlayer(name));
    }

    @Override
    public Plugin getHandle() {
        return this.plugin;
    }

    CommandExecutor<?> wrapReceiver(MessageReceiver receiver) {
        return new CanarySenderWrapper(receiver);
    }
}
