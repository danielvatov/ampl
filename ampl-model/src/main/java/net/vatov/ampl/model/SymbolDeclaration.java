/**
 *  Copyright (C) 2010 by Daniel Vatov
 */
package net.vatov.ampl.model;

import java.util.HashMap;
import java.util.Map;

public class SymbolDeclaration implements Cloneable {
    /**
     * Редът на константите е важен при сериализиране в xml ({@link
     * bg.bas.iit.weboptim.model.xml.SymbolDeclarationConverter.marshal(Object,
     * HierarchicalStreamWriter, MarshallingContext)}) Нещата сериализирани като
     * атрибути в xml-а трябва да са първи TODO: Написаното по-горе да се оправи
     */
    public static enum DeclarationAttributeEnum {
        INTEGER, VALUE, BINDED_VALUE, BINARY, LOWER_BOUND, UPPER_BOUND,
        SET_DIMEN, SET_WITHIN, SET_ASSIGN, SET_DEFAULT, SET_ORDERED, SET_CIRCULAR;
    }

    public static enum SymbolType {
        PARAM, VAR, SET;
    }

    private String name;
    private Map<DeclarationAttributeEnum, Object> attributes;
    private SymbolType type;

    private SymbolDeclaration(String name, SymbolType type) {
        if (null == name || null == type) {
            throw new ModelException("both name and type are mandatory");
        }
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Expression getValue() {
        if (null == attributes) {
            return null;
        }
        return (Expression) attributes.get(DeclarationAttributeEnum.VALUE);
    }

    public Expression getLowerBound() {
        if (null == attributes) {
            return null;
        }
        return (Expression) attributes.get(DeclarationAttributeEnum.LOWER_BOUND);
    }

    public Expression getUpperBound() {
        if (null == attributes) {
            return null;
        }
        return (Expression) attributes.get(DeclarationAttributeEnum.UPPER_BOUND);
    }

    public Boolean isInteger() {
        if (null == attributes) {
            return null;
        }
        return (Boolean) attributes.get(DeclarationAttributeEnum.INTEGER);
    }

    public Boolean isBinary() {
        if (null == attributes) {
            return null;
        }
        return (Boolean) attributes.get(DeclarationAttributeEnum.BINARY);
    }

    /**
     * Методът трябва да се синхронизира за да се извиква паралелно TODO: value
     * може да е вектор, матрица или множество, когато синтаксиса го позволи
     * 
     * @param value
     */
    public void setBindValue(Double value) {
        if (null == attributes) {
            attributes = new HashMap<SymbolDeclaration.DeclarationAttributeEnum, Object>();
        }
        attributes.put(DeclarationAttributeEnum.BINDED_VALUE, value);
    }

    public Double getBindValue() {
        if (null == attributes) {
            return null;
        }
        return (Double) attributes.get(DeclarationAttributeEnum.BINDED_VALUE);
    }

    public Map<DeclarationAttributeEnum, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<DeclarationAttributeEnum, Object> attributes) {
        this.attributes = attributes;
    }

    public SymbolType getType() {
        return type;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder(name);
        ret.append(": ").append(attributes);
        return ret.toString();
    }

    public static SymbolDeclaration createParamDeclaration(String name) {
        return new SymbolDeclaration(name, SymbolType.PARAM);
    }

    public static SymbolDeclaration createVarDeclaration(String name) {
        return new SymbolDeclaration(name, SymbolType.VAR);
    }

    public static SymbolDeclaration createSetDeclaration(String name) {
        return new SymbolDeclaration(name, SymbolType.SET);
    }

    public static SymbolDeclaration createSymbolDeclaration(String name, SymbolType type) {
        return new SymbolDeclaration(name, type);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (null == obj) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SymbolDeclaration)) {
            return false;
        }
        SymbolDeclaration o = (SymbolDeclaration) obj;
        return name.equals(o.name);
    }

    @Override
    public Object clone() {
        SymbolDeclaration sd = createSymbolDeclaration(name, type);
        Map<DeclarationAttributeEnum, Object> a = new HashMap<SymbolDeclaration.DeclarationAttributeEnum, Object>();
        for (Map.Entry<DeclarationAttributeEnum, Object> e : attributes.entrySet()) {
            if (e.getValue() instanceof Expression) {
                Expression ori = (Expression) (e.getValue());
                Expression exp = (Expression) ori.clone();
                a.put(e.getKey(), exp);
            } else if (e.getValue() instanceof Boolean) {
                a.put(e.getKey(), e.getValue());
            } else {
                throw new ModelException("Unsupported attribute for clone operation " + e.getKey());
            }
        }
        return sd;
    }
}