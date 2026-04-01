package com.cgsit.training.security.resource;

import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.Map;

/**
 * Public endpoint — no authentication required.
 *
 * @PermitAll allows access without credentials.
 * This works even though web.xml requires auth for /api/* —
 * because @PermitAll overrides the constraint for this resource.
 */
@Path("/public")
@Produces(MediaType.APPLICATION_JSON)
@PermitAll
public class PublicResource {

    @GET
    public Map<String, String> health() {
        return Map.of(
            "status", "UP",
            "message", "This endpoint is public — no authentication required"
        );
    }
}
