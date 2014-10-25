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

package net.stuxcrystal.simpledev.configuration.compat;

import net.canarymod.plugin.Plugin;
import net.stuxcrystal.simpledev.configuration.ConfigurationLoader;
import net.stuxcrystal.simpledev.configuration.parser.Constructor;

/**
 * Canary-Recode version for a configuration loader.<p />
 * <p/>
 * Automatically hooks into the logging system of the plugin and uses
 * the configuration storage system given by canary-mod.
 */
public class CanaryConfigurationLoader extends ConfigurationLoader {

    /**
     * Uses the default parameters for the configuration loader.
     *
     * @param plugin The plugin that the logger should use.
     */
    public CanaryConfigurationLoader(Plugin plugin) {
        super(new CanaryConfigurationStorage(plugin));
        this.setLoggingInterface(new Log4jBinding(plugin.getLogman()));
    }

    /**
     * Uses the given constructor to initialize the configuration loader.
     *
     * @param plugin      The plugin which logger should be used.
     * @param constructor The constructor-object that adds the types and resolvers of the configuration loader.
     */
    public CanaryConfigurationLoader(Plugin plugin, Constructor constructor) {
        super(new CanaryConfigurationStorage(plugin), constructor);
        this.setLoggingInterface(new Log4jBinding(plugin.getLogman()));
    }
}
