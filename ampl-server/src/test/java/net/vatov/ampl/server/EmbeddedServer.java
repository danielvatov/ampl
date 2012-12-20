/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.server;

import java.io.File;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.webapp.WebAppContext;

public class EmbeddedServer {

	private static Server server;
	private final static int PORT = 8080;

	private static void setUp() throws Exception {
		server = new Server(PORT);
		WebAppContext ctx = new WebAppContext(new File(
				"target/ampl-server-0.0.1-SNAPSHOT").getCanonicalPath(),
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
