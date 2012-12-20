/**
 *  Copyright (C) 2010 by Daniel Vatov
 */
package net.vatov.ampl.model.xml;

import java.util.ArrayList;

import net.vatov.ampl.model.Expression;
import net.vatov.ampl.model.ModelException;
import net.vatov.ampl.model.SymbolDeclaration;
import net.vatov.ampl.model.Expression.ExpressionType;
import net.vatov.ampl.model.NodeValue.BuiltinFunction;
import net.vatov.ampl.model.NodeValue.OperationType;


import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ExpressionConverter implements Converter {

    private static final String VALUE_TYPE_FIELD = "value-type";
    private static final String EXPR_NODE = "expr";
    private static final String OPERATION_FIELD = "op";
    private static final String BUILTIN_FUNC_FIELD = "func";

    @Override
    public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
        return Expression.class.equals(type);
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer, MarshallingContext context) {
        Expression e = (Expression) source;
        writer.startNode(EXPR_NODE);
        writer.addAttribute(VALUE_TYPE_FIELD, e.getType().toString());
        switch (e.getType()) {
        case DOUBLE:
            writer.setValue(e.getValue().toString());
            break;
        case SYMREF:
            writer.setValue(e.getSymRef().getName());
            break;
        case TREE:
            // TODO: <minus> var; --var; var--, etc
            writer.addAttribute(OPERATION_FIELD, e.getTreeValue().getOperation().toString().toLowerCase());
            if (e.getTreeValue().getOperation().equals(OperationType.BUILTIN_FUNCTION)) {
                writer.addAttribute(BUILTIN_FUNC_FIELD, e.getTreeValue().getBuiltinFunction().toString().toLowerCase());
            }
            if (null != e.getTreeValue().getOperands()) {
                for (Object o : e.getTreeValue().getOperands()) {
                    context.convertAnother(o);
                }
            }
            break;
        default:
            throw new ModelException("Not implemented");
        }
        writer.endNode();
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
        Expression e = new Expression();
        ExpressionType valueType = ExpressionType.valueOf(reader.getAttribute(VALUE_TYPE_FIELD));
        switch (valueType) {
        case DOUBLE:
            e.setValue(Double.parseDouble(reader.getValue()));
            break;
        case SYMREF:
            SymbolDeclaration decl = Util.getSymbolDeclaration(reader.getValue(), context);
            e.setSymRef(decl);
            break;
        case TREE:
            // TODO: ако е операция с константи да се изчисли с-та
            // на израза
            OperationType op = OperationType.valueOf(reader.getAttribute(OPERATION_FIELD).toUpperCase());
            BuiltinFunction fType = null;
            if (OperationType.BUILTIN_FUNCTION.equals(op)) {
                fType = BuiltinFunction.valueOf(reader.getAttribute(BUILTIN_FUNC_FIELD).toUpperCase());
            }
            ArrayList<Expression> operands = null;
            while (reader.hasMoreChildren()) {
                if (null == operands) {
                    operands = new ArrayList<Expression>(2);
                }
                reader.moveDown();
                operands.add((Expression) context.convertAnother(e, Expression.class));
                reader.moveUp();
            }
            if (OperationType.BUILTIN_FUNCTION.equals(op)) {
                e.setTreeValue(fType, operands);
            } else {
                e.setTreeValue(op, operands.toArray(new Expression[operands.size()]));
            }
            break;
        default:
            throw new ModelException("Do not know how to handle " + e.getType().toString());
        }
        return e;
    }
}
