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

package net.stuxcrystal.configuration.logging;

import net.stuxcrystal.configuration.LoggingInterface;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Binding to java.util.logging. Use this for Bukkit.
 */
public class JULBinding implements LoggingInterface {

    private final Logger logger;

    public JULBinding(Logger logger) {
        this.logger = logger;
    }

    @Override
    public void warn(String message) {
        this.logger.warning(message);
    }

    @Override
    public void debug(String message) {
        this.logger.fine(message);
    }

    @Override
    public void debugException(Throwable exception) {
        this.logger.log(Level.FINE, "Minor exception.", exception);
    }

    @Override
    public void exception(Throwable exception) {
        this.logger.log(Level.WARNING, "Major exception.", exception);
    }
}
