/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.server.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.util.List;

import net.vatov.ampl.server.EmbeddedServer;
import net.vatov.ampl.solver.impl.LpSolverAdapter;

import org.apache.commons.io.IOUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


public class SolverManagerResourceIT extends Base {

    private final Logger logger = Logger.getLogger(SolverManagerResourceIT.class);
    
    @BeforeClass
    public static void setUp() throws Exception {
        EmbeddedServer.start();
    }

    @AfterClass
    public static void tearDown() throws Exception {
        EmbeddedServer.stop();
    }

    @Test
    public void testGet() throws ClientProtocolException, IOException {
        HttpResponse response = executeGet("ampl/rest/solvers");
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusLine().getStatusCode());
        HttpEntity entity = response.getEntity();
        assertNotNull(entity);
        InputStream content = entity.getContent();
        @SuppressWarnings("unchecked")
        List<String> lines = IOUtils.readLines(content);
        for (String s : lines) {
            logger.debug(s);
        }
    }

    @Test
    public void testPost() throws Exception {
        InputStream is = getClass().getResourceAsStream("lp.ampl");
        HttpResponse response = executePost("ampl/rest/execute/" + LpSolverAdapter.NAME, IOUtils.toString(is));
        assertEquals(HttpURLConnection.HTTP_CREATED, response.getStatusLine().getStatusCode());
        Header location = response.getFirstHeader("Location");
        int status = HttpURLConnection.HTTP_ACCEPTED;
        int tries = 0;
        do {
            if (0 != tries) {
                Thread.sleep(1000);
            }
            response = executeGet(URI.create(location.getValue()).getPath());
            status = response.getStatusLine().getStatusCode();
        } while (tries++ < 20 && HttpURLConnection.HTTP_ACCEPTED == status);
        assertEquals(HttpURLConnection.HTTP_OK, response.getStatusLine().getStatusCode());
        logger.debug(IOUtils.toString(response.getEntity().getContent()));
    }
}
