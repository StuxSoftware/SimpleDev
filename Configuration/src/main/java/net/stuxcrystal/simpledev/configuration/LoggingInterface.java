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

package net.stuxcrystal.simpledev.configuration;

/**
 * Interface to connect to the logging system used.
 */
public interface LoggingInterface {

    /**
     * Warns the user.
     *
     * @param message Message to send.
     */
    public void warn(String message);

    /**
     * Returns a debug message.
     * @param message The debug message.
     */
    public void debug(String message);

    /**
     * Exception thrown in debug-mode.<p />
     *
     * Use this method to declare a minor exception that
     * does not affect the load in any way.
     *
     * @param exception The thrown exception.
     */
    public void debugException(Throwable exception);

    /**
     * Send a major exception into the logging system.<p />
     *
     * Use this method to show exception that can affect
     * the load if the configuration.
     *
     * @param exception The thrown exception.
     */
    public void exception(Throwable exception);
}
