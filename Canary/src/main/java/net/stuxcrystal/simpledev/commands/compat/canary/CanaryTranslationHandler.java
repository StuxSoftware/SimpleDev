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

package net.stuxcrystal.simpledev.commands.compat.canary;

import net.canarymod.Translator;
import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.translations.contrib.simple.TranslationHandler;

/**
 * Translation handler for canary mod.
 */
public class CanaryTranslationHandler implements TranslationHandler {

    /**
     * This translation class uses the inbuilt localization feature of Canary-Recode.
     *
     * @param sender The sender that sent the command.
     * @param key    The Key of the translation.
     * @return The translated message.
     */
    @Override
    public String getTranslation(CommandExecutor sender, String key) {
        return Translator.translate(key);
    }

}
