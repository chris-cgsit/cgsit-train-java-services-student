package com.cgsit.training.servicelayer.resource;

import com.cgsit.training.servicelayer.model.Product;
import com.cgsit.training.servicelayer.service.ProductService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;

/*
 * ============================================================
 * CLEAN: Resource layer — responsible ONLY for HTTP handling.
 * ============================================================
 *
 * This resource is THIN:
 * - Takes HTTP requests
 * - Delegates to ProductService
 * - Returns HTTP responses
 *
 * No business logic here! Compare with bad/EverythingInOneResource.java
 * ============================================================
 */
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductService service;     // CDI injects the service — no "new"!

    @GET
    public List<Product> getAll(
            @QueryParam("sort") @DefaultValue("name") String sort) {
        return service.findAll(sort);   // Just delegate — no logic here
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") long id) {
        return service.findById(id)
                .map(p -> Response.ok(p).build())
                .orElse(Response.status(Status.NOT_FOUND).build());
    }

    @POST
    public Response create(Product product) {
        Product created = service.create(product);  // Service validates
        return Response.status(Status.CREATED).entity(created).build();
    }
}
