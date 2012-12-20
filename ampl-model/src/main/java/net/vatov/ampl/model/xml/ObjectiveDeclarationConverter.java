/**
 *  Copyright (C) 2010 by Daniel Vatov
 */
package net.vatov.ampl.model.xml;

import net.vatov.ampl.model.Expression;
import net.vatov.ampl.model.ObjectiveDeclaration;
import net.vatov.ampl.model.ObjectiveDeclaration.Goal;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

public class ObjectiveDeclarationConverter implements Converter {

    private static final String NAME_FIELD = "name";
    private static final String GOAL_FIELD = "goal";

    @Override
    public boolean canConvert(@SuppressWarnings("rawtypes") Class type) {
        return ObjectiveDeclaration.class.equals(type);
    }

    @Override
    public void marshal(Object source, HierarchicalStreamWriter writer,
            MarshallingContext context) {
        ObjectiveDeclaration od = (ObjectiveDeclaration) source;
        writer.addAttribute(GOAL_FIELD, od.getGoal().toString());
        writer.addAttribute(NAME_FIELD, od.getName());
        context.convertAnother(od.getExpression());
    }

    @Override
    public Object unmarshal(HierarchicalStreamReader reader,
            UnmarshallingContext context) {
        Goal goal = Goal.valueOf(reader.getAttribute(GOAL_FIELD));
        String name = reader.getAttribute(NAME_FIELD);
        reader.moveDown();
        Expression e = (Expression) context.convertAnother(null, Expression.class);
        reader.moveUp();
        ObjectiveDeclaration od = new ObjectiveDeclaration(goal, name, e);
        return od;
    }
}
