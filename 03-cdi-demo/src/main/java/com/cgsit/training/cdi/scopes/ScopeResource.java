package com.cgsit.training.cdi.scopes;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.Map;

@Path("/scopes")
@Produces(MediaType.APPLICATION_JSON)
public class ScopeResource {

    @Inject
    RequestCounter requestCounter;

    @Inject
    AppCounter appCounter;

    // GET /api/scopes — shows the difference between scopes
    // requestCounter resets every request, appCounter persists
    @GET
    public Map<String, Object> showScopes() {
        requestCounter.increment();
        requestCounter.increment();
        requestCounter.increment();
        appCounter.increment();

        return Map.of(
            "requestScoped_count", requestCounter.getCount(),
            "applicationScoped_count", appCounter.getCount(),
            "explanation", Map.of(
                "requestScoped", "Always 3 — new instance per request",
                "applicationScoped", "Grows with every request — singleton"
            )
        );
    }
}
