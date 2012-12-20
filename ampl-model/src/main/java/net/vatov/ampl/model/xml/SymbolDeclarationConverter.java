/**
 *  Copyright (C) 2010 by Daniel Vatov
 */
package net.vatov.ampl.model.xml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import net.vatov.ampl.model.Expression;
import net.vatov.ampl.model.ModelException;
import net.vatov.ampl.model.SymbolDeclaration;
import net.vatov.ampl.model.SymbolDeclaration.DeclarationAttributeEnum;
import net.vatov.ampl.model.SymbolDeclaration.SymbolType;


import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class SymbolDeclarationConverter implements Converter {

    private static final String NAME_FIELD = "name";
    private static final String TYPE_FIELD = "type";

    @Override
    public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
        return SymbolDeclaration.class.equals(type);
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        SymbolDeclaration sd = (SymbolDeclaration) source;
        writer.addAttribute(NAME_FIELD, sd.getName());
        writer.addAttribute(TYPE_FIELD, sd.getType().toString());
        Map<DeclarationAttributeEnum, Object> attributes = sd.getAttributes();
        if (null != attributes) {
            for (SymbolDeclaration.DeclarationAttributeEnum key : SymbolDeclaration.DeclarationAttributeEnum.values()) {
                if (!attributes.containsKey(key)) {
                    continue;
                }
                Object value = attributes.get(key);
                if (!isDeclarationAttribute(key.toString(), true)) {
                    continue;
                }
                switch (key) {
                case BINDED_VALUE:
                case BINARY:
                case INTEGER:
                    writer.addAttribute(key.toString().toLowerCase(), value.toString());
                    break;
                case LOWER_BOUND:
                case UPPER_BOUND:
                case VALUE:
                    writer.startNode(key.toString().toLowerCase());
                    Expression e = (Expression) value;
                    context.convertAnother(e);
                    writer.endNode();
                    break;
                default:
                    throw new ModelException("unknown attribute " + key.toString());
                }
            }
        }
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        SymbolType type = SymbolType.valueOf(reader.getAttribute(TYPE_FIELD));
        SymbolDeclaration sd = SymbolDeclaration.createSymbolDeclaration(reader.getAttribute(NAME_FIELD), type);

        Map<DeclarationAttributeEnum, Object> attributes = new HashMap<SymbolDeclaration.DeclarationAttributeEnum, Object>();
        @SuppressWarnings("unchecked")
        Iterator<String> it = reader.getAttributeNames();
        while (it.hasNext()) {
            String name = (String) it.next();
            if (!isDeclarationAttribute(name, false)) {
                continue;
            }
            String value = reader.getAttribute(name);
            DeclarationAttributeEnum attKey = DeclarationAttributeEnum.valueOf(name.toUpperCase());
            switch (attKey) {
            case BINARY:
            case INTEGER:
                attributes.put(attKey, null == value ? null : Boolean.parseBoolean(value));
                break;
            case BINDED_VALUE:
                attributes.put(DeclarationAttributeEnum.BINDED_VALUE, null == value ? null : Double.parseDouble(value));
                break;
            }
        }

        while (reader.hasMoreChildren()) {
            reader.moveDown();
            String nodeName = reader.getNodeName();
            reader.moveDown();
            if (DeclarationAttributeEnum.VALUE.toString().toLowerCase().equals(nodeName)) {
                Expression e = (Expression) context.convertAnother(sd, Expression.class);
                if (null != e) {
                    attributes.put(DeclarationAttributeEnum.VALUE, e);
                }
            } else if (DeclarationAttributeEnum.LOWER_BOUND.toString().toLowerCase().equals(nodeName)) {
                Expression e = (Expression) context.convertAnother(sd, Expression.class);
                if (null != e) {
                    attributes.put(DeclarationAttributeEnum.LOWER_BOUND, e);
                }
            } else if (DeclarationAttributeEnum.UPPER_BOUND.toString().toLowerCase().equals(nodeName)) {
                Expression e = (Expression) context.convertAnother(sd, Expression.class);
                if (null != e) {
                    attributes.put(DeclarationAttributeEnum.UPPER_BOUND, e);
                }
            }
            reader.moveUp();
            reader.moveUp();
        }
        if (!attributes.isEmpty()) {
            sd.setAttributes(attributes);
        }
        Util.setSymbolDeclaration(sd.getName(), sd, context);
        return sd;
    }

    private boolean isDeclarationAttribute(String key, boolean marshal) {
        if (NAME_FIELD.equals(key) || TYPE_FIELD.equals(key)) {
            return false;
        }
        DeclarationAttributeEnum attKey = DeclarationAttributeEnum.valueOf(key.toUpperCase());
        switch (attKey) {
        case BINARY:
        case INTEGER:
        case BINDED_VALUE:
            return true;
        case VALUE:
        case LOWER_BOUND:
        case UPPER_BOUND:
            return marshal;
        default:
            return false;
        }
    }
}