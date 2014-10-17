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

package net.stuxcrystal.commandhandler.translations;

import net.stuxcrystal.commandhandler.CommandExecutor;
import net.stuxcrystal.commandhandler.translations.contrib.EnumResolver;
import net.stuxcrystal.commandhandler.translations.contrib.PrefixingResolver;
import net.stuxcrystal.commandhandler.translations.contrib.SimpleVariableReplacer;
import net.stuxcrystal.commandhandler.utils.MessageColor;

import java.util.*;

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
     * Contains all value resolvers.
     */
    private List<ValueResolver> resolvers = new ArrayList<>(Arrays.asList(
            new ValueResolver[] {
                    // Make it a default to support colors.
                    new PrefixingResolver("color:", new EnumResolver(MessageColor.class))
            }
    ));

    /**
     * The replacer that replaces the variables.
     */
    private VariableReplacer replacer = new SimpleVariableReplacer();

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
        // Errors
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

        // Other things.
        defaults.put("cmd.help.base", "${0} - ${1}");

        // Internal warnings.
        defaults.put(
                "internal.threading.no-scheduler",
                "Your backend does not support scheduling... Falling back to standard threads."
        );

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
     * Translates a given text with variables.
     *
     * @param sender The sender.
     * @param key    The key
     * @param values The values.
     * @return The translated mesage.
     */
    public String translate(CommandExecutor sender, String key, Object... values) {
        String result;
        if (handler == null || (result = handler.getTranslation(sender, key)) == null) {
            result = defaults.get(key);
        }
        if (result == null)
            result = key;

        // Add the default formatters.
        Map<String, String> formatMap = new HashMap<String, String>();
        for (ValueResolver resolver : this.resolvers) {
            formatMap.putAll(resolver.getFormatMap(sender));
        }

        // Add the values to the translator.
        for (int i = 0; i<values.length; i++) {
            formatMap.put("" + i, values[i].toString());
        }

        // Put in the variables.
        return this.replacer.replaceVariables(result, formatMap);
    }

    /**
     * Sets the {@link TranslationHandler} that is providing the translations.
     *
     * @param handler The handler providing the translations.
     */
    public void setTranslationHandler(TranslationHandler handler) {
        this.handler = handler;
    }

    /**
     * Changes the {@link VariableReplacer} that is replacing the
     * translations.
     * @param replacer The replacer for the variables.
     */
    public void setVariableReplacer(VariableReplacer replacer) {
        this.replacer = replacer;
    }

    /**
     * Adds an {@link ValueResolver} that is providing the default variables.
     * @param resolver The resolver to add.
     */
    public void addValueResolver(ValueResolver resolver) {
        this.resolvers.add(resolver);
    }

    /**
     * Removes an {@link ValueResolver} from this translation manager.
     * @param resolver The resolver to remove.
     */
    public void removeValueResolver(ValueResolver resolver) {
        this.resolvers.remove(resolver);
    }

}
