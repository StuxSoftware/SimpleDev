package net.stuxcrystal.simpledev.commandhandler.translations;

import java.util.Map;

/**
 * This interface is designed
 */
public interface VariableReplacer {

    /**
     * The actual replaces for variables.
     * @param text       The values to replace.
     * @param variables  The variables.
     * @return The replaced string.
     */
    public String replaceVariables(String text, Map<String, String> variables);

}
