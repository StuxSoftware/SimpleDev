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

import java.util.logging.Logger;

/**
 * Backend of the command handler. Used to communicate with the program.
 *
 * @param <T> The type of the backend.
 */
public interface Backend<T> {

    /**
     * Called if the logger has to be used.
     *
     * @return Reference to the logger.
     */
    public Logger getLogger();

    /**
     * Schedules an asynchronous task.
     *
     * @param runnable
     */
    public void schedule(Runnable runnable);

    /**
     * Returns an CommandExecutor by the given name.
     *
     * @param name The name of the executor. If null or empty, the console executor will be returned.
     */
    public CommandExecutor<?> getExecutor(String name);

    /**
     * @return The handle.
     */
    public T getHandle();

    /**
     * Checks if the user has the permission.
     *
     * @param executor The executor.
     * @param node     The node to use.
     * @return null, if the given backend does not support permissions.
     */
    public Boolean hasPermission(CommandExecutor<?> executor, String node);

}
