package net.stuxcrystal.configuration.logging;

import net.stuxcrystal.configuration.LoggingInterface;

/**
 * Binding to the error stream.<p />
 * <p/>
 * This is the default binding.
 */
public class ErrorStreamBinding implements LoggingInterface {

    @Override
    public void warn(String message) {
        System.err.println(message);
    }
}
