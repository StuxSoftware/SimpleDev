package net.stuxcrystal.commandhandler.component;

/**
 * What states of synchronization is allowed?
 */
public enum SynchronizationState {

    /**
     * Just execute the function.
     */
    IGNORE,

    /**
     * Execute the function in the main server thread.
     */
    SYNCHRONOUS,

    /**
     * <p>Execute the function asynchronously.</p>
     * <p>Will always return a "future" object.</p>
     */
    ASYNCHRONOUS

}
