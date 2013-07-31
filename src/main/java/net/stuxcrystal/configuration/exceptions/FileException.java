package net.stuxcrystal.configuration.exceptions;

/**
 * Wrapper for IOExceptions.
 *
 * @author StuxCrystal
 */
public class FileException extends ConfigurationException {

    private static final long serialVersionUID = 1L;

    public FileException() {
    }

    public FileException(String arg0) {
        super(arg0);
    }

    public FileException(Throwable arg0) {
        super(arg0);
    }

    public FileException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public FileException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

}
