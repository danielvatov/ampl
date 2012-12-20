/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.model.comparators;

import net.vatov.ampl.model.ConstraintDeclaration;

import org.apache.log4j.Logger;


public class ConstraintDeclarationComparator extends BaseComparator<ConstraintDeclaration> {

    private static Logger logger = Logger.getLogger(ConstraintDeclarationComparator.class);

    @Override
    public ComparisonResult compare(ConstraintDeclaration o1, ConstraintDeclaration o2) {
        ExpressionComparator ec = new ExpressionComparator();
        if (o1.getName().equals(o2.getName()) && o1.getRelop().equals(o2.getRelop())
                && ec.compare(o1.getaExpr(), o2.getaExpr()).equals(ComparisonResult.EQUALS)
                && ec.compare(o1.getbExpr(), o2.getbExpr()).equals(ComparisonResult.EQUALS)) {
            return ComparisonResult.EQUALS;
        }
        logger.debug(String.format("Constraints differ \n\t%s \n\t%s", o1.getName(), o2.getName()));
        return ComparisonResult.DIFFER;
    }
}
