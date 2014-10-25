package net.stuxcrystal.simpledev.commandhandler.arguments.splitter;

import net.stuxcrystal.simpledev.commandhandler.arguments.ArgumentSplitter;

import java.util.LinkedList;


/**
 * Splits the arguments while maintaining quotes.<p />
 *
 * The escape character can escape any special character denoted in this specification. An escaped character
 * will directly be added to the argument- or flag-content without executing any of the special function of
 * the escape character.<p />
 *
 * Escape-Characters:
 * <ul>
 *     <li>{@code ' '} - Denotes a new argument.</li>
 *     <li>
 *         {@code '"'} or {@code "'"} - Quotes strings, no blank space starts a new character.
 *         (Automatically escapes the other quote character inside it.)
 *     </li>
 *     <li>{@code '-'} - Denotes a flag if a new argument is started. (Only works until the first actual argument is being parsed.</li>
 * </ul>
 *
 * {@code /&lt;command&gt; "-fc0" -l -gf0 --c "ab c d e"ef\ g \"cc0} will be splitted to this:<br />
 * Flags: {@code fc0lg-}<br />
 * Arguments: {@code ['ab c d e', 'ef g', '"cc0']}
 */
public class QuotedSplit implements ArgumentSplitter {

    /**
     * Executes the action.
     */
    private static class QuotedSplitExecutor {

        /**
         * The raw string.
         */
        private final String raw;

        /**
         * The parsed arguments.
         */
        private LinkedList<String> arguments = new LinkedList<String>();

        /**
         * Holds the current value of the argument.
         */
        private StringBuilder cArgument = new StringBuilder();

        /**
         * Contains the currently recognized flags.
         */
        private StringBuilder flags = new StringBuilder();

        /**
         * Is the next character is escaped?
         */
        private boolean isEscaped = false;

        /**
         * Does the parser currently parse flags?
         */
        private boolean inFlag = false;

        /**
         * Does the parser expect flags now?
         */
        private boolean expectFlags = false;

        /**
         * The current quote-char.
         */
        private Character quoteChar = null;

        /**
         * Prepares the quoted split executor.
         */
        private QuotedSplitExecutor(String raw) {
            this.raw = raw;
        }

        /**
         * Adds a character to the appropriate StringBuilder-Instance.
         * @param c The character to add.
         */
        public void addCharacter(char c) {
            // If the character is escaped, remove the escape-mode.
            if (isEscaped) isEscaped = false;

            // If the parser is currently parsing flags,
            if (inFlag && !expectFlags) {
                // Check if the flag is already known.
                if (!flags.toString().contains(new String(new char[]{c})))
                    flags.append(c);

            // If the parser is not parsing a flag,
            } else {
                // But is currently expecting a flag, make the parser don't expecting a flag.
                if (expectFlags) expectFlags = false;

                // Add the character to the argument.
                cArgument.append(c);
            }
        }

        /**
         * Adds the new attribute to the argument list.
         */
        public void newAttribute() {
            if (inFlag) { inFlag = false; }

            arguments.add(cArgument.toString());
            cArgument = new StringBuilder();
        }

        /**
         * Executes the split algorithm.
         */
        public void execute() {

            for (char c : raw.toCharArray()) {

                switch (c) {

                    // Handle spaces.
                    case ' ':
                        // Just add the space if it is escaped.
                        if (isEscaped || quoteChar!=null) {
                            addCharacter(c);
                            continue;
                        }
                        // Ignore the space if the current argument is empty.
                        if (cArgument.length() == 0) continue;

                        // Parse next attribute after this character.
                        newAttribute();
                        continue;

                    // Handle quotes.
                    case '"':
                    case '\'':
                        // Just add the character if it is escaped or quoted with the other quote character.
                        if (isEscaped || !new Character(c).equals(quoteChar)) {
                            addCharacter(c);
                            continue;
                        }

                        // If there is no quote character, set the quote-character to this one.
                        if (quoteChar == null) {
                            quoteChar = c;

                        // Otherwise disable the quote-mode.
                        } else {
                            quoteChar = null;
                        }

                        newAttribute();
                        continue;

                    // Handle flags.
                    case '-':
                        // Sets In-Flag attribute.
                        if (expectFlags && !inFlag && !isEscaped) {
                            inFlag = true;
                            continue;
                        }

                        // Adds a character to the flag (or attribute).
                        addCharacter(c);
                        continue;

                    // Handle other characters.
                    default:
                        addCharacter(c);
                }
            }

            // If there is an argument left, just add the argument.
            if (cArgument.length() > 0) newAttribute();

        }

        /**
         * Parses the value.
         * @param value The value to parse.
         * @return The new value.
         */
        public static String[] getParserResult(String value) {
            QuotedSplitExecutor executor = new QuotedSplitExecutor(value);
            executor.execute();

            executor.arguments.addFirst(executor.flags.toString());

            return executor.arguments.toArray(new String[executor.arguments.size()]);
        }

    }

    @Override
    public String[] split(String args) {
        return QuotedSplitExecutor.getParserResult(args);
    }
}
