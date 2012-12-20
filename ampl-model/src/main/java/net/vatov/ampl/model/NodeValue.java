/**
 *  Copyright (C) 2010 by Daniel Vatov
 */
package net.vatov.ampl.model;

public class NodeValue implements Cloneable {

    public enum BuiltinFunction {
        ABS, ACOS, ACOSH, ASIN, ASINH, ATAN, ATAN2, ATANH, CEIL,
        CTIME, COS, EXP, FLOOR, LOG, LOG10, MAX, MIN, PRECISION,
        ROUND, SIN, SINH, SQRT, TAN, TANH, TIME, TRUNC;
    }

    public enum OperationType {
        PLUS, MINUS, MULT, DIV_SLASH, MOD, DIV, POW, BUILTIN_FUNCTION, UNARY_MINUS, UNARY_PLUS;
    }

    private OperationType operation;
    private Expression[] operands;
    private BuiltinFunction fType;

    public BuiltinFunction getBuiltinFunction() {
        checkBuiltinFunction();
        return fType;
    }

    void setBuiltinFunction(BuiltinFunction fType) {
        checkBuiltinFunction();
        this.fType = fType;
    }

    private void checkBuiltinFunction() {
        if (!operation.equals(OperationType.BUILTIN_FUNCTION)) {
            throw new ModelException("Only " + OperationType.BUILTIN_FUNCTION.name()
                    + " operations can use builtinFunction");
        }
    }

    public NodeValue(OperationType operation, Expression... operands) {
        this.operation = operation;
        this.operands = operands;
    }

    public OperationType getOperation() {
        return operation;
    }

    public Expression[] getOperands() {
        return operands;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("'").append(operation).append("' ");
        if (OperationType.BUILTIN_FUNCTION.equals(operation)) {
            ret.append("'").append(fType).append("' ");
        }
        if (null == operands) {
            return ret.toString();
        }
        for (Expression e : operands) {
            ret.append(e.toString());
        }
        return ret.toString();
    }

    @Override
    public Object clone() {
        Expression[] o = new Expression[operands.length];
        for (int i = 0; i < operands.length; ++i) {
            o[i] = operands[i];
        }
        return new NodeValue(operation, o);
    }
}
