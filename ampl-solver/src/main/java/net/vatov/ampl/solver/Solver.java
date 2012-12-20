/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.solver;

import java.io.InputStream;
import java.util.Map;

import net.vatov.ampl.AmplParser;
import net.vatov.ampl.model.OptimModel;
import net.vatov.ampl.solver.io.UserIO;


public abstract class Solver {

    public abstract String getName();
    public abstract String getDescription();
    //TODO връщане на по-смислен резултат
	public abstract Map<String, String> solve(InputStream input, UserIO io);
	   
    protected OptimModel parse(InputStream input) {
        return new AmplParser().parse(input);
    }
}
