package com.cgsit.training.advancedrest.resource;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.Map;

/**
 * API Versioning — demonstrates different strategies.
 *
 * Strategy 1: URL Path Versioning (most common)
 *   GET /api/v1/info
 *   GET /api/v2/info
 *
 * Strategy 2: Header Versioning
 *   GET /api/info  +  Accept: application/vnd.cgsit.v2+json
 *
 * Strategy 3: Query Parameter
 *   GET /api/info?version=2
 *
 * Recommendation: URL Path Versioning — simple, visible, cacheable.
 */
@Produces(MediaType.APPLICATION_JSON)
public class VersionedResource {

    /**
     * V1 — original API response format.
     * Separate class per version, mounted at different paths.
     */
    @Path("/v1/info")
    public static class V1 {
        @GET
        public Map<String, String> info() {
            return Map.of(
                "version", "1",
                "name", "Advanced REST Demo",
                "status", "running"
            );
        }
    }

    /**
     * V2 — extended response with additional fields.
     * V1 clients continue to work unchanged.
     */
    @Path("/v2/info")
    public static class V2 {
        @GET
        public Map<String, Object> info() {
            return Map.of(
                "version", "2",
                "name", "Advanced REST Demo",
                "status", "running",
                "features", java.util.List.of("search", "paging", "versioning", "cors"),
                "uptime", System.currentTimeMillis()
            );
        }
    }
}
