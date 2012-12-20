/**
 *  Copyright (C) 2010 by Daniel Vatov
 */
package net.vatov.ampl.model;

import static org.junit.Assert.*;

import net.vatov.ampl.model.ModelException;
import net.vatov.ampl.model.SymbolDeclaration;

import org.junit.Test;
public class SymbolDeclarationTest {

    @Test(expected=ModelException.class)
    public final void testCreateParamDeclaration() {
        SymbolDeclaration.createParamDeclaration(null);
    }

    @Test(expected=ModelException.class)
    public final void testCreateVarDeclaration() {
        SymbolDeclaration.createVarDeclaration(null);
    }

    @Test
    public final void testEqualsObject() {
        SymbolDeclaration param = SymbolDeclaration.createParamDeclaration("test");
        SymbolDeclaration var = SymbolDeclaration.createVarDeclaration("test");
        assertTrue(param.equals(var));
    }

}
