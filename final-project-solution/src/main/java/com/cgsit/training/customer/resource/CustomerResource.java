package com.cgsit.training.customer.resource;

import com.cgsit.training.customer.model.Customer;
import com.cgsit.training.customer.service.CustomerService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.logging.Logger;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Customers", description = "Customer CRUD API")
public class CustomerResource {

    private static final Logger LOG = Logger.getLogger(CustomerResource.class.getName());

    @Inject
    CustomerService service;

    @GET
    @Operation(summary = "List all customers")
    public List<Customer> getAll() {
        LOG.info("GET /customers");
        return service.findAll();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get customer by ID")
    @APIResponse(responseCode = "200", description = "Customer found")
    @APIResponse(responseCode = "404", description = "Customer not found")
    public Customer getById(@PathParam("id") Long id) {
        LOG.info("GET /customers/" + id);
        return service.findById(id);
    }

    @POST
    @Operation(summary = "Create customer")
    @APIResponse(responseCode = "201", description = "Customer created")
    @APIResponse(responseCode = "400", description = "Validation failed")
    @APIResponse(responseCode = "409", description = "Email already exists")
    public Response create(@Valid Customer customer) {
        LOG.info("POST /customers — name=" + customer.name()
                + " email=" + customer.email()
                + " phone=" + customer.phone());
        Customer created = service.create(customer);
        LOG.info("Created customer id=" + created.id());
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update customer")
    @APIResponse(responseCode = "200", description = "Customer updated")
    @APIResponse(responseCode = "404", description = "Customer not found")
    public Customer update(@PathParam("id") Long id, @Valid Customer customer) {
        LOG.info("PUT /customers/" + id + " — name=" + customer.name()
                + " email=" + customer.email());
        return service.update(id, customer);
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete customer")
    @APIResponse(responseCode = "204", description = "Customer deleted")
    @APIResponse(responseCode = "404", description = "Customer not found")
    public Response delete(@PathParam("id") Long id) {
        LOG.info("DELETE /customers/" + id);
        service.delete(id);
        return Response.noContent().build();
    }
}
