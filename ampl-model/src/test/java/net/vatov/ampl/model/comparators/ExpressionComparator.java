/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.model.comparators;

import java.util.Arrays;

import net.vatov.ampl.model.Expression;
import net.vatov.ampl.model.ModelException;
import net.vatov.ampl.model.NodeValue;
import net.vatov.ampl.model.NodeValue.BuiltinFunction;
import net.vatov.ampl.model.NodeValue.OperationType;

import org.apache.log4j.Logger;


public class ExpressionComparator extends BaseComparator<Expression> {

    private static Logger logger = Logger.getLogger(ExpressionComparator.class);

    @Override
    public ComparisonResult compare(Expression o1, Expression o2) {
        if (o1 == o2) {
            return ComparisonResult.EQUALS;
        } else if (null != o1 && null != o2) {
            if (!o1.getType().equals(o2.getType())) {
                logger.debug(String.format("Expression types differ \n\t%s \n\t%s", o1.getType(), o2.getType()));
                return ComparisonResult.DIFFER;
            } else {
                ComparisonResult ret;
                switch (o1.getType()) {
                case DOUBLE:
                    ret = o1.getValue().equals(o2.getValue()) ? ComparisonResult.EQUALS : ComparisonResult.DIFFER;
                    if (ret == ComparisonResult.DIFFER) {
                        logger.debug(String.format("Expression values differ \n\t%s \n\t%s", o1.getValue(), o2.getValue()));
                    }
                    return ret;
                case SYMREF:
                    ret = o1.getSymRef().getName().equals(o2.getSymRef().getName()) ? ComparisonResult.EQUALS
                            : ComparisonResult.DIFFER;
                    if (ret == ComparisonResult.DIFFER) {
                        logger.debug(String.format("Expression values differ \n\t%s \n\t%s", o1.getSymRef().getName(), o2.getSymRef().getName()));
                    }
                    return ret;
                case TREE:
                    NodeValue n1 = o1.getTreeValue();
                    NodeValue n2 = o2.getTreeValue();
                    if (!n1.getOperation().equals(n2.getOperation())) {
                        logger.debug(String.format("Expression values differ \n\t%s \n\t%s", o1.getTreeValue(), o2.getTreeValue()));
                        return ComparisonResult.DIFFER;
                    }
                    if (OperationType.BUILTIN_FUNCTION.equals(n1.getOperation())) {
                        if (!n1.getBuiltinFunction().equals(n2.getBuiltinFunction())) {
                            logger.debug(String.format("Expression values differ \n\t%s \n\t%s", o1.getTreeValue(), o2.getTreeValue()));
                            return ComparisonResult.DIFFER;
                        }
                    }
                    if (n1.getOperands() == n2.getOperands()) {
                        return ComparisonResult.EQUALS;
                    } else if (n1.getOperands() == null || n2.getOperands() == null) {
                        logger.debug(String.format("Expression values differ \n\t%s \n\t%s", o1.getTreeValue(), o2.getTreeValue()));
                        return ComparisonResult.DIFFER;
                    }
                    ret = compareLists(Arrays.asList(n1.getOperands()), Arrays.asList(n2.getOperands()), this);
                    return ret;
                default:
                    throw new ModelException(o1.getType().toString());
                }
            }
        }
        logger.debug(String.format("Expression types differ because one is null \n\t%s", o1 == null ? o2 : o1));
        return ComparisonResult.DIFFER;
    }

}
