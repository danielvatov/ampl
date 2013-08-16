/*******************************************************************************
 * Copyright (c) 2008-2013 VMware, Inc. All rights reserved.
 ******************************************************************************/
package net.vatov.ampl.model;

public class RangeValue {
    private Expression from;
    private Expression to;
    private Expression step;
    
    public RangeValue(Expression from, Expression to, Expression step) {
        this.from = from;
        this.to = to;
        this.step = step;
    }

    public Expression getFrom() {
        return from;
    }

    public Expression getTo() {
        return to;
    }

    public Expression getStep() {
        return step;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(from).append(" .. ").append(to);
        if (null != step) {
            sb.append(" by ").append(step);
        }
        return sb.toString();
    }
}
