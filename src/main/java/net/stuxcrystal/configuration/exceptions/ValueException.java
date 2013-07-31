package net.stuxcrystal.configuration.exceptions;

/**
 * Thrown when an invalid value is given.
 *
 * @author StuxCrystal
 */
public class ValueException extends ConfigurationException {

    private static final long serialVersionUID = 1L;

    public ValueException() {
    }

    public ValueException(String arg0) {
        super(arg0);
    }

    public ValueException(Throwable arg0) {
        super(arg0);
    }

    public ValueException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ValueException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

}
