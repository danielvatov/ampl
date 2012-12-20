/**
 *  Copyright (C) 2010 by Daniel Vatov
 */
package net.vatov.ampl.model;

import net.vatov.ampl.model.Expression.ExpressionType;

public class Util {

    /**
     * Проверява дали всички подадени аргументи са константни изрази, чиято
     * стойност може да бъде изчислена в момента
     * 
     * @param expressions
     * @return true ако всички аргументи са константни изрази
     */
    public static boolean areConstExpressions(Expression... expressions) {
        for (Expression e : expressions) {
            if (e.getType() != ExpressionType.DOUBLE) {
                return false;
            }
        }
        return true;
    }
}
