package net.stuxcrystal.configuration.exceptions;

/**
 * Wrapper for ReflectiveOperationExceptions.
 *
 * @author StuxCrystal
 */
public class ReflectionException extends ConfigurationException {

    private static final long serialVersionUID = 1L;

    public ReflectionException() {
    }

    public ReflectionException(String arg0) {
        super(arg0);
    }

    public ReflectionException(Throwable arg0) {
        super(arg0);
    }

    public ReflectionException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ReflectionException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }
}
