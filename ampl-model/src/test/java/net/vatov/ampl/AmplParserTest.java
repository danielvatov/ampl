/**
 *  Copyright (C) 2010 by Daniel Vatov
 */
package net.vatov.ampl;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static junit.framework.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import net.vatov.ampl.AmplException;
import net.vatov.ampl.AmplParser;
import net.vatov.ampl.model.Expression;
import net.vatov.ampl.model.ModelException;
import net.vatov.ampl.model.OptimModel;
import net.vatov.ampl.model.SymbolDeclaration;
import net.vatov.ampl.model.Expression.ExpressionType;
import net.vatov.ampl.model.NodeValue.BuiltinFunction;
import net.vatov.ampl.model.NodeValue.OperationType;
import net.vatov.ampl.model.ObjectiveDeclaration.Goal;
import net.vatov.ampl.model.SymbolDeclaration.DeclarationAttributeEnum;
import net.vatov.ampl.model.SymbolDeclaration.SymbolType;

import org.junit.Test;


public class AmplParserTest {

    private OptimModel loadModel(String modelFile) throws AmplException, IOException {
        URL url = getClass().getResource(modelFile);
        assertNotNull(url);
        AmplParser parser = new AmplParser();
        OptimModel model = parser.parse(url.openStream());
        assertNotNull(model);
        List<SymbolDeclaration> symbolDeclarations = model.getSymbolDeclarations();
        for (SymbolDeclaration sd : symbolDeclarations) {
            if (sd.getType() == SymbolType.PARAM) {
                assertTrue(
                        sd.getName(),
                        ((Expression) sd.getAttributes().get(DeclarationAttributeEnum.VALUE)).getType() == Expression.ExpressionType.DOUBLE);
            } else if (sd.getType() == SymbolType.VAR) {
                if (sd.getAttributes() != null && sd.getAttributes().containsKey(DeclarationAttributeEnum.VALUE)) {
                    ExpressionType expressionType = sd.getValue().getType();
                    assertTrue(sd.getName(), expressionType == Expression.ExpressionType.TREE
                            || expressionType == Expression.ExpressionType.SYMREF);
                }
                assertTrue(sd.getName(), sd.getBindValue() == null);
            } else if (sd.getType() == SymbolType.SET) {
            } else {
                fail();
            }
        }
        return model;
    }

    @Test(expected=AmplException.class)
    public final void testVarPrefix() {
        ByteArrayInputStream ampl = new ByteArrayInputStream("var var_name;".getBytes());
        AmplParser p = new AmplParser();
        p.parse(ampl);
    }
    
    @Test
    public final void testParse() throws AmplException, IOException {
        OptimModel model = loadModel("params.mod");
        assertEquals(15.4, model.getParamValue("test1"));
        assertEquals(-3.0, model.getParamValue("test2"));
        assertEquals(12.4, model.getParamValue("test3"));
        assertEquals(8.0, model.getParamValue("test4"));
        assertEquals(4.0, model.getParamValue("test5"));
        assertEquals(-2.6666666666666665, model.getParamValue("test6"));
        assertEquals(16.0, model.getParamValue("test7"));
        assertEquals(-0.8888888888888888, model.getParamValue("test8"));
        assertEquals(0.25, model.getParamValue("test9"));
        assertEquals(-15.4, model.getParamValue("test10"));
        assertNotNull(model.getVarRef("v1"));
        assertNotNull(model.getVarRef("v1").getLowerBound());
        assertNotNull(model.getVarRef("v1").getUpperBound());
        assertTrue(model.getVarRef("v1").isInteger());
        assertNotNull(model.getVarRef("v2"));
        assertTrue(Expression.ExpressionType.TREE == model.getVarRef("v2").getValue().getType());
        assertNotNull(model.getVarRef("v3"));
        assertTrue(Expression.ExpressionType.SYMREF == model.getVarRef("v3").getValue().getType());
        assertNotNull(model.getVarRef("v4"));
        assertTrue(Expression.ExpressionType.TREE == model.getVarRef("v4").getValue().getType());
        assertNotNull(model.getVarRef("v5"));
        assertTrue(Expression.ExpressionType.TREE == model.getVarRef("v5").getValue().getType());
        assertNotNull(model.getVarRef("v6"));
        assertTrue(model.getVarRef("v6").isBinary());
        assertNotNull(model.getVarRef("v7"));
        assertEquals(2, model.getObjectives().size());
        assertEquals(Goal.MAXIMIZE, model.getObjectives().get(0).getGoal());
        assertEquals(Goal.MINIMIZE, model.getObjectives().get(1).getGoal());
        assertEquals(4, model.getConstraints().size());
        assertEquals("constr1", model.getConstraints().get(2).getName());
        assertEquals("constr2", model.getConstraints().get(3).getName());
        assertEquals("var_v1_lower", model.getConstraints().get(0).getName());
        assertEquals("var_v1_upper", model.getConstraints().get(1).getName());
    }

