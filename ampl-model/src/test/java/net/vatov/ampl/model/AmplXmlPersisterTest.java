/**
 *  Copyright (C) 2010 by Daniel Vatov
 */
package net.vatov.ampl.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;

import net.vatov.ampl.AmplParser;
import net.vatov.ampl.model.OptimModel;
import net.vatov.ampl.model.comparators.ComparisonResult;
import net.vatov.ampl.model.comparators.OptimModelComparator;
import net.vatov.ampl.model.xml.AmplXmlPersister;

import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;


public class AmplXmlPersisterTest {

    private static Logger logger = Logger.getLogger(AmplXmlPersisterTest.class);
    private OptimModel model;
    
    @Before
    public void setUp () throws Exception {
        URL url = getClass().getResource("params.mod");
        BufferedInputStream is = (BufferedInputStream)url.getContent();
        AmplParser parser = new AmplParser( );
        model = parser.parse(is);
        model.getVarRef("x1").setBindValue(5.0);
    }
    
    @Test
    public final void testWriteRead() {
        AmplXmlPersister p = new AmplXmlPersister();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        p.write(model, baos);
        String xml = baos.toString();
        logger.debug(xml);
        assertNotNull(xml);
        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        OptimModel m = p.read(bais);
        assertEquals(ComparisonResult.EQUALS, new OptimModelComparator().compare(model, m));
    }
}
