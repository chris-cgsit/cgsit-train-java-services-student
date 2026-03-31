package com.cgsit.training.rest.resource;

/*
 * ============================================================
 * IMPORT REFERENCE (safe from IntelliJ "Optimize Imports")
 * ============================================================
 *
 * --- JAX-RS Core (jakarta.ws.rs.*) — required for REST ---
 * @Consumes          Declares which content types this endpoint accepts (e.g. JSON)
 * @DELETE            Maps method to HTTP DELETE
 * @DefaultValue      Default value for optional query parameters
 * @GET               Maps method to HTTP GET
 * @POST              Maps method to HTTP POST
 * @PUT               Maps method to HTTP PUT
 * @Path              Defines the URI path for this resource or method
 * @PathParam         Extracts a value from the URI path (e.g. /products/{id})
 * @Produces          Declares which content types this endpoint returns
 * @QueryParam        Extracts a value from the query string (e.g. ?category=X)
 * MediaType          Constants for content types (APPLICATION_JSON, etc.)
 * Response           Builder for HTTP responses with status code, headers, body
 * Response.Status    HTTP status code constants (OK, CREATED, NOT_FOUND, etc.)
 *
 * --- CDI (jakarta.inject.*) — dependency injection ---
 * @Inject            Marks an injection point — container provides the dependency
 *
 * --- MicroProfile OpenAPI (org.eclipse.microprofile.openapi.*) — OPTIONAL, docs only ---
 * @Operation         Summary & description for an endpoint (shown in Swagger UI)
 * @Parameter         Describes a path/query parameter (name, description, required)
 * @APIResponse       Documents one possible HTTP response (status + description)
 * @APIResponses      Groups multiple @APIResponse on one method
 * @Tag               Groups endpoints into sections in Swagger UI
 *
 * NOTE: OpenAPI annotations have NO effect on runtime behavior.
 * Remove them and the API works identically — you just lose the documentation.
 * ============================================================
 */

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import com.cgsit.training.rest.repository.ProductRepository;
import com.cgsit.training.rest.model.Product;
import java.util.List;

/**
 * REST Resource for Product CRUD operations.
 *
 * <h3>JAX-RS Annotations (Core — required):</h3>
 * <ul>
 *   <li>{@code @Path} — URI path for this resource (/api/products)</li>
 *   <li>{@code @GET/@POST/@PUT/@DELETE} — HTTP method mapping</li>
 *   <li>{@code @Produces/@Consumes} — content type (JSON)</li>
 *   <li>{@code @PathParam/@QueryParam} — extract values from URI</li>
 *   <li>{@code @Inject} — CDI injects the ProductRepository</li>
 * </ul>
 *
 * <h3>OpenAPI Annotations (Optional — documentation only):</h3>
 * <ul>
 *   <li>{@code @Tag} — groups endpoints in Swagger UI</li>
 *   <li>{@code @Operation} — summary and description for each endpoint</li>
 *   <li>{@code @APIResponse} — documents possible HTTP responses</li>
 *   <li>{@code @Parameter} — describes path/query parameters</li>
 * </ul>
 *
 * <p>The OpenAPI annotations have NO effect on runtime behavior.
 * Remove them and the API works identically — you just lose the documentation.</p>
 *
 * @see <a href="https://jakarta.ee/specifications/restful-ws/3.1/">Jakarta RESTful Web Services 3.1</a>
 * @see <a href="https://download.eclipse.org/microprofile/microprofile-open-api-4.0/">MicroProfile OpenAPI 4.0</a>
 */
@Path("/products")                                      // Base path: all methods are under /api/products
@Produces(MediaType.APPLICATION_JSON)                   // All responses are JSON
@Consumes(MediaType.APPLICATION_JSON)                   // All request bodies must be JSON
@Tag(name = "Products", description = "Product management CRUD operations")  // OpenAPI: group in Swagger UI
public class ProductResource {

