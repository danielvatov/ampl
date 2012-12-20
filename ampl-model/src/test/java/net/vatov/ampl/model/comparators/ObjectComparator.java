/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.model.comparators;

public class ObjectComparator extends BaseComparator<Object> {

    @Override
    public ComparisonResult compare(Object o1, Object o2) {
        if (o1 == o2) {
            return ComparisonResult.EQUALS;
        }
        if (null == o1 || null == o2) {
            return ComparisonResult.DIFFER;
        }
        return o1.equals(o2) ? ComparisonResult.EQUALS : ComparisonResult.DIFFER;

    }

}
