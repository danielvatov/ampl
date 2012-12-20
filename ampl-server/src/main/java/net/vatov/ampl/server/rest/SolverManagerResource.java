/**
 *  Copyright (C) 2011 by Daniel Vatov
 */
package net.vatov.ampl.server.rest;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;

import net.vatov.ampl.server.SolverManager;


//TODO изходният формат не е дефиниран
@Path("/")
public class SolverManagerResource {

	@GET
	@Path("solvers")
	@Produces(MediaType.TEXT_PLAIN)
	public String getSolvers() {
	    List<String> solvers = SolverManager.getInstance().getSolvers();
	    StringBuilder sb = new StringBuilder();
	    for (String s : solvers) {
	        sb.append(s).append("\n");
	    }
		return sb.toString();
	}
	
	@POST
	@Path("execute/{solverName}")
	@Consumes(MediaType.TEXT_PLAIN)
	public Response executeSolver(@PathParam("solverName") String solverName, String input) {
	    UUID uuid = SolverManager.getInstance().executeSolver(solverName, input);
	    URI uri = UriBuilder.fromResource(getClass()).path(uuid.toString()).build((Object)null);
	    return Response.created(uri).build();
	}
	
	@GET
	@Path("execute/{solverName}/{uuid}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response executionResult(@PathParam("uuid") String uuid) {
	    UUID executionUuid = UUID.fromString(uuid);
	    Map<String, String> result = SolverManager.getInstance().getExecutionResult(executionUuid);
	    if (null == result) {
	        return Response.status(Status.ACCEPTED).build();
	    }
	    return Response.ok(result.toString()).build();
	}
}
