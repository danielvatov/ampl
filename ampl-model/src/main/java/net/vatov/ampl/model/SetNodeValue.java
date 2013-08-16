/*******************************************************************************
 * Copyright (c) 2008-2013 VMware, Inc. All rights reserved.
 ******************************************************************************/
package net.vatov.ampl.model;

import net.vatov.ampl.model.SetExpression.SetExpressionType;

public class SetNodeValue {

    public enum SetOperationType {
        UNION, INTER, DIFF, SYMDIFF, CROSS;

        public static SetOperationType parse(String value) {
            return SetOperationType.valueOf(value.toUpperCase());
        }
    }

    private SetOperationType operation;
    private SetExpression[] operands;

    public SetNodeValue(SetOperationType operation, SetExpression... operands) {
        this.operation = operation;
        this.operands = operands;
    }

    public SetOperationType getOperation() {
        return operation;
    }

    public SetExpression[] getOperands() {
        return operands;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append("'").append(operation).append("' ");
        if (null == operands) {
            return ret.toString();
        }
        for (SetExpression e : operands) {
            if (e.getType().equals(SetExpressionType.VARREF)) {
                ret.append(e.getSymbolDeclaration().getName());
            } else {
                ret.append(e.toString());
            }
            ret.append(", ");
        }
        ret.delete(ret.length()-2, ret.length());
        return ret.toString();
    }

    @Override
    public Object clone() {
        SetExpression[] o = new SetExpression[operands.length];
        for (int i = 0; i < operands.length; ++i) {
            o[i] = operands[i];
        }
        return new SetNodeValue(operation, o);
    }
}
