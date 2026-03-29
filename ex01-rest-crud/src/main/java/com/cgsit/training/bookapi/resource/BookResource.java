package com.cgsit.training.bookapi.resource;

import com.cgsit.training.bookapi.data.BookStore;
import com.cgsit.training.bookapi.model.Book;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import java.util.List;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    @Inject
    BookStore store;

    // TODO: GET /api/books — Alle Bücher zurückgeben
    //
    // Hinweise:
    // - @GET Annotation
    // - Rückgabetyp: List<Book>
    // - Rufe store.findAll() auf


    // TODO: GET /api/books/{id} — Ein Buch nach ID
    //
    // Hinweise:
    // - @GET + @Path("/{id}")
    // - @PathParam("id") long id
    // - Rückgabetyp: Response
    // - Gefunden → Response.ok(book).build()
    // - Nicht gefunden → Response.status(Status.NOT_FOUND).build()


    // TODO: POST /api/books — Neues Buch erstellen
    //
    // Hinweise:
    // - @POST Annotation
    // - Parameter: Book book (wird aus JSON deserialisiert)
    // - Rückgabe: Response.status(Status.CREATED).entity(created).build()


    // TODO: PUT /api/books/{id} — Buch aktualisieren
    //
    // Hinweise:
    // - @PUT + @Path("/{id}")
    // - Parameter: @PathParam("id") long id, Book book
    // - Gefunden → Response.ok(updated).build()
    // - Nicht gefunden → Response.status(Status.NOT_FOUND).build()


    // TODO: DELETE /api/books/{id} — Buch löschen
    //
    // Hinweise:
    // - @DELETE + @Path("/{id}")
    // - Gelöscht → Response.noContent().build()  (204)
    // - Nicht gefunden → Response.status(Status.NOT_FOUND).build()

}
