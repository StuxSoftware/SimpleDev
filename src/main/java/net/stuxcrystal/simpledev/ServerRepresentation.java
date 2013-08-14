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

package net.stuxcrystal.simpledev;

import net.stuxcrystal.commandhandler.CommandHandler;
import net.stuxcrystal.configuration.ConfigurationLoader;

/**
 * The representation of the server.
 *
 * @param <Commands>      The CommandHandler for the server.
 * @param <Configuration> The ConfigurationLoader that loads the configurations.
 * @param <T>             The Type of the Plugins.
 */
public abstract class ServerRepresentation
        <Commands extends CommandHandler, Configuration extends ConfigurationLoader, T> {

    /**
     * The reference to the plugin.
     */
    protected final T plugin;

    /**
     * Constructs the server representation.
     *
     * @param plugin The plugin.
     */
    public ServerRepresentation(T plugin) {
        this.plugin = plugin;
    }

    /**
     * Creates the command handler.
     *
     * @return The command handler.
     */
    public abstract Commands createCommandHandler();

    /**
     * Loads the plugin.
     *
     * @return The configuration loader.
     */
    public abstract Configuration getConfigurationLoader();

}
