/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.model.comparators;

import java.util.HashMap;
import java.util.Map;

import net.vatov.ampl.model.SymbolDeclaration;
import net.vatov.ampl.model.SymbolDeclaration.DeclarationAttributeEnum;

import org.apache.log4j.Logger;


public class SymbolDeclarationComparator extends BaseComparator<SymbolDeclaration> {

    private static Logger logger = Logger.getLogger(SymbolDeclarationComparator.class);

    @Override
    public ComparisonResult compare(SymbolDeclaration sd1, SymbolDeclaration sd2) {
        if (!sd1.getName().equals(sd2.getName()) || !sd1.getType().equals(sd2.getType())) {
            logger.debug(String.format("SymbolDeclarations differ in name or type \n\t%s \n\t%s", sd1.getName(), sd2.getName()));
            return ComparisonResult.DIFFER;
        }
        Map<DeclarationAttributeEnum, Object> att1 = sd1.getAttributes();
        Map<DeclarationAttributeEnum, Object> att2 = sd2.getAttributes();
        @SuppressWarnings("rawtypes")
        Map<DeclarationAttributeEnum, BaseComparator> c = new HashMap<SymbolDeclaration.DeclarationAttributeEnum, BaseComparator>();
        ObjectComparator objComparator = new ObjectComparator();
        ExpressionComparator exprComparator = new ExpressionComparator();
        c.put(DeclarationAttributeEnum.INTEGER, objComparator);
        c.put(DeclarationAttributeEnum.VALUE, exprComparator);
        c.put(DeclarationAttributeEnum.BINDED_VALUE, objComparator);
        c.put(DeclarationAttributeEnum.BINARY, objComparator);
        c.put(DeclarationAttributeEnum.LOWER_BOUND, exprComparator);
        c.put(DeclarationAttributeEnum.UPPER_BOUND, exprComparator);
        
        ComparisonResult ret = compareMaps(att1, att2, c);
        if (ret == ComparisonResult.DIFFER) {
            logger.debug(String.format("SymbolDeclarations differ \n\t%s \n\t%s", sd1, sd2));
        }
        return ret;
    }
}
