package com.cgsit.training.bookapi.resource;

import com.cgsit.training.bookapi.model.Book;
import com.cgsit.training.bookapi.service.BookService;
import jakarta.inject.Inject;
// TODO 7: Importiere jakarta.validation.Valid
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
    BookService service;

    @GET
    public List<Book> getAll() {
        return service.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") long id) {
        return service.findById(id)
                .map(b -> Response.ok(b).build())
                .orElse(Response.status(Status.NOT_FOUND).build());
    }

    @POST
    // TODO 8: Füge @Valid vor dem Book-Parameter hinzu
    public Response create(Book book) {
        Book created = service.create(book);
        return Response.status(Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    // TODO 9: Füge @Valid vor dem Book-Parameter hinzu
    public Response update(@PathParam("id") long id, Book book) {
        return service.update(id, book)
                .map(b -> Response.ok(b).build())
                .orElse(Response.status(Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        if (service.delete(id)) {
            return Response.noContent().build();
        }
        return Response.status(Status.NOT_FOUND).build();
    }
}
