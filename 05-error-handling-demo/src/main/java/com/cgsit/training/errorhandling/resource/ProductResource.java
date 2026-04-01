package com.cgsit.training.errorhandling.resource;

import com.cgsit.training.errorhandling.model.Product;
import com.cgsit.training.errorhandling.service.ProductService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductService service;

    @GET
    public List<Product> getAll() {
        return service.findAll();
    }

    @GET
    @Path("/{id}")
    public Product getById(@PathParam("id") long id) {
        return service.findById(id);
    }

    @POST
    public Response create(@Valid Product product) {
        Product created = service.create(product);
        return Response.status(Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, @Valid Product product) {
        Product updated = service.update(id, product);
        return Response.ok(updated).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
