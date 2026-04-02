package com.cgsit.training.advancedrest.resource;

import com.cgsit.training.advancedrest.model.*;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Product REST Resource — demonstrates the Search/Paging pattern
 * used by Angular/React frontends (Material Table, PrimeNG, ag-Grid).
 *
 * Search pattern:
 *   GET /api/products/search?name=Key&category=Electronics&page=0&size=10&sort=price&dir=ASC
 *   → SearchResult<Product> with content, pagination metadata, sort info
 */
@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductResource {

    @Inject
    ProductStore store;

    @GET
    public List<Product> getAll() {
        return store.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") long id) {
        return store.findById(id)
                .map(p -> Response.ok(p).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    /**
     * Paginated search endpoint.
     *
     * Query Parameters:
     *   name     — filter by name (contains, case-insensitive)
     *   category — filter by exact category
     *   page     — page number (0-based, default 0)
     *   size     — page size (default 10, max 100)
     *   sort     — sort field: id, name, price, category (default: id)
     *   dir      — sort direction: ASC, DESC (default: ASC)
     *
     * Response: SearchResult<Product> with content + pagination metadata.
     */
    @GET
    @Path("/search")
    public SearchResult<Product> search(
            @QueryParam("name") String name,
            @QueryParam("category") String category,
            @QueryParam("page") @DefaultValue("0") int page,
            @QueryParam("size") @DefaultValue("10") int size,
            @QueryParam("sort") @DefaultValue("id") String sort,
            @QueryParam("dir") @DefaultValue("ASC") SortDirection dir) {

        // Clamp size to 1-100
        size = Math.max(1, Math.min(size, 100));
        page = Math.max(0, page);

        List<Product> filtered = store.search(name, category, sort, dir);
        return SearchResult.of(filtered, page, size, sort, dir);
    }

    @POST
    public Response create(Product product) {
        Product created = store.save(product);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }
}
