/**
 *  Copyright (C) 2013 by Daniel Vatov
 */
package net.vatov.ampl.model;

public class SetMember {
    public enum SetMemberType {
        NUMBER, STRING, SYMREF;
    }

    private SetMemberType type;
    private Object value;

    public SetMember(Object value, SetMemberType type) {
        this.type = type;
        this.value = value;
    }

    public Double getNumber() {
        return (Double) value;
    }

    public String getString() {
        return (String) value;
    }

    public SymbolDeclaration getSymRef() {
        return (SymbolDeclaration) value;
    }

    public SetMemberType getType() {
        return type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        sb.append(type).append(",").append(value).append("]");
        return sb.toString();
    }
}
