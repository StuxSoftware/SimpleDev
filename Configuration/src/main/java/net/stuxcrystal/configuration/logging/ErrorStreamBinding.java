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

/**
 * Binding to the error stream.<p />
 * <p/>
 * This is the default binding.
 */
public class ErrorStreamBinding implements LoggingInterface {

    @Override
    public void warn(String message) {
        System.err.println(message);
    }

    @Override
    public void debug(String message) {
        // Silently drop the message.
    }

    @Override
    public void debugException(Throwable exception) {
        // Silently drop the exception.
    }

    @Override
    public void exception(Throwable exception) {
        exception.printStackTrace();
    }
}
