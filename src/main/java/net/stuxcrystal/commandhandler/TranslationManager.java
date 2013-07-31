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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A manager for translations.
 *
 * @author StuxCrystal
 */
public class TranslationManager {

    /**
     * The handler that is handling the translations.
     */
    private TranslationHandler handler = null;

    /**
     * Represents the default translations of the system.
     */
    private static final Map<String, String> defaults = createDefaults();

    /**
     * Default Translations for Command-Fails.<p />
     * <p/>
     * Made static to prevent modifiable defaults.
     */
    private static Map<String, String> createDefaults() {
        Map<String, String> defaults = new HashMap<String, String>();
        defaults.put("cmd.notfound", "Command not found.");
        defaults.put("cmd.exception", "An exception occured while executing the command.");
        defaults.put("cmd.call.fail", "Failed to call command.");
        defaults.put("cmd.check.oponly", "You have to execute this command as an op.");
        defaults.put("cmd.check.noplayer", "This command cannot be executed by a player.");
        defaults.put("cmd.check.noconsole", "This command cannot be executed from the console.");
        defaults.put("cmd.check.permission", "You don't have permission.");
        defaults.put("cmd.check.flag", "Invalid flag.");
        defaults.put("cmd.check.args.min", "Too few arguments.");
        defaults.put("cmd.check.args.max", "Too many arguments.");

        return Collections.unmodifiableMap(defaults);
    }

    /**
     * @return Returns the defaults.
     */
    public static Map<String, String> getDefaults() {
        return Collections.unmodifiableMap(defaults);
    }

    /**
     * Private Constructor to ensure that no TranslationManager overrides another translation.
     */
    public TranslationManager() {

    }

    /**
     * Translates a given text
     *
     * @param sender
     * @param key
     * @return
     */
    public String translate(CommandExecutor sender, String key) {
        String result;
        if (handler == null || (result = handler.getTranslation(sender, key)) == null) {
            result = defaults.get(key);
        }
        if (result == null)
            result = key;

        return result;

    }

    /**
     * Sets the {@link TranslationHandler} that is providing the translations.
     *
     * @param handler The handler providing the translations.
     */
    public void setTranslationHandler(TranslationHandler handler) {
        this.handler = handler;
    }

}
