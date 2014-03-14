/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.model;

public class ConstraintDeclaration {

    private String name;
    private RelopType relop;
    private Expression aExpr;
    private Expression bExpr;

    public enum RelopType {
        GE, EQ, LE;

        public static RelopType parseRelop(String relop) {
            if (">=".equals(relop)) {
                return RelopType.GE;
            } else if ("=".equals(relop)) {
                return RelopType.EQ;
            } else if ("<=".equals(relop)) {
                return RelopType.LE;
            } else {
                // TODO
                throw new RuntimeException("Not implemented");
            }
        }
    }

    public ConstraintDeclaration(String name, String relop, Expression aExpr, Expression bExpr) {
        this(name, RelopType.parseRelop(relop), aExpr, bExpr);
    }

    public ConstraintDeclaration(String name, RelopType relop, Expression aExpr, Expression bExpr) {
        this.name = name;
        this.relop = relop;
        this.aExpr = aExpr;
        this.bExpr = bExpr;
    }

    public String getName() {
        return name;
    }

    public RelopType getRelop() {
        return relop;
    }

    public Expression getaExpr() {
        return aExpr;
    }

    public Expression getbExpr() {
        return bExpr;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("name: ").append(name).append(", relop: ").append(relop).append(aExpr).append(";").append(bExpr);
        return sb.toString();
    }
}
