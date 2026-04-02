package com.cgsit.training.advancedrest.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.ext.Provider;

/**
 * CORS (Cross-Origin Resource Sharing) Filter.
 *
 * Without this filter, a browser running an Angular/React app on
 * http://localhost:4200 cannot call the REST API on http://localhost:8080
 * — the browser blocks the request (Same-Origin Policy).
 *
 * This filter adds the required CORS headers to every response:
 *   Access-Control-Allow-Origin   — which origins may call the API
 *   Access-Control-Allow-Methods  — which HTTP methods are allowed
 *   Access-Control-Allow-Headers  — which request headers are allowed
 *   Access-Control-Max-Age        — how long the browser caches the preflight
 *
 * In production, restrict Access-Control-Allow-Origin to your frontend domain
 * instead of "*" (wildcard).
 *
 * CORS flow:
 *   1. Browser sends OPTIONS preflight request
 *   2. Server responds with CORS headers
 *   3. Browser checks headers → allows or blocks the actual request
 *   4. Actual GET/POST/PUT/DELETE request follows
 */
@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext request,
                       ContainerResponseContext response) {
        response.getHeaders().add("Access-Control-Allow-Origin", "*");
        response.getHeaders().add("Access-Control-Allow-Methods",
                "GET, POST, PUT, DELETE, OPTIONS");
        response.getHeaders().add("Access-Control-Allow-Headers",
                "Content-Type, Authorization, X-Requested-With");
        response.getHeaders().add("Access-Control-Max-Age", "86400");
    }
}
