/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.server;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

public class EmbeddedServer {

    private static Server server;
    private final static int PORT = 8080;

    private static void setUp() throws Exception {
		server = new Server(PORT);
		File f = new File("target");
		String[] s = f.list(new FilenameFilter() {
            
            @Override
            public boolean accept(File dir, String name) {
                return name.matches("ampl-server-\\d+\\.\\d+\\.\\d+(-SNAPSHOT)?");
            }
        });
		WebAppContext ctx = new WebAppContext(f.getCanonicalPath() + File.separator + s[0],
				"/ampl");
		server.addHandler(ctx);
	}

    public static void start() throws Exception {
        setUp();
        server.start();
    }

    public static void stop() throws Exception {
        server.stop();
    }
}
