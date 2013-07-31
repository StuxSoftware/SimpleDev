package net.stuxcrystal.commandhandler;

/**
 * Defines when a function is called.
 *
 * @author StuxCrystal
 */
public enum CallTime {
    /**
     * Call the function before the subcommand is processed.
     */
    POST,
    /**
     * Call the function after the subcommand is processed.
     */
    PRE,

    /**
     * Call the function if the the subcommand is not known.
     */
    FALLBACK,

    /**
     * Never call the function.
     */
    NEVER
}
