/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.solver.impl;

public class PolinomException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public PolinomException() {
    }
    
    public PolinomException(String message) {
        super(message);
    }
    
    public PolinomException(Throwable e) {
        super(e);
    }
}
