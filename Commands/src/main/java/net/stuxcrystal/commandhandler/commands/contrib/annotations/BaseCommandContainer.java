package net.stuxcrystal.commandhandler.commands.contrib.annotations;

import net.stuxcrystal.commandhandler.CommandExecutor;

/**
 * Base class for command containers.<p />
 *
 * With some nice features.
 */
public class BaseCommandContainer implements CommandListener {

    /**
     * Translates a string
     *
     * @param executor Executor that should receive the message.
     * @param key      The key that receives the message.
     * @param values   The values that are used in the message.
     * @return The translated message.
     */
    public String _(CommandExecutor executor, String key, Object... values) {
        return executor.getCommandHandler().getTranslationManager().translate(executor, key, values);
    }

}
