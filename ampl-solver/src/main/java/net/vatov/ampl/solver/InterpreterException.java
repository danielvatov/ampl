package net.vatov.ampl.solver;

public class InterpreterException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public InterpreterException() {
        super();
    }
    
    public InterpreterException(Throwable e) {
        super(e);
    }
    
    public InterpreterException(String message) {
        super(message);
    }
}
