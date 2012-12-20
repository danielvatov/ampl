/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.model.xml;

import java.util.HashMap;
import java.util.Map;

import net.vatov.ampl.model.ModelException;
import net.vatov.ampl.model.SymbolDeclaration;


import com.thoughtworks.xstream.converters.UnmarshallingContext;

public class Util {

    static final String SYMBOLS_KEY = "symbols";

    static void initContextObject(UnmarshallingContext ctx, String key, Class<?> c) throws InstantiationException,
            IllegalAccessException {
        Object o = ctx.get(key);
        if (null == o) {
            o = c.newInstance();
            ctx.put(key, o);
        }
    }

    static Object getContextObject(UnmarshallingContext ctx, String key, Class<?> c) {
        try {
            initContextObject(ctx, key, c);
        } catch (Exception e) {
            throw new ModelException(e);
        }
        return ctx.get(key);
    }

    static SymbolDeclaration getSymbolDeclaration(String name,
            UnmarshallingContext ctx) {
        Map<?, ?> symbols = (Map<?, ?>) getContextObject(ctx, SYMBOLS_KEY, HashMap.class);
        if (!symbols.containsKey(name)) {
            throw new ModelException("symbol " + name + " not defined");
        }
        return (SymbolDeclaration) symbols.get(name);
    }

    @SuppressWarnings("unchecked")
    static void setSymbolDeclaration(String name, SymbolDeclaration sd,
            UnmarshallingContext ctx) {
        @SuppressWarnings("rawtypes")
        Map symbols = (Map) getContextObject(ctx, SYMBOLS_KEY, HashMap.class);
        if (symbols.containsKey(name)) {
            throw new ModelException("symbol " + name + " already defined");
        }
        symbols.put(name, sd);
    }
}