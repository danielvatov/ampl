/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.model.comparators;

import net.vatov.ampl.model.ObjectiveDeclaration;

import org.apache.log4j.Logger;


public class ObjectiveDeclarationComparator extends
		BaseComparator<ObjectiveDeclaration> {

    private static Logger logger = Logger.getLogger(ObjectiveDeclarationComparator.class);
    
	@Override
	public ComparisonResult compare(ObjectiveDeclaration o1,
			ObjectiveDeclaration o2) {
	    ComparisonResult ret;
		if (o1.getGoal().equals(o2.getGoal()) 
				&& o1.getName().equals(o2.getName())) {
					ret = new ExpressionComparator().compare(o1.getExpression(), o2.getExpression());
					if (ret == ComparisonResult.DIFFER) {
					    logger.debug(String.format("Goals differ in expressions \n\t%s \n\t%s", o1.getName(), o2.getName()));
					}
					return ret;
		}
        logger.debug(String.format("Goals have different names \n\t%s \n\t%s", o1.getName(), o2.getName()));
		return ComparisonResult.DIFFER;
	}

}
