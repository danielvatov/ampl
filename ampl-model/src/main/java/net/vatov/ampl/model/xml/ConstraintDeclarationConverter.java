/**
 *  Copyright (C) 2010 by Daniel Vatov
 */
package net.vatov.ampl.model.xml;

import net.vatov.ampl.model.ConstraintDeclaration;
import net.vatov.ampl.model.Expression;
import net.vatov.ampl.model.ConstraintDeclaration.RelopType;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ConstraintDeclarationConverter implements Converter {

    private static final String NAME_FIELD  = "name";
    private static final String RELOP_FIELD = "relop";

    @Override
    public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
        return ConstraintDeclaration.class.equals(type);
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer,
            MarshallingContext context) {
        ConstraintDeclaration cd = (ConstraintDeclaration) source;
        writer.addAttribute(RELOP_FIELD, cd.getRelop().toString().toLowerCase());
        writer.addAttribute(NAME_FIELD, cd.getName());
        context.convertAnother(cd.getaExpr());
        context.convertAnother(cd.getbExpr());
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader,
            UnmarshallingContext context) {
        String relop = reader.getAttribute(RELOP_FIELD);
        String name = reader.getAttribute(NAME_FIELD);
        reader.moveDown();
        Expression aExpr = (Expression) context.convertAnother(null, Expression.class);
        reader.moveUp();
        reader.moveDown();
        Expression bExpr = (Expression) context.convertAnother(null, Expression.class);
        reader.moveUp();
        ConstraintDeclaration cd = new ConstraintDeclaration(name, RelopType.valueOf(relop.toUpperCase()), aExpr, bExpr);
        return cd;
    }
}
