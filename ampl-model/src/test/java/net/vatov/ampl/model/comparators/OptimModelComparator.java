/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.model.comparators;

import net.vatov.ampl.model.OptimModel;

public class OptimModelComparator extends BaseComparator<OptimModel>{

	@Override
	public ComparisonResult compare(OptimModel m1, OptimModel m2) {
		if (compareLists(m1.getSymbolDeclarations(),
				m2.getSymbolDeclarations(),
				new SymbolDeclarationComparator()).equals(ComparisonResult.EQUALS)
				&& compareLists(m1.getObjectives(), m2.getObjectives(),
						new ObjectiveDeclarationComparator()).equals(ComparisonResult.EQUALS)
				&& compareLists(m1.getConstraints(), m2.getConstraints(),
				        new ConstraintDeclarationComparator()).equals(ComparisonResult.EQUALS)) {
			return ComparisonResult.EQUALS;
		}
		return ComparisonResult.DIFFER;
	}
}
