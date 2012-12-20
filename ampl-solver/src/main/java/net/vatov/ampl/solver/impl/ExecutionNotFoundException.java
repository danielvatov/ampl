/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.solver.impl;

public class ExecutionNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ExecutionNotFoundException() {
    }
    
    public ExecutionNotFoundException(String message) {
        super(message);
    }
    
    public ExecutionNotFoundException(Throwable e) {
        super(e);
    }
}
