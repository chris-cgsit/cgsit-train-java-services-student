package com.cgsit.training.advancedrest.filter;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.container.ContainerResponseFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Simple Rate Limiting Filter — limits requests per client IP.
 *
 * Adds standard rate limit headers to every response:
 *   X-RateLimit-Limit      — max requests per window
 *   X-RateLimit-Remaining  — remaining requests in current window
 *   X-RateLimit-Reset      — seconds until the window resets
 *
 * When the limit is exceeded:
 *   HTTP 429 Too Many Requests + Retry-After header
 *
 * This is a simplified in-memory implementation for demonstration.
 * In production, use a distributed solution (Redis, API Gateway).
 *
 * Typical rate limit headers (used by GitHub, Twitter, Stripe):
 *   X-RateLimit-Limit: 100
 *   X-RateLimit-Remaining: 42
 *   X-RateLimit-Reset: 1712000000
 */
@Provider
public class RateLimitFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final int MAX_REQUESTS = 60;          // per window
    private static final long WINDOW_MILLIS = 60_000;    // 1 minute

    // Per-client counters (keyed by IP)
    private final Map<String, ClientWindow> clients = new ConcurrentHashMap<>();

    @Override
    public void filter(ContainerRequestContext request) {
        String clientIp = getClientIp(request);
        ClientWindow window = clients.compute(clientIp, (ip, existing) -> {
            if (existing == null || existing.isExpired()) {
                return new ClientWindow();
            }
            return existing;
        });

        int current = window.counter.incrementAndGet();
        // Store for response filter
        request.setProperty("rateLimitWindow", window);
        request.setProperty("rateLimitCount", current);

        if (current > MAX_REQUESTS) {
            long resetSeconds = window.resetInSeconds();
            request.abortWith(Response.status(429)
                    .header("Retry-After", resetSeconds)
                    .header("X-RateLimit-Limit", MAX_REQUESTS)
                    .header("X-RateLimit-Remaining", 0)
                    .header("X-RateLimit-Reset", window.resetEpochSeconds())
                    .entity(Map.of(
                        "status", 429,
                        "title", "Too Many Requests",
                        "detail", "Rate limit exceeded. Retry after " + resetSeconds + " seconds."
                    ))
                    .build());
        }
    }

    @Override
    public void filter(ContainerRequestContext request,
                       ContainerResponseContext response) {
        ClientWindow window = (ClientWindow) request.getProperty("rateLimitWindow");
        Integer count = (Integer) request.getProperty("rateLimitCount");
        if (window != null && count != null) {
            int remaining = Math.max(0, MAX_REQUESTS - count);
            response.getHeaders().add("X-RateLimit-Limit", MAX_REQUESTS);
            response.getHeaders().add("X-RateLimit-Remaining", remaining);
            response.getHeaders().add("X-RateLimit-Reset", window.resetEpochSeconds());
        }
    }

    private String getClientIp(ContainerRequestContext request) {
        String forwarded = request.getHeaderString("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        // Fallback — in a real app this would be the remote address
        return "127.0.0.1";
    }

    private static class ClientWindow {
        final long startTime = System.currentTimeMillis();
        final AtomicInteger counter = new AtomicInteger(0);

        boolean isExpired() {
            return System.currentTimeMillis() - startTime > WINDOW_MILLIS;
        }

        long resetInSeconds() {
            long elapsed = System.currentTimeMillis() - startTime;
            return Math.max(1, (WINDOW_MILLIS - elapsed) / 1000);
        }

        long resetEpochSeconds() {
            return (startTime + WINDOW_MILLIS) / 1000;
        }
    }
}
