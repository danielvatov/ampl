/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.model;

import java.util.ArrayList;
import java.util.List;

public class ObjectiveDeclaration {

    public enum Goal {
        MINIMIZE("minimize", "min"),
        MAXIMIZE("maximize", "max");

        private List<String> variants = new ArrayList<String>();

        private Goal(String... variants) {
            for (String v : variants) {
                this.variants.add(v);
            }
        }

        public boolean isVariant(String value) {
            for (String v : variants) {
                if (v.equalsIgnoreCase(value)) {
                    return true;
                }
            }
            return false;
        }

        public static Goal fromString(String value) {
            for (Goal g : Goal.values()) {
                if (g.isVariant(value)) {
                    return g;
                }
            }
            throw new ModelException(value);
        }
    }

    private Goal       goal;
    private String     name;
    private Expression expression;

    public ObjectiveDeclaration(Goal goal, String name, Expression expression) {
        this.goal = goal;
        this.name = name;
        this.expression = expression;
    }

    public Goal getGoal() {
        return goal;
    }

    public String getName() {
        return name;
    }

    public Expression getExpression() {
        return expression;
    }

    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(goal).append(" ").append(name).append(": ").append(expression.toString()).append("\n");
        return ret.toString();
    }
}