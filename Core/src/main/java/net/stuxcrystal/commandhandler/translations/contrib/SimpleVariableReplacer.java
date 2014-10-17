package net.stuxcrystal.commandhandler.translations.contrib;

import net.stuxcrystal.commandhandler.translations.VariableReplacer;

import java.util.Map;

/**
 * Simple variable replacer.
 */
public class SimpleVariableReplacer implements VariableReplacer {

    /**
     * The default prefix of the replacer.
     */
    private static final String DEFAULT_PREFIX = "${";

    /**
     * The default suffix of the replacer.
     */
    private static final String DEFAULT_SUFFIX = "}";

    /**
     * The prefix for the values.
     */
    public final String prefix;

    /**
     * The suffix for the values.
     */
    public final String suffix;

    /**
     * Creates a new variable replacer with the given prefix and suffix.
     * @param prefix The prefix to use.
     * @param suffix The suffix to use.
     */
    public SimpleVariableReplacer(String prefix, String suffix) {
        this.prefix = prefix;
        this.suffix = suffix;
    }

    /**
     * Creates a new variable replacer with the default prefix and suffix.
     */
    public SimpleVariableReplacer() {
        this(SimpleVariableReplacer.DEFAULT_PREFIX, SimpleVariableReplacer.DEFAULT_SUFFIX);
    }

    @Override
    public String replaceVariables(String text, Map<String, String> variables) {
        String current = text;
        for (Map.Entry<String, String> entry : variables.entrySet())
            current = current.replace(this.prefix + entry.getKey() + this.suffix, entry.getValue());
        return current;
    }
}
