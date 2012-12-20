/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.solver;

import java.io.InputStream;

import net.vatov.ampl.solver.impl.LpSolverAdapter;

import org.junit.Test;


public class LpSolverAdapterTest {

    @Test
    public void testSolve() throws Exception {
        InputStream is = getClass().getResourceAsStream("lp.mod");
        LpSolverAdapter s = new LpSolverAdapter();
        s.solve(is, null);
    }
}
