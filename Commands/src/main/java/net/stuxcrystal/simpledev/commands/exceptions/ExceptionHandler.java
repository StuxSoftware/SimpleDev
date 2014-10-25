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

package net.stuxcrystal.simpledev.commands.exceptions;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.arguments.ArgumentParser;

/**
 * A handler for exceptions.<p />
 * <p/>
 * Needed to allow the plugin to handle some exceptions itself.
 *
 * @param <T> The class this exception handles.
 */
public interface ExceptionHandler<T extends Throwable> {

    /**
     * If an exception is thrown that cannot be handled by the CommandHandler, an passable exception-handler will
     * be searched.
     *
     * @param exception The exception that was thrown.
     * @param name      The name of the command.
     * @param executor  The sender who executed the command.
     * @param arguments The arguments that were passed to the command.
     */
    public void exception(T exception, String name, CommandExecutor<?> executor, ArgumentParser arguments);

}
