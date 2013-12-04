/**
 *  Copyright (C) 2010 by Daniel Vatov
 */
package net.vatov.ampl.model.xml;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import net.vatov.ampl.model.ConstraintDeclaration;
import net.vatov.ampl.model.ObjectiveDeclaration;
import net.vatov.ampl.model.OptimModel;
import net.vatov.ampl.model.SymbolDeclaration;

import org.apache.log4j.Logger;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import com.thoughtworks.xstream.io.naming.NameCoder;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.CompactWriter;
import com.thoughtworks.xstream.io.xml.PrettyPrintWriter;
import com.thoughtworks.xstream.io.xml.XppReader;

public class AmplXmlPersister {

    private static Logger logger = Logger.getLogger(AmplXmlPersister.class);
    
    private XStream xstream = new XStream();
    private NameCoder coder = new NoNameCoder();
    
    public AmplXmlPersister() {
        xstream.setMode(XStream.NO_REFERENCES); // lower/upper bound constrains requirement
        xstream.alias("model", OptimModel.class);
        xstream.alias("symbol", SymbolDeclaration.class);
        xstream.alias("objective", ObjectiveDeclaration.class);
        xstream.alias("constraint", ConstraintDeclaration.class);
        xstream.registerConverter(new SymbolDeclarationConverter());
        xstream.registerConverter(new ExpressionConverter());
        xstream.registerConverter(new ObjectiveDeclarationConverter());
        xstream.registerConverter(new ConstraintDeclarationConverter());
    }

    public OptimModel read(InputStream inputStream) {
        try {
            XmlPullParserFactory instance = XmlPullParserFactory.newInstance();
            return (OptimModel) xstream.unmarshal(new XppReader(new InputStreamReader(inputStream), instance.newPullParser(), coder));
        } catch (XmlPullParserException e) {
            throw new XmlModelException(e);
        }
    }

    public void write(OptimModel model, OutputStream outputStream) {
        HierarchicalStreamWriter writer;
        if (logger.isDebugEnabled()) {
            writer = new PrettyPrintWriter(new OutputStreamWriter(outputStream), coder);
        } else {
            writer = new CompactWriter(new OutputStreamWriter(outputStream), coder);            
        }
        xstream.marshal(model, writer);
    }
}