    /**
     * CDI injection — the container creates and manages the ProductRepository singleton.
     * No "new ProductRepository()" needed. The @ApplicationScoped store is shared across all requests.
     */
    @Inject
    ProductRepository repository;

    /**
     * GET /api/products — List all products, optionally filtered by category.
     *
     * @param category optional query parameter to filter by category name
     * @return list of products (always 200, even if empty)
     */
    @GET                                                // HTTP GET method
    @Operation(summary = "List all products",           // OpenAPI: short description in Swagger
               description = "Returns all products, optionally filtered by category")
    @APIResponse(responseCode = "200", description = "List of products")  // OpenAPI: documents 200 response
    public List<Product> getAll(
            @Parameter(description = "Filter by category (e.g. Electronics, Peripherie)")  // OpenAPI: parameter docs
            @QueryParam("category") String category) {  // JAX-RS: extracts ?category=... from URL

        List<Product> all = repository.findAll();
        if (category != null && !category.isBlank()) {
            return all.stream()
                    .filter(p -> p.category().equalsIgnoreCase(category))
                    .toList();
        }
        return all;
    }

    /**
     * GET /api/products/{id} — Get a single product by its ID.
     *
     * @param id the product ID from the URL path
     * @return 200 + product JSON if found, 404 if not found
     */
    @GET
    @Path("/{id}")                                      // JAX-RS: appends /{id} to the base path
    @Operation(summary = "Get a product by ID")
    @APIResponses({                                     // OpenAPI: multiple possible responses
        @APIResponse(responseCode = "200", description = "Product found"),
        @APIResponse(responseCode = "404", description = "Product not found")
    })
    public Response getById(
            @Parameter(description = "Product ID", required = true)
            @PathParam("id") long id) {                 // JAX-RS: extracts {id} from /products/42

        return repository.findById(id)
                .map(p -> Response.ok(p).build())       // Found → 200 OK + JSON body
                .orElse(Response.status(Status.NOT_FOUND).build());  // Not found → 404
    }

    /**
     * POST /api/products — Create a new product.
     * The product JSON is automatically deserialized from the request body.
     *
     * @param product deserialized from JSON request body (JSON-B handles this)
     * @return 201 Created + the created product with generated ID
     */
    @POST
    @Operation(summary = "Create a new product")
    @APIResponses({
        @APIResponse(responseCode = "201", description = "Product created"),
        @APIResponse(responseCode = "400", description = "Invalid input")
    })
    public Response create(Product product) {           // JAX-RS: JSON body → Product record (automatic)

        Product created = repository.save(product);
        return Response
                .status(Status.CREATED)                 // 201 Created
                .entity(created)                        // JSON body with generated ID
                .build();
    }

    /**
     * PUT /api/products/{id} — Update an existing product.
     *
     * @param id the product ID from the URL path
     * @param product the updated product data from the request body
     * @return 200 + updated product if found, 404 if not found
     */
    @PUT
    @Path("/{id}")
    @Operation(summary = "Update an existing product")
    @APIResponses({
        @APIResponse(responseCode = "200", description = "Product updated"),
        @APIResponse(responseCode = "404", description = "Product not found")
    })
    public Response update(
            @Parameter(description = "Product ID", required = true)
            @PathParam("id") long id,
            Product product) {

        return repository.update(id, product)
                .map(p -> Response.ok(p).build())       // Found & updated → 200 OK
                .orElse(Response.status(Status.NOT_FOUND).build());
    }

    /**
     * DELETE /api/products/{id} — Delete a product.
     *
     * @param id the product ID from the URL path
     * @return 204 No Content if deleted, 404 if not found
     */
    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete a product")
    @APIResponses({
        @APIResponse(responseCode = "204", description = "Product deleted"),
        @APIResponse(responseCode = "404", description = "Product not found")
    })
    public Response delete(
            @Parameter(description = "Product ID", required = true)
            @PathParam("id") long id) {

        if (repository.delete(id)) {
            return Response.noContent().build();        // Deleted → 204 No Content (no body)
        }
        return Response.status(Status.NOT_FOUND).build();
    }
}
