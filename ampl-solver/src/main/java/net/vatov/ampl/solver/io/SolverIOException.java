package net.vatov.ampl.solver.io;

public class SolverIOException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public SolverIOException(String message) {
        super(message);
    }
    
    public SolverIOException(Throwable e) {
        super(e);
    }
}
