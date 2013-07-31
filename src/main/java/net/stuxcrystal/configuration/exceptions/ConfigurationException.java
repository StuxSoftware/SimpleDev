package net.stuxcrystal.configuration.exceptions;

/**
 * Base-Class for Exceptions.
 *
 * @author StuxCrystal
 */
public class ConfigurationException extends Exception {

    private static final long serialVersionUID = 1L;

    ConfigurationException() {
    }

    ConfigurationException(String arg0) {
        super(arg0);
    }

    ConfigurationException(Throwable arg0) {
        super(arg0);
    }

    ConfigurationException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    ConfigurationException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

}
