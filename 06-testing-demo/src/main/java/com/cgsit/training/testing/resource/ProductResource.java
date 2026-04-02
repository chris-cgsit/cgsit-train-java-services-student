package com.cgsit.training.testing.resource;

import com.cgsit.training.testing.model.Product;
import com.cgsit.training.testing.service.ProductService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;

import java.net.URI;
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
    public Product getById(@PathParam("id") Long id) {
        return service.findById(id);
    }

    @POST
    public Response create(@Valid Product product, @Context UriInfo uriInfo) {
        Product created = service.create(product);
        URI location = uriInfo.getAbsolutePathBuilder()
                .path(String.valueOf(created.id()))
                .build();
        return Response.created(location).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public Product update(@PathParam("id") Long id, @Valid Product product) {
        return service.update(id, product);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
