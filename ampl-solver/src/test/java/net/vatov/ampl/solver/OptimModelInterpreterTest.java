package net.vatov.ampl.solver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import net.vatov.ampl.AmplException;
import net.vatov.ampl.AmplParser;
import net.vatov.ampl.model.OptimModel;

import org.junit.Before;
import org.junit.Test;


public class OptimModelInterpreterTest {

    private OptimModel model;
    OptimModelInterpreter interpreter;

    @Before
    public void init() throws AmplException, IOException {
        InputStream is = getClass().getResourceAsStream("interpreter.mod");
        model = new AmplParser().parse(is);
        is.close();
        interpreter = new OptimModelInterpreter(model);
    }

    @Test
    public void testInitialBind() {
        assertEquals(34, model.getSymbolDeclarations().size());
        assertNull("Value binded before initial bind", model.getVarRef("x1").getBindValue());
        assertNull("Value binded before initial bind", model.getVarRef("x2").getBindValue());
        assertNull("Value binded before initial bind", model.getVarRef("x3").getBindValue());
        assertNull("Value binded before initial bind", model.getVarRef("x4").getBindValue());
        interpreter.initialBind();
        assertNotNull("Value not binded after initial bind", model.getVarRef("x1"));
        assertNotNull("Value not binded after initial bind", model.getVarRef("x2"));
        assertNotNull("Value not binded after initial bind", model.getVarRef("x3"));
        assertNotNull("Value not binded after initial bind", model.getVarRef("x4"));
        assertEquals(Double.valueOf(2.0), (Double) (model.getVarRef("x1").getBindValue()));
        assertEquals(Double.valueOf(3.0), (Double) (model.getVarRef("x2").getBindValue()));
        assertEquals(Double.valueOf(-2.0), (Double) (model.getVarRef("x3").getBindValue()));
        assertEquals(Double.valueOf(2.0), (Double) (model.getVarRef("x4").getBindValue()));
    }

    @Test
    public void testEvaluateConstraint() {
        assertEquals("c1", model.getConstraints().get(0).getName());
        assertEquals("c2", model.getConstraints().get(1).getName());
        interpreter.initialBind();
        assertTrue("Constraint 1 expression evaluated with mistake", interpreter.evaluateConstraint(0));
        assertTrue("Constraint 2 expression evaluated with mistake", interpreter.evaluateConstraint(1));
        model.getVarRef("x1").setBindValue(-2.0);
        model.getVarRef("x2").setBindValue(-3.0);
        assertTrue("Constraint 1 expression evaluated with mistake", interpreter.evaluateConstraint(0));
        assertFalse("Constraint 2 expression evaluated with mistake", interpreter.evaluateConstraint(1));
        model.getVarRef("x1").setBindValue(20.0);
        model.getVarRef("x2").setBindValue(-1.0);
        assertFalse("Constraint 1 expression evaluated with mistake", interpreter.evaluateConstraint(0));
        assertFalse("Constraint 2 expression evaluated with mistake", interpreter.evaluateConstraint(1));
        model.getVarRef("x1").setBindValue(20.0);
        model.getVarRef("x2").setBindValue(-1.0);
        assertFalse("Constraint 1 expression evaluated with mistake", interpreter.evaluateConstraint(0));
        assertFalse("Constraint 2 expression evaluated with mistake", interpreter.evaluateConstraint(1));
    }

    @Test
    public void testEvaluateGoal() {
        interpreter.initialBind();
        assertEquals("Goal expression evaluated with mistake", Double.valueOf(8.0), interpreter.evaluateGoal(0));
    }

    @Test
    public void testBuiltinFunctions() {
        interpreter.initialBind();
        
        assertVariable("v1", 1.0);
        assertVariable("v2", 1.0471975511965979);
        assertVariable("v3", 2.0634370688955608);
        assertVariable("v4", 1.5707963267948966);
        assertVariable("v15", 36.);
        assertVariable("v16", 0.);
              
        assertNotNull(model.getVarRef("v25").getValue().getTreeValue().getBuiltinFunction().toString(), model.getVarRef("v25").getBindValue());
        
        assertVariable("v26", 1.0);
        assertVariable("v27", 1.12);
        assertVariable("v28", 1.12);
        assertVariable("v29", -1.12);
        assertVariable("v30", -1.12);
    }

    private void assertVariable(String name, Double value) {
        assertEquals(model.getVarRef(name).getValue().getTreeValue().getBuiltinFunction().toString(),
                value, model.getVarRef(name).getBindValue(), 0.0001);
    }
}
