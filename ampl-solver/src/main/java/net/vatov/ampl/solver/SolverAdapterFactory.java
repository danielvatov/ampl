/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.solver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.vatov.ampl.solver.impl.LpSolverAdapter;
import net.vatov.ampl.solver.impl.SolverNotFoundException;


public class SolverAdapterFactory {

    private static List<String> solvers = new ArrayList<String>();
    
    static {
        solvers.add(LpSolverAdapter.NAME);
    }
    
    public static Solver getSolverAdapter(String name) {
        // TODO използване на reflection?
        if (LpSolverAdapter.NAME.equals(name)) {
            return new LpSolverAdapter();
        }
        throw new SolverNotFoundException();
    }

    public static List<String> getSupportedSolvers() {
        return Collections.unmodifiableList(solvers);
    }
}
