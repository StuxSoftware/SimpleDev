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

import net.stuxcrystal.commandhandler.CommandHandler.CommandData;

class CommandExecutionTask implements Runnable {

    private final CommandData data;

    private final CommandExecutor sender;

    private final ArgumentParser arguments;

    CommandExecutionTask(CommandData data, CommandExecutor sender, ArgumentParser arguments) {
        this.data = data;
        this.sender = sender;
        this.arguments = arguments;
    }

    @Override
    public void run() {
        this.data.execute(sender, arguments);
    }

}
