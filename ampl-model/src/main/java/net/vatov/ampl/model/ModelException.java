/**
 *  Copyright (C) 2010 by Daniel Vatov
 */
package net.vatov.ampl.model;

public class ModelException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ModelException(String message) {
        super(message);
    }

    public ModelException(Throwable cause) {
        super(cause);
    }
}
