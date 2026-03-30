package com.cgsit.training.servicelayer.bad;

import com.cgsit.training.servicelayer.model.Product;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/*
 * ============================================================
 * ANTI-PATTERN: Everything in one class
 * ============================================================
 *
 * This resource does EVERYTHING:
 * - HTTP handling (JAX-RS annotations)
 * - Business logic (filtering, sorting, validation)
 * - Data storage (in-memory list)
 *
 * Problems:
 * - Not testable (can't test business logic without HTTP)
 * - Not reusable (logic tied to REST, can't use from CLI/Batch)
 * - Hard to maintain (one change can break everything)
 * - Violates Single Responsibility Principle
 *
 * → See the "good" package for the clean version.
 * ============================================================
 */
@Path("/bad/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EverythingInOneResource {

    // Data storage directly in the resource — BAD!
    private static final List<Product> products = new ArrayList<>();
    private static final AtomicLong idCounter = new AtomicLong();

    static {
        // Test data — also in the resource!
        products.add(new Product(idCounter.incrementAndGet(), "Laptop", 999.99, "Electronics"));
        products.add(new Product(idCounter.incrementAndGet(), "Tastatur", 79.99, "Peripherie"));
        products.add(new Product(idCounter.incrementAndGet(), "Defektes Teil", -10.00, "Schrott"));
        products.add(new Product(idCounter.incrementAndGet(), "Monitor 27\"", 349.00, "Electronics"));
    }

    @GET
    public List<Product> getAll(@QueryParam("sort") @DefaultValue("name") String sort) {
        // Business logic directly in HTTP handler — BAD!
        return products.stream()
                .filter(p -> p.price() > 0)                    // Filtering here?
                .sorted(sort.equals("price")                   // Sorting here?
                        ? Comparator.comparingDouble(Product::price)
                        : Comparator.comparing(Product::name))
                .toList();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") long id) {
        // Search logic in the resource — BAD!
        for (Product p : products) {
            if (p.id() == id) {
                return Response.ok(p).build();
            }
        }
        return Response.status(Response.Status.NOT_FOUND)
                .entity("{\"error\":\"Product not found\"}")   // Manual JSON — BAD!
                .build();
    }

    @POST
    public Response create(Product product) {
        // Validation logic in the resource — BAD!
        if (product.name() == null || product.name().isBlank()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Name is required\"}")
                    .build();
        }
        if (product.price() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Price must be positive\"}")
                    .build();
        }

        // Storage logic in the resource — BAD!
        Product created = new Product(
                idCounter.incrementAndGet(),
                product.name(),
                product.price(),
                product.category()
        );
        products.add(created);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
}
