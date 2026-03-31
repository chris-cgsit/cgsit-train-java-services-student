package com.cgsit.training.cdi.events;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/orders")
@Produces(MediaType.APPLICATION_JSON)
public class OrderResource {

    @Inject
    OrderService orderService;

    // POST /api/orders?customer=Max&total=199.99
    @POST
    public Response createOrder(
            @QueryParam("customer") @DefaultValue("Max Mustermann") String customer,
            @QueryParam("total") @DefaultValue("99.99") double total) {

        var event = orderService.createOrder(customer, total);
        return Response.status(Response.Status.CREATED).entity(event).build();
    }
}
