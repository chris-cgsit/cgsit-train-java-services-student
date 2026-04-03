package com.cgsit.training.customer.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.Map;

/**
 * TODO: Version-Endpoint implementieren.
 *
 * Aufgabe:
 *   1. Injiziere die Property "app.version" per MicroProfile Config:
 *
 *      @Inject
 *      @ConfigProperty(name = "app.version", defaultValue = "unknown")
 *      String appVersion;
 *
 *   2. Injiziere die Property "app.name" per MicroProfile Config:
 *
 *      @Inject
 *      @ConfigProperty(name = "app.name", defaultValue = "Customer API")
 *      String appName;
 *
 *   3. Implementiere getVersion(): gib eine Map mit name + version zurueck
 *
 *   4. Setze die Version als System Property im WildFly CLI:
 *      jboss-cli.bat -c
 *      /system-property=app.version:add(value=1.0.0)
 *      /system-property=app.name:add(value=Customer API Production)
 *
 *   5. Teste: GET /api/version → {"name": "Customer API Production", "version": "1.0.0"}
 *
 * Imports:
 *   import jakarta.inject.Inject;
 *   import org.eclipse.microprofile.config.inject.ConfigProperty;
 *
 * Hinweis: Braucht standalone-microprofile.xml (MicroProfile Config Subsystem).
 */
@Path("/version")
@Produces(MediaType.APPLICATION_JSON)
public class VersionResource {

    // TODO: @Inject @ConfigProperty(name = "app.version") ...
    // TODO: @Inject @ConfigProperty(name = "app.name") ...

    @GET
    public Map<String, String> getVersion() {
        // TODO: Implementiere — gib name + version als Map zurueck
        //
        // Erwartetes JSON:
        // {
        //   "name": "Customer API",
        //   "version": "1.0.0"
        // }
        //
        // Tipp: Map.of("name", appName, "version", appVersion)

        return Map.of(
            "name", "TODO: inject app.name",
            "version", "TODO: inject app.version"
        );
    }
}
