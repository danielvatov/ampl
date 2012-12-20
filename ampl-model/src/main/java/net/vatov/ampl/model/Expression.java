/**
 *  Copyright (C) 2010 by Daniel Vatov
 */
package net.vatov.ampl.model;

import java.util.List;

import net.vatov.ampl.model.NodeValue.BuiltinFunction;
import net.vatov.ampl.model.NodeValue.OperationType;


public class Expression implements Cloneable {
    public static enum ExpressionType {
        DOUBLE, TREE, SYMREF;
    }

    private ExpressionType type;
    private Object value;

    public ExpressionType getType() {
        return type;
    }

    /**
     * В случая на константен израз се присвоява стойността на израза
     * 
     * @param value
     */
    public void setValue(Double value) {
        this.value = value;
        type = ExpressionType.DOUBLE;
    }

    /**
     * Референция към декларирана по-рано променлива
     * 
     * @param symref
     */
    public void setSymRef(SymbolDeclaration symref) {
        value = symref;
        type = ExpressionType.SYMREF;
    }

    /**
     * 
     * @param operation
     *            код на операция както е дефинирана от кодовете на токените в
     *            лексера
     * @param operands
     *            обекти от клас Expresson
     */
    public void setTreeValue(OperationType operation, Expression... operands) {
        value = new NodeValue(operation, operands);
        type = ExpressionType.TREE;
    }

    /**
     * 
     * @param function
     *            име на вградена функция
     * @param params
     *            обекти от клас Expresson
     */
    public void setTreeValue(BuiltinFunction function, List<Expression> params) {
        Expression[] arr = null;
        if (params != null) {
            arr = params.toArray(new Expression[params.size()]);
        }
        value = new NodeValue(OperationType.BUILTIN_FUNCTION, arr);
        ((NodeValue)value).setBuiltinFunction(function);
        type = ExpressionType.TREE;
    }
    public Double getValue() {
        if (type != ExpressionType.DOUBLE) {
            throw new ModelException("Not a simple value");
        }
        return (Double) value;
    }

    public NodeValue getTreeValue() {
        if (type != ExpressionType.TREE) {
            throw new ModelException("Not a tree");
        }
        return (NodeValue) value;
    }

    public SymbolDeclaration getSymRef() {
        if (type != ExpressionType.SYMREF) {
            throw new ModelException("Not a symbol reference");
        }
        return (SymbolDeclaration) value;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        return ret.append("[").append(type.toString()).append(":").append(value.toString()).append("]").toString();
    }

    @Override
    public Object clone() {
        Expression e = new Expression();
        e.type = type;
        switch (type) {
        case DOUBLE:
            e.value = getValue();
            break;
        case TREE:
            e.value = getTreeValue().clone();
            break;
        case SYMREF:
            e.value = getSymRef().clone();
            break;
        default:
            throw new ModelException("type not supported " + type);
        }
        return e;
    }
}