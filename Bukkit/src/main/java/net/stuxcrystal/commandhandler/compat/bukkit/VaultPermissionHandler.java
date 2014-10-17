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

import net.milkbowl.vault.permission.Permission;
import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.PermissionHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Permission-Handler for Vault.
 */
public class VaultPermissionHandler implements PermissionHandler {

    /**
     * Handler to the permissions provider.
     */
    private Permission permissions = null;

    /**
     * Constructs the Vault-Permission-Handler.
     */
    public VaultPermissionHandler() {}

    /**
     * Prepares the permission-handler to be used.<p />
     *
     * Vault has to be loaded when this function is called.
     */
    private void preparePermissionHandler() {
        if (permissions != null) return;

        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("Vault");
        if (plugin == null)
            throw new IllegalStateException("Vault is not installed.");

        RegisteredServiceProvider<Permission> permission = Bukkit.getServicesManager().getRegistration(Permission.class);
        this.permissions = permission.getProvider();

        if (this.permissions == null)
            throw new IllegalStateException("No provider installed.");
    }

    /**
     * Checks if the user has the permissions needed.
     *
     * @param executor The user.
     * @param node     The node to check.
     * @throws IllegalArgumentException  If the given command executor does not represent a user.
     * @return true if the player has the permissions needed.
     */
    @Override
    public boolean hasPermission(CommandExecutor<?> executor, String node) {
        this.preparePermissionHandler();

        if (!(executor instanceof BukkitSenderWrapper))
            throw new IllegalArgumentException("This permission handler needs a Bukkit-Command-Sender.");

        return this.permissions.has(((BukkitSenderWrapper) executor).getHandle(), node);
    }
}
