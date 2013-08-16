/**
 *  Copyright (C) 2013 by Daniel Vatov
 */
package net.vatov.ampl.model;

import java.util.Set;

public class SetExpression {
    public enum SetExpressionType {
        VARREF, ENUM, TREE, RANGE
    }

    private SetExpressionType type;
    private Object value;

    public SetExpression(SetExpressionType type, Object value) {
        this.type = type;
        this.value = value;
    }

    public SetExpressionType getType() {
        return type;
    }

    public RangeValue getRange() {
        return (RangeValue) value;
    }

    public SetNodeValue getTree() {
        return (SetNodeValue)value;
    }
    
    public Set<SetMember> getEnumValue() {
        return (Set<SetMember>) value;
    }

    public SymbolDeclaration getSymbolDeclaration() {
        return (SymbolDeclaration) value;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        sb.append(type).append(", ").append(value).append("]");
        return sb.toString();
    }
}
