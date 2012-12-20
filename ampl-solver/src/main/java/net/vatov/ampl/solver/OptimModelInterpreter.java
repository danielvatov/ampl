package net.vatov.ampl.solver;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import net.vatov.ampl.model.ConstraintDeclaration;
import net.vatov.ampl.model.Expression;
import net.vatov.ampl.model.NodeValue;
import net.vatov.ampl.model.OptimModel;
import net.vatov.ampl.model.SymbolDeclaration;
import net.vatov.ampl.model.NodeValue.BuiltinFunction;
import net.vatov.ampl.model.NodeValue.OperationType;

import org.apache.commons.math3.analysis.function.Acosh;
import org.apache.commons.math3.analysis.function.Asinh;
import org.apache.commons.math3.analysis.function.Atanh;
import org.apache.commons.math3.util.Precision;


public class OptimModelInterpreter {

    private final OptimModel model;
    private final Acosh acosh = new Acosh();
    private final Asinh asinh = new Asinh();
    private final Atanh atanh = new Atanh();

    public OptimModelInterpreter(OptimModel model) {
        this.model = model;
    }

    public void initialBind() {
        ArrayList<SymbolDeclaration> symbolDeclarations = model.getSymbolDeclarations();
        for (SymbolDeclaration sd : symbolDeclarations) {
            Double exprVal = evaluateExpression(sd.getValue());
            if (null == exprVal) {
                continue;
            }
            sd.setBindValue(exprVal);
        }
    }

    public Boolean evaluateConstraint(int index) {
        ConstraintDeclaration cd = model.getConstraints().get(index);
        Double aExpr = evaluateExpression(cd.getaExpr());
        Double bExpr = evaluateExpression(cd.getbExpr());
        switch (cd.getRelop()) {
        case EQ:
            return aExpr.equals(bExpr);
        case GE:
            return aExpr >= bExpr;
        case LE:
            return aExpr <= bExpr;
        default:
            throw new InterpreterException(cd.getRelop().name() + " not supported");
        }
    }

    public Double evaluateGoal(int index) {
        return evaluateExpression(model.getObjectives().get(index).getExpression());
    }

    public Double evaluateExpression(Expression expr) {
        if (null == expr) {
            return null;
        }
        switch (expr.getType()) {
        case DOUBLE:
            return expr.getValue();
        case SYMREF:
            Double value = expr.getSymRef().getBindValue();
            if (null == value) {
                throw new InterpreterException(expr.getSymRef() + " undefined");
            }
            return expr.getSymRef().getBindValue();
        case TREE:
            NodeValue nodeValue = expr.getTreeValue();
            Expression[] operands = nodeValue.getOperands();
            OperationType operation = nodeValue.getOperation();
            switch (operation) {
            case PLUS:
                return evaluateExpression(operands[0]) + evaluateExpression(operands[1]);
            case MINUS:
                return evaluateExpression(operands[0]) - evaluateExpression(operands[1]);
            case MULT:
                return evaluateExpression(operands[0]) * evaluateExpression(operands[1]);
            case DIV_SLASH:
                return evaluateExpression(operands[0]) / evaluateExpression(operands[1]);
            case MOD:
                return evaluateExpression(operands[0]) % evaluateExpression(operands[1]);
            case POW:
                return Math.pow(evaluateExpression(operands[0]), evaluateExpression(operands[1]));
            case UNARY_MINUS:
                return -evaluateExpression(operands[0]);
            case UNARY_PLUS:
                return evaluateExpression(operands[0]);
            case BUILTIN_FUNCTION:
                return evaluateBuiltinFunction(expr.getTreeValue().getBuiltinFunction(), operands);
            case DIV:
            default:
                throw new InterpreterException("Not implemented");
            }
        default:
            throw new InterpreterException("Unknown type " + expr.getType());
        }
    }

    private Double evaluateBuiltinFunction(BuiltinFunction f, Expression[] operands) {
        switch (f) {
        case ABS:
            return Math.abs(evaluateExpression(operands[0]));
        case ACOS:
            return Math.acos(evaluateExpression(operands[0]));
        case ACOSH:
            return acosh.value(evaluateExpression(operands[0]));
        case ASIN:
            return Math.asin(evaluateExpression(operands[0]));
        case ASINH:
            return asinh.value(evaluateExpression(operands[0]));
        case ATAN:
            return Math.atan(evaluateExpression(operands[0]));
        case ATAN2:
            return Math.atan2(evaluateExpression(operands[0]), evaluateExpression(operands[1]));
        case ATANH:
            return atanh.value(evaluateExpression(operands[0]));
        case CEIL:
            return Math.ceil(evaluateExpression(operands[0]));
        case COS:
            return Math.cos(evaluateExpression(operands[0]));
        case CTIME:
            throw new InterpreterException("Still working only with double as expression type");
        case EXP:
            return Math.exp(evaluateExpression(operands[0]));
        case FLOOR:
            return Math.floor(evaluateExpression(operands[0]));
        case LOG:
            return Math.log(evaluateExpression(operands[0]));
        case LOG10:
            return Math.log10(evaluateExpression(operands[0]));
        case MAX: {
            double[] vals = evaluateAndSort(operands);
            return vals[vals.length - 1];
        }
        case MIN: {
            double[] vals = evaluateAndSort(operands);
            return vals[0];
        }
        case PRECISION:
            return Precision.round(evaluateExpression(operands[0]), evaluateExpression(operands[1]).intValue());
        case ROUND:
            if (operands.length > 1) {
                return Precision.round(evaluateExpression(operands[0]), evaluateExpression(operands[1]).intValue());
            }
            return (double) Math.round(evaluateExpression(operands[0]));
        case SIN:
            return Math.sin(evaluateExpression(operands[0]));
        case SINH:
            return Math.sinh(evaluateExpression(operands[0]));
        case SQRT:
            return Math.sqrt(evaluateExpression(operands[0]));
        case TAN:
            return Math.tan(evaluateExpression(operands[0]));
        case TANH:
            return Math.tanh(evaluateExpression(operands[0]));
        case TIME:
            return Long.valueOf(System.currentTimeMillis()).doubleValue();
        case TRUNC:
            int precision = 0;
            if (operands.length > 1) {
                precision = evaluateExpression(operands[1]).intValue();
            }
            return Precision.round(evaluateExpression(operands[0]), precision,
                    BigDecimal.ROUND_DOWN);
        default:
            throw new InterpreterException("Unknown builtin function " + f);
        }
    }

    private double[] evaluateAndSort(Expression[] operands) {
        double[] vals = new double[operands.length];
        for (int i = 0; i < operands.length; ++i) {
            vals[i] = evaluateExpression(operands[i]);
        }
        Arrays.sort(vals);
        return vals;
    }
}
