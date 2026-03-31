package com.cgsit.training.cdi.qualifier;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.Map;

// @ApplicationScoped: Resource hat keinen State — eine Instanz reicht.
// JAX-RS Default wäre @RequestScoped, aber das erzeugt unnötigen Overhead.
@Path("/checkout")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class CheckoutResource {

    @Inject @CreditCard
    PaymentService creditCard;

    @Inject @PayPal
    PaymentService paypal;

    // GET /api/checkout?amount=99.99&method=creditcard
    @GET
    public Map<String, String> pay(
            @QueryParam("amount") @DefaultValue("100.00") double amount,
            @QueryParam("method") @DefaultValue("creditcard") String method) {

        PaymentService selected = "paypal".equalsIgnoreCase(method) ? paypal : creditCard;
        return Map.of(
            "method", selected.getName(),
            "result", selected.pay(amount)
        );
    }
}
