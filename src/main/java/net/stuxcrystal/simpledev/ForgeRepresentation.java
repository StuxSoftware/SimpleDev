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

import cpw.mods.fml.common.FMLLog;
import net.stuxcrystal.commandhandler.compat.forge.ForgeCommandHandler;
import net.stuxcrystal.configuration.compat.ForgeConfigurationLoader;

import java.util.logging.Logger;

/**
 * The forge-simpledev-bridge.
 */
public class ForgeRepresentation<T> extends ServerRepresentation<ForgeCommandHandler<T>, ForgeConfigurationLoader, T> {

    /**
     * The logger.
     */
    private final Logger logger;

    public ForgeRepresentation(T mod) {
        this(mod, FMLLog.getLogger());
    }

    public ForgeRepresentation(T mod, Logger logger) {
        super(mod);
        this.logger = logger;
    }

    @Override
    public ForgeCommandHandler<T> createCommandHandler() {
        return new ForgeCommandHandler<T>(this.plugin, this.logger);
    }

    @Override
    public ForgeConfigurationLoader getConfigurationLoader() {
        return new ForgeConfigurationLoader(this.logger);
    }
}
