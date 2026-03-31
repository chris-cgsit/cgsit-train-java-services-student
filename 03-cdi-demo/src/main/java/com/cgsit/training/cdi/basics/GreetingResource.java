package com.cgsit.training.cdi.basics;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

// JAX-RS Default wäre @RequestScoped (per Spec), aber wir nutzen @ApplicationScoped:
// Die Resource hat keinen State — eine Instanz reicht (weniger Overhead).
// State (z.B. User-Daten) gehört in eigene @RequestScoped oder @SessionScoped Beans.
@Path("/greeting")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class GreetingResource {

    @Inject
    GreetingService service;

    @GET
    @Path("/{name}")
    public String greet(@PathParam("name") String name) {
        return service.greet(name);
    }
}
