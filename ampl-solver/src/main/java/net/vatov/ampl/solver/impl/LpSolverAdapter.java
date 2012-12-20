/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.solver.impl;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lpsolve.LpSolve;
import lpsolve.LpSolveException;
import net.vatov.ampl.model.ConstraintDeclaration;
import net.vatov.ampl.model.Expression.ExpressionType;
import net.vatov.ampl.model.ObjectiveDeclaration;
import net.vatov.ampl.model.ObjectiveDeclaration.Goal;
import net.vatov.ampl.model.OptimModel;
import net.vatov.ampl.model.SymbolDeclaration;
import net.vatov.ampl.solver.Solver;
import net.vatov.ampl.solver.io.UserIO;

public class LpSolverAdapter extends Solver {

    public static String NAME = "lp_solve";

    @Override
    public Map<String, String> solve(InputStream input, UserIO io) {
        try {
            OptimModel model = parse(input);
            List<SymbolDeclaration> sds = Util.getSymRefs(model.getObjectives().get(0).getExpression(),
                    new ArrayList<SymbolDeclaration>());
            // Create a problem with 4 variables and 0 constraints
            LpSolve solver = LpSolve.makeLp(0, sds.size());

            double[] coeffs;
            for (ConstraintDeclaration cd : model.getConstraints()) {
                coeffs = Util.sortCoeffs(sds, cd.getaExpr());
                int rel = 0;
                switch (cd.getRelop()) {
                case GE:
                    rel = LpSolve.GE;
                    break;
                case EQ:
                    rel = LpSolve.EQ;
                    break;
                case LE:
                    rel = LpSolve.LE;
                    break;
                }
                if (!cd.getbExpr().getType().equals(ExpressionType.DOUBLE)) {
                    throw new RuntimeException("Not implemented");
                }
                solver.addConstraint(coeffs, rel, cd.getbExpr().getValue());
            }
            ObjectiveDeclaration goal = model.getObjectives().get(0);
            coeffs = Util.sortCoeffs(sds, goal.getExpression());
            solver.setObjFn(coeffs);

            if (goal.getGoal().equals(Goal.MAXIMIZE)) {
                solver.setMaxim();
            } else {
                solver.setMinim();
            }

            solver.solve();

            Map<String, String> ret = new HashMap<String, String>();
            ret.put("solution", Double.toString(solver.getObjective()));

            double[] var = solver.getPtrVariables();
            for (int i = 0; i < var.length; i++) {
                ret.put("var[" + i + "]", Double.toString(var[i]));
            }

            solver.deleteLp();
            return ret;
        } catch (LpSolveException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String getDescription() {
        return "lp_solve is a free (see LGPL for the GNU lesser general public license) linear (integer) programming solver based on the revised simplex method and the Branch-and-bound method for the integers.";
    }
}
