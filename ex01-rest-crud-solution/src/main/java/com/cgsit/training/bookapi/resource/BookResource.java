package com.cgsit.training.bookapi.resource;

import com.cgsit.training.bookapi.repository.BookRepository;
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
    BookRepository repository;

    @GET
    public List<Book> getAll() {
        return repository.findAll();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") long id) {
        return repository.findById(id)
                .map(b -> Response.ok(b).build())
                .orElse(Response.status(Status.NOT_FOUND).build());
    }

    @POST
    public Response create(Book book) {
        Book created = repository.save(book);
        return Response
                .status(Status.CREATED)
                .entity(created)
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response update(@PathParam("id") long id, Book book) {
        return repository.update(id, book)
                .map(b -> Response.ok(b).build())
                .orElse(Response.status(Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        if (repository.delete(id)) {
            return Response.noContent().build();
        }
        return Response.status(Status.NOT_FOUND).build();
    }
}
