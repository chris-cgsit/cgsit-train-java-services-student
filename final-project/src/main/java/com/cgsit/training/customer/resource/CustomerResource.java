package com.cgsit.training.customer.resource;

import com.cgsit.training.customer.model.Customer;
import com.cgsit.training.customer.service.CustomerService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

/**
 * Customer REST Resource.
 *
 * GET /api/customers           — Liste aller Kunden (fertig)
 * GET /api/customers/{id}      — Einzelner Kunde (TODO)
 * POST /api/customers          — Neuer Kunde (TODO)
 * PUT /api/customers/{id}      — Kunde aktualisieren (TODO)
 * DELETE /api/customers/{id}   — Kunde loeschen (TODO)
 *
 * Hinweise:
 *   - @Valid auf dem Request Body aktiviert Bean Validation
 *   - POST soll 201 (Created) zurueckgeben, nicht 200
 *   - DELETE soll 204 (No Content) zurueckgeben
 *   - Der Service wirft Exceptions — der ExceptionMapper wandelt sie um
 */
@Path("/customers")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Customers", description = "Customer CRUD API")
public class CustomerResource {

    @Inject
    CustomerService service;

    // Beispiel (fertig implementiert)
    @GET
    @Operation(summary = "List all customers")
    public List<Customer> getAll() {
        return service.findAll();
    }

    // TODO: GET /customers/{id}
    //   - Annotation: @GET + @Path("/{id}")
    //   - Parameter: @PathParam("id") Long id
    //   - Rufe service.findById(id) auf
    //   - Gib das Customer-Objekt direkt zurueck
    //   - Bei nicht gefunden wirft der Service automatisch CustomerNotFoundException


    // TODO: POST /customers
    //   - Annotation: @POST
    //   - Parameter: @Valid Customer customer (Bean Validation!)
    //   - Rufe service.create(customer) auf
    //   - Gib Response mit Status 201 (CREATED) und dem erstellten Customer zurueck
    //   - Tipp: Response.status(Response.Status.CREATED).entity(created).build()


    // TODO: PUT /customers/{id}
    //   - Annotation: @PUT + @Path("/{id}")
    //   - Parameter: @PathParam("id") Long id, @Valid Customer customer
    //   - Rufe service.update(id, customer) auf
    //   - Gib das aktualisierte Customer-Objekt zurueck


    // TODO: DELETE /customers/{id}
    //   - Annotation: @DELETE + @Path("/{id}")
    //   - Parameter: @PathParam("id") Long id
    //   - Rufe service.delete(id) auf
    //   - Gib Response mit Status 204 (NO_CONTENT) zurueck
    //   - Tipp: Response.noContent().build()

}
