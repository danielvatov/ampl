package net.vatov.ampl;

import java.io.InputStream;

import net.vatov.ampl.AmplCombinedParser.model_return;
import net.vatov.ampl.model.OptimModel;

import org.antlr.runtime.ANTLRInputStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.tree.CommonTreeNodeStream;


/**
 * Copyright (C) 2010 by Daniel Vatov
 */

public class AmplParser {

    public OptimModel parse(InputStream ampl) throws AmplException {
        try {
            ANTLRInputStream is = new ANTLRInputStream(ampl);
            AmplCombinedLexer lexer = new AmplCombinedLexer(is);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            AmplCombinedParser parser = new AmplCombinedParser(tokens);
            model_return model = parser.model();
            CommonTreeNodeStream tns = new CommonTreeNodeStream(model.getTree());
            Ampl walker = new Ampl(tns);
            OptimModel ret = walker.model();
            return ret;
        } catch (Exception e) {
            throw new AmplException(e);
        }
    }
}
