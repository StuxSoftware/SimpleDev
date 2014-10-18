package net.stuxcrystal.commandhandler.component;

/**
 * Failed to access a component function
 */
public class ComponentAccessException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ComponentAccessException() {
    }

    public ComponentAccessException(String arg0) {
        super(arg0);
    }

    public ComponentAccessException(Throwable arg0) {
        super(arg0);
    }

    public ComponentAccessException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public ComponentAccessException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }
}
