package net.stuxcrystal.commandhandler;

/**
 * Thrown when the command handler should execute the given subcommand.
 *
 * @author StuxCrystal
 */
public class DoNotExecuteException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DoNotExecuteException() {
    }

    public DoNotExecuteException(String arg0) {
        super(arg0);
    }

    public DoNotExecuteException(Throwable arg0) {
        super(arg0);
    }

    public DoNotExecuteException(String arg0, Throwable arg1) {
        super(arg0, arg1);
    }

    public DoNotExecuteException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
        super(arg0, arg1, arg2, arg3);
    }

}
