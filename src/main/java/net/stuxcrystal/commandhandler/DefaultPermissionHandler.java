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

package net.stuxcrystal.commandhandler;

/**
 * The default permission handler.
 */
public class DefaultPermissionHandler implements PermissionHandler {

    private final CommandHandler handler;

    private boolean warning = false;

    public DefaultPermissionHandler(CommandHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean hasPermission(CommandExecutor<?> executor, String node) {
        Boolean result = this.handler.getServerBackend().hasPermission(executor, node);
        if (result == null) {
            if (!warning) {
                handler.backend.getLogger().warning("[CommandHandler] The given backend does not support a permission-system. Defaulting to op.");
                warning = true;
            }

            return executor.isOp();
        }

        return result;
    }
}