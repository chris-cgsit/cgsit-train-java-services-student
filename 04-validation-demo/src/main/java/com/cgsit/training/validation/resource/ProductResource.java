package com.cgsit.training.validation.resource;

import com.cgsit.training.validation.constraint.ValidCategory;
import com.cgsit.training.validation.data.ProductStore;
import com.cgsit.training.validation.model.Product;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.math.BigDecimal;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Products", description = "CRUD with Bean Validation")
public class ProductResource {

    @Inject
    ProductStore store;

    @GET
    @Operation(summary = "List all products")
    public List<Product> getAll() {
        return store.findAll();
    }

    // Bean Validation on @QueryParam — validates BEFORE method executes.
    // GET /api/products/search?name=Key&category=Electronics&minPrice=10&maxPrice=500
    @GET
    @Path("/search")
    @Operation(summary = "Search products (validated query parameters)")
    @APIResponse(responseCode = "200", description = "Matching products")
    @APIResponse(responseCode = "400", description = "Invalid search parameters")
    public List<Product> search(

            @Parameter(description = "Name filter (min 2 chars)", example = "Key")
            @QueryParam("name")
            @Size(min = 2, max = 50, message = "Suchbegriff muss zwischen 2 und 50 Zeichen lang sein")
            String name,

            @Parameter(description = "Category filter", example = "Electronics")
            @QueryParam("category")
            @ValidCategory
            String category,

            @Parameter(description = "Minimum price", example = "10")
            @QueryParam("minPrice")
            @Positive(message = "Mindestpreis muss positiv sein")
            BigDecimal minPrice,

            @Parameter(description = "Maximum price", example = "500")
            @QueryParam("maxPrice")
            @Positive(message = "Maximalpreis muss positiv sein")
            BigDecimal maxPrice) {

        return store.findAll().stream()
                .filter(p -> name == null || p.name().toLowerCase().contains(name.toLowerCase()))
                .filter(p -> category == null || category.equals(p.category()))
                .filter(p -> minPrice == null || p.price().compareTo(minPrice) >= 0)
                .filter(p -> maxPrice == null || p.price().compareTo(maxPrice) <= 0)
                .toList();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Get product by ID")
    @APIResponse(responseCode = "200", description = "Product found")
    @APIResponse(responseCode = "404", description = "Product not found")
    public Response getById(
            @Parameter(description = "Product ID (must be >= 1)", example = "1")
            @PathParam("id") @Min(value = 1, message = "ID muss >= 1 sein") long id) {
        return store.findById(id)
                .map(p -> Response.ok(p).build())
                .orElse(Response.status(Status.NOT_FOUND).build());
    }

    @POST
    @Operation(summary = "Create product (with validation)")
    @APIResponse(responseCode = "201", description = "Product created",
            content = @Content(schema = @Schema(implementation = Product.class)))
    @APIResponse(responseCode = "400", description = "Validation failed")
    public Response create(@Valid Product product) {
        Product created = store.save(product);
        return Response.status(Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    @Operation(summary = "Update product (with validation)")
    @APIResponse(responseCode = "200", description = "Product updated")
    @APIResponse(responseCode = "400", description = "Validation failed")
    @APIResponse(responseCode = "404", description = "Product not found")
    public Response update(
            @Parameter(description = "Product ID", example = "1")
            @PathParam("id") long id,
            @Valid Product product) {
        return store.update(id, product)
                .map(p -> Response.ok(p).build())
                .orElse(Response.status(Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Delete product")
    @APIResponse(responseCode = "204", description = "Product deleted")
    @APIResponse(responseCode = "404", description = "Product not found")
    public Response delete(
            @Parameter(description = "Product ID", example = "1")
            @PathParam("id") long id) {
        if (store.delete(id)) {
            return Response.noContent().build();
        }
        return Response.status(Status.NOT_FOUND).build();
    }
}
