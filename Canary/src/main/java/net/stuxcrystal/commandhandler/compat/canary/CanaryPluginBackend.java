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
import net.canarymod.api.entity.living.humanoid.Player;
import net.canarymod.chat.MessageReceiver;
import net.canarymod.plugin.Plugin;
import net.canarymod.tasks.ServerTask;
import net.stuxcrystal.commandhandler.CommandBackend;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.contrib.scheduler.FallbackSchedulerBackend;

import java.util.List;
import java.util.logging.Logger;

/**
 * Backend for the Canary-Mod-Server.
 */
public class CanaryPluginBackend extends CommandBackend<Plugin, MessageReceiver> {

    private FallbackSchedulerBackend scheduler;

    /**
     * Creates a new handle.
     *
     * @param handle The handle in the backend.
     */
    protected CanaryPluginBackend(Plugin handle) {
        super(handle);
        this.scheduler = new FallbackSchedulerBackend();
    }


    @Override
    public Logger getLogger() {
        return JULToLog4jBridge.bridge(this.getHandle().getLogman());
    }

    @Override
    public void scheduleAsync(Runnable runnable) {
        this.scheduler.schedule(this.getCommandHandler(), runnable);
    }

    @Override
    public void scheduleSync(final Runnable runnable) {
        Canary.getServer().addSynchronousTask(new ServerTask(this.getHandle(), 0) {
            @Override
            public void run() {
                runnable.run();
            }
        });
    }

    @Override
    public boolean inMainThread() {
        return this.scheduler.isMainThread();
    }

    @Override
    public CommandExecutor<?>[] getPlayers() {
        List<Player> players = Canary.getServer().getPlayerList();
        CommandExecutor[] executors = new CommandExecutor[players.size()];
        for (int i = 0; i<players.size(); i++) {
            executors[i] = wrapReceiver(players.get(i));
        }
        return executors;
    }

    @Override
    public CommandExecutor<?> getExecutor(String name) {
        if (name == null || name.isEmpty() || "console".equalsIgnoreCase(name)) {
            return wrapReceiver(Canary.getServer());
        }

        return wrapReceiver(Canary.getServer().getPlayer(name));
    }

    @Override
    public CommandExecutor<?> wrapPlayer(MessageReceiver player) {
        return wrapReceiver(player);
    }

    @Override
    public CommandExecutor<?> getConsole() {
        return wrapReceiver(Canary.getServer());
    }

    @Override
    public Boolean hasPermission(CommandExecutor<?> executor, String node) {
        if (!(executor instanceof CanarySenderWrapper)) return null;
        return ((CanarySenderWrapper) executor).getHandle().hasPermission(node);
    }

    CommandExecutor<?> wrapReceiver(MessageReceiver receiver) {
        if (receiver == null) return null;
        return new CanarySenderWrapper(receiver, this.getCommandHandler());
    }
}
