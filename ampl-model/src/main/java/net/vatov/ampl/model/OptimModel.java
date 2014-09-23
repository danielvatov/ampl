/**
 *  Copyright (C) 2010 by Daniel Vatov
 */
package net.vatov.ampl.model;

import java.util.ArrayList;
import java.util.List;

import net.vatov.ampl.model.ConstraintDeclaration.RelopType;
import net.vatov.ampl.model.Expression.ExpressionType;
import net.vatov.ampl.model.ObjectiveDeclaration.Goal;
import net.vatov.ampl.model.SymbolDeclaration.SymbolType;


/**
 * Коренният обект на модела
 */
public class OptimModel {

    private ArrayList<SymbolDeclaration>     symbolDeclarations = new ArrayList<SymbolDeclaration>();
    private ArrayList<ObjectiveDeclaration>  objectives         = new ArrayList<ObjectiveDeclaration>();
    private ArrayList<ConstraintDeclaration> constraints        = new ArrayList<ConstraintDeclaration>();
    private String varConstraintPrefix = "var_";
    
    public ConstraintDeclaration addConstraint(String name, String relop,
            Expression aExpr, Expression bExpr) {
        ConstraintDeclaration c = new ConstraintDeclaration(name, relop, aExpr, bExpr);
        constraints.add(c);
        return c;
    }

    public ObjectiveDeclaration addObjective(String goal,
                                    String name, Expression expression) {
        Goal g = Goal.fromString(goal);
        ObjectiveDeclaration o = new ObjectiveDeclaration(g, name, expression);
        objectives.add(o);
        return o;
    }

    public List<ConstraintDeclaration> getConstraints() {        
        return constraints;
    }

    public ConstraintDeclaration getConstraint(String name) {
        for (ConstraintDeclaration cd : constraints) {
            if (name.equals(cd.getName())) {
                return cd;
            }
        }
        return null;
    }

    public List<ObjectiveDeclaration> getObjectives() {
        return objectives;
    }

    public ArrayList<SymbolDeclaration> getSymbolDeclarations() {
        return symbolDeclarations;
    }

    public boolean paramIsDefined(String name) {
        for (SymbolDeclaration sd : symbolDeclarations) {
            if (sd.getName().equals(name) && SymbolType.PARAM == sd.getType()) {
                return true;
            }
        }
        return false;
    }

    public boolean symbolIsDefined(String name) {
        for (SymbolDeclaration sd : symbolDeclarations) {
            if (sd.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
    
    public Double getParamValue(String name) {
        for (SymbolDeclaration sd : symbolDeclarations) {
            if (sd.getName().equals(name) && SymbolType.PARAM == sd.getType()) {
                Expression e = (Expression) sd.getAttributes().get(
                        SymbolDeclaration.DeclarationAttributeEnum.VALUE);
                if (e.getType() != ExpressionType.DOUBLE) {
                    throw new ModelException("param '" + name
                            + "' is not constant");
                }
                return e.getValue();
            }
        }
        throw new ModelException("param " + name + " not defined");
    }

    public boolean varIsDefined(String name) {
        for (SymbolDeclaration sd : symbolDeclarations) {
            if (sd.getName().equals(name) && SymbolType.VAR == sd.getType()) {
                return true;
            }
        }
        return false;
    }

    public SymbolDeclaration getVarRef(String name) {
        for (SymbolDeclaration sd : symbolDeclarations) {
            if (sd.getName().equals(name) && SymbolType.VAR == sd.getType()) {
                return sd;
            }
        }
        throw new ModelException("var " + name + " not defined");
    }

    public SymbolDeclaration getSetRef(String name) {
        for (SymbolDeclaration sd : symbolDeclarations) {
            if (sd.getName().equals(name) && SymbolType.SET == sd.getType()) {
                return sd;
            }
        }
        throw new ModelException("set " + name + " not defined");
    }
    
    public SymbolDeclaration getSymbolRef(String name) {
        for (SymbolDeclaration sd : symbolDeclarations) {
            if (sd.getName().equals(name)) {
                return sd;
            }
        }
        throw new ModelException("symbol " + name + " not defined");
    }

    public SymbolDeclaration getParamRef(String name) {
        for (SymbolDeclaration sd : symbolDeclarations) {
            if (sd.getName().equals(name) && SymbolType.PARAM == sd.getType()) {
                return sd;
            }
        }
        throw new ModelException("param " + name + " not defined");
    }

    public List<SymbolDeclaration> getVarRefs() {
        List<SymbolDeclaration> ret = new ArrayList<SymbolDeclaration>();
        for (SymbolDeclaration sd : symbolDeclarations) {
            if (SymbolType.VAR == sd.getType()) {
                ret.add(sd);
            }
        }
        return ret;
    }

    public void addSymbolDeclaration(SymbolDeclaration decl) {
        if (symbolDeclarations.contains(decl)) {
            throw new ModelException(decl.getType().toString().toLowerCase()
                    + " " + decl.getName() + " already defined");
        }
        checkName(decl.getName());
        symbolDeclarations.add(decl);
        updateConstraints(decl);
    }

    private void checkName(String name) {
        if (name.startsWith(varConstraintPrefix)) {
            throw new ModelException("identifier can not start with " + varConstraintPrefix + " (" + name
                    + "). It is reserved for variable bound constraints!");
        }
    }

    private void updateConstraints(SymbolDeclaration decl) {
        Expression lowerBound = decl.getLowerBound();
        Expression upperBound = decl.getUpperBound();
        if (null != lowerBound) {
            constraints.add(new ConstraintDeclaration(varConstraintPrefix + decl.getName() + "_lower",
                    RelopType.GE, wrapAsExpression(decl), lowerBound));
        }
        if (null != upperBound) {
            constraints.add(new ConstraintDeclaration(varConstraintPrefix + decl.getName() + "_upper",
                    RelopType.LE, wrapAsExpression(decl), upperBound));
        }
    }

    private Expression wrapAsExpression(SymbolDeclaration decl) {
        Expression e = new Expression();
        e.setSymRef(decl);
        return e;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        if (null != symbolDeclarations && !symbolDeclarations.isEmpty()) {
            ret.append("[Symbols]\n");
            for (SymbolDeclaration sd : symbolDeclarations) {
                ret.append(sd.toString()).append("\n");
            }
        }
        if (null != objectives && !objectives.isEmpty()) {
            ret.append("[Objectives]\n");
            for (ObjectiveDeclaration o : objectives) {
                ret.append(o.toString()).append("\n");
            }
        }
        return ret.toString();
    }
}