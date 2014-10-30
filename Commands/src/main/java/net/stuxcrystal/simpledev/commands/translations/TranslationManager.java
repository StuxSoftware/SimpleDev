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

package net.stuxcrystal.simpledev.commands.translations;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import net.stuxcrystal.simpledev.commands.translations.contrib.EnumResolver;
import net.stuxcrystal.simpledev.commands.translations.contrib.PrefixingResolver;
import net.stuxcrystal.simpledev.commands.utils.MessageColor;
import org.apache.commons.lang.text.StrSubstitutor;

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
                "[CommandHandler::Scheduler] Your platform does not support scheduling... Using internal scheduler."
        );
        defaults.put(
                "internal.permissions.op-default",
                "[CommandHandler::Permissions] Your platform does not support permission-systems... Falling back to op."
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
     * Constructor to ensure that no TranslationManager overrides another translation.
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

        return null;
    }

    /**
     * Returns the object that is associated with the value.
     *
     * @param executor   The executor which may be queried.
     * @param name       The name of the object.
     * @return The object.
     */
    public Object getValue(CommandExecutor executor, String name) {
        for (ValueResolver resolver : this.resolvers) {
            Object result = resolver.get(this, executor, name);
            if (result != null)
                return result;
        }
        return null;
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
