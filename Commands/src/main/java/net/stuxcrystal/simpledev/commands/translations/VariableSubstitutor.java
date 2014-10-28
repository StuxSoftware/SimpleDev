package net.stuxcrystal.simpledev.commands.translations;

import net.stuxcrystal.simpledev.commands.CommandExecutor;
import org.apache.commons.lang.text.StrLookup;
import org.apache.commons.lang.text.StrSubstitutor;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * Simple variable substitutor for repetitive use.
 */
public class VariableSubstitutor {

    /**
     * The StrLookup that is used to construct the StrLookup.
     */
    private class VariableRegistry extends StrLookup {

        /**
         * The executor that will be used passed to the {@link net.stuxcrystal.simpledev.commands.translations.ValueResolver}.
         */
        private final CommandExecutor executor;

        /**
         * Creates a new variable-registry object.
         * @param executor The executor that will be passed to the ValueResolvers.
         */
        private VariableRegistry(CommandExecutor executor) {
            this.executor = executor;
        }

        @Override
        public String lookup(String s) {
            return VariableSubstitutor.this.lookup(this.executor, s);
        }
    }

    /**
     * Translation Manager for the
     */
    private final TranslationManager manager;

    /**
     * The value substitutor.
     * @param manager The manager used for translations.
     */
    public VariableSubstitutor(TranslationManager manager) {
        this.manager = manager;
    }

    /**
     * Look up the values. (This is a fairly complicated process as we will be doing variable-resolving.)
     * @param executor  The executor that is queried.
     * @param string    The string to be resolved.
     * @return The resolved string.
     */
    private String lookup(CommandExecutor executor, String string) {
        // Get the locale of the user.
        Locale locale = Locale.getDefault();
        if (executor.getCommandHandler().hasFunction("getLocale", CommandExecutor.class)) {
            locale = ((LocaleResolver)executor.getComponent(LocaleResolver.class)).getLocale();
        }

        // Parse the lookup.
        String[] values = string.split(",", 2);

        String value = values[0];
        String format = null;
        if (values.length == 2)
            format = values[1];

        // Pass it to a message formatter.
        StringBuilder sb = new StringBuilder("{0");
        if (format != null)
            sb.append(",").append(format);
        sb.append("}");

        // Resolve the value
        Object obj = this.manager.getValue(executor, string);

        // Pass it to a message format and actually format it.
        MessageFormat mf = new MessageFormat(sb.toString(), locale);
        return mf.format(obj);
    }
}
