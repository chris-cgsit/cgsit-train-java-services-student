package com.cgsit.training.customer.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.Map;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/version")
@Produces(MediaType.APPLICATION_JSON)
public class VersionResource {

    @Inject
    @ConfigProperty(name = "app.version", defaultValue = "unknown")
    String appVersion;

    @Inject
    @ConfigProperty(name = "app.name", defaultValue = "Customer API")
    String appName;

    @GET
    public Map<String, String> getVersion() {
        return Map.of(
            "name", appName,
            "version", appVersion
        );
    }
}