    @Test
    public final void testLeonid1() throws AmplException, IOException {
        OptimModel model = loadModel("leonid1.mod");
        assertNotNull(model.getVarRef("x1"));
        assertNotNull(model.getVarRef("x2"));
        assertEquals(3, model.getConstraints().size());
        assertNotNull(model.getConstraints().get(0));
        assertNotNull(model.getConstraints().get(1));
        assertNotNull(model.getConstraints().get(2));
        assertEquals(2, model.getObjectives().size());
        assertEquals(Goal.MAXIMIZE, model.getObjectives().get(0).getGoal());
        assertEquals(Goal.MAXIMIZE, model.getObjectives().get(1).getGoal());
        assertEquals("f1", model.getObjectives().get(0).getName());
        assertEquals("f2", model.getObjectives().get(1).getName());
    }

    @Test
    public final void testLeonid2() throws AmplException, IOException {
        OptimModel model = loadModel("leonid2.mod");
        assertEquals(11, model.getSymbolDeclarations().size());
        assertEquals(16, model.getConstraints().size());
        assertEquals(7, model.getObjectives().size());
    }

    @Test
    public final void testBuiltin() throws AmplException, IOException {
        OptimModel model = loadModel("builtin.mod");
        assertEquals(29, model.getSymbolDeclarations().size());
        validateBuiltin(model, "x1", BuiltinFunction.ABS, 1);
        validateBuiltin(model, "x2", BuiltinFunction.ACOS, 1);
        validateBuiltin(model, "x3", BuiltinFunction.ACOSH, 1);
        validateBuiltin(model, "x4", BuiltinFunction.ASIN, 1);
        validateBuiltin(model, "x5", BuiltinFunction.ASINH, 1);
        validateBuiltin(model, "x6", BuiltinFunction.ATAN, 1);
        validateBuiltin(model, "x7", BuiltinFunction.ATAN2, 2);
        validateBuiltin(model, "x8", BuiltinFunction.ATANH, 1);
        validateBuiltin(model, "x9", BuiltinFunction.CEIL, 1);
        validateBuiltin(model, "x10", BuiltinFunction.CTIME, 0);
        validateBuiltin(model, "x11", BuiltinFunction.CTIME, 1);
        validateBuiltin(model, "x12", BuiltinFunction.COS, 1);
        validateBuiltin(model, "x13", BuiltinFunction.EXP, 1);
        validateBuiltin(model, "x14", BuiltinFunction.FLOOR, 1);
        validateBuiltin(model, "x15", BuiltinFunction.LOG, 1);
        validateBuiltin(model, "x16", BuiltinFunction.LOG10, 1);
        validateBuiltin(model, "x17", BuiltinFunction.MAX, 5);
        validateBuiltin(model, "x18", BuiltinFunction.MIN, 5);
        validateBuiltin(model, "x19", BuiltinFunction.PRECISION, 2);
        validateBuiltin(model, "x20", BuiltinFunction.ROUND, 1);
        validateBuiltin(model, "x21", BuiltinFunction.ROUND, 2);
        validateBuiltin(model, "x22", BuiltinFunction.SIN, 1);
        validateBuiltin(model, "x23", BuiltinFunction.SINH, 1);
        validateBuiltin(model, "x24", BuiltinFunction.SQRT, 1);
        validateBuiltin(model, "x25", BuiltinFunction.TAN, 1);
        validateBuiltin(model, "x26", BuiltinFunction.TANH, 1);
        validateBuiltin(model, "x27", BuiltinFunction.TIME, 0);
        validateBuiltin(model, "x28", BuiltinFunction.TRUNC, 1);
        validateBuiltin(model, "x29", BuiltinFunction.TRUNC, 2);
    }

    @Test
    public final void testSets() throws Exception {
        OptimModel model = loadModel("set.mod");
        System.out.println(model.getSymbolDeclarations());
    }

    private void validateBuiltin(OptimModel model, String varName, BuiltinFunction fType, int params) {
        SymbolDeclaration varRef = model.getVarRef(varName);
        Expression value = varRef.getValue();
        assertEquals(OperationType.BUILTIN_FUNCTION, value.getTreeValue().getOperation());
        assertEquals(fType, value.getTreeValue().getBuiltinFunction());
        if (0 == params) {
            assertNull(varName, value.getTreeValue().getOperands());
        } else {
            assertEquals(varName, params, value.getTreeValue().getOperands().length);
        }
    }
}