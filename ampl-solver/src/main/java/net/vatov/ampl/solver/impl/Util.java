/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.solver.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.vatov.ampl.model.Expression;
import net.vatov.ampl.model.ModelException;
import net.vatov.ampl.model.SymbolDeclaration;
import net.vatov.ampl.model.Expression.ExpressionType;
import net.vatov.ampl.model.NodeValue.OperationType;


public class Util {

    static List<SymbolDeclaration> getSymRefs(Expression e, List<SymbolDeclaration> symRefs) {
        switch (e.getType()) {
        case DOUBLE:
            return symRefs;
        case SYMREF:
            symRefs.add(e.getSymRef());
            return symRefs;
        case TREE:
            Expression[] expressions = e.getTreeValue().getOperands();
            for (Expression ex : expressions) {
                getSymRefs(ex, symRefs);
            }
            return symRefs;
        default:
            throw new ModelException(e.getType().toString());
        }
    }

    static Map<SymbolDeclaration, Double> getPolinomCoeff(Expression e, Map<SymbolDeclaration, Double> coeffs) {
        return getPolinomCoeff(e, coeffs, OperationType.PLUS);
    }
    
    static Map<SymbolDeclaration, Double> getPolinomCoeff(Expression e, Map<SymbolDeclaration, Double> coeffs, OperationType parentOp) {
        SymbolDeclaration sd = null;
        if (isNormalMonom(e)) {
            Double coef = 0.0;
            int sign = 1;
            if (OperationType.MINUS == parentOp) {
                sign = -1;
            }
            if (e.getTreeValue().getOperands()[0].getType().equals(ExpressionType.DOUBLE)) {
                coef = e.getTreeValue().getOperands()[0].getValue() * sign;
                sd = e.getTreeValue().getOperands()[1].getSymRef();
            } else {
                coef = e.getTreeValue().getOperands()[1].getValue() * sign;
                sd = e.getTreeValue().getOperands()[0].getSymRef();                
            }
            if (coeffs.containsKey(sd)) {
                throw new PolinomException(sd.getName());
            }
            coeffs.put(sd, coef);
        } else if (e.getType().equals(ExpressionType.SYMREF)) {
            sd = e.getSymRef();
            if (coeffs.containsKey(sd)) {
                throw new PolinomException(sd.getName());
            }
            coeffs.put(sd, 1.0);
        } else if (e.getType().equals(ExpressionType.TREE)) {
            OperationType op = e.getTreeValue().getOperation();
            if (!(OperationType.PLUS == op || OperationType.MINUS == op)) {
                throw new PolinomException(op.toString());
            }
            for (Expression ex : e.getTreeValue().getOperands()) {
                getPolinomCoeff(ex, coeffs, op);
            }
        } else {
            throw new PolinomException(e.getType().toString());
        }
        return coeffs;
    }
    
    static boolean isNormalMonom(Expression e) {
        return e.getType().equals(ExpressionType.TREE)
        && e.getTreeValue().getOperation() == OperationType.MULT
        && 2 == e.getTreeValue().getOperands().length
        && (e.getTreeValue().getOperands()[0].getType().equals(ExpressionType.SYMREF) ^ 
                e.getTreeValue().getOperands()[1].getType().equals(ExpressionType.SYMREF))
        && (e.getTreeValue().getOperands()[0].getType().equals(ExpressionType.DOUBLE) ^ 
                e.getTreeValue().getOperands()[1].getType().equals(ExpressionType.DOUBLE));
    }
    
    static double[] sortCoeffs(List<SymbolDeclaration> sds, Expression e) {
        Map<SymbolDeclaration, Double> polinomCoeff = Util.getPolinomCoeff(e,
                new HashMap<SymbolDeclaration, Double>(sds.size()));
        double[] coeffs = new double[sds.size()];
        int idx = 0;
        for (SymbolDeclaration sd : sds) {
            if (!polinomCoeff.containsKey(sd)) {
                coeffs[idx] = 0.0;
            } else {
                coeffs[idx] = polinomCoeff.get(sd);
            }
            idx++;
        }
        return coeffs;
    }
}