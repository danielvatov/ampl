/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.solver.impl;

public class SolverNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public SolverNotFoundException() {
		super();
	}
	
	public SolverNotFoundException(Throwable e) {
		super(e);
	}
	
	public SolverNotFoundException(String message) {
		super(message);
	}
}
