package com.cgsit.training.bookapi.mapper;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class CatchAllExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(CatchAllExceptionMapper.class.getName());

    @Override
    public Response toResponse(Exception e) {
        LOG.log(Level.SEVERE, "Unhandled exception", e);

        ErrorResponse error = new ErrorResponse(
            500,
            "Internal Server Error",
            "An unexpected error occurred"
        );
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity(error)
                       .type(MediaType.APPLICATION_JSON)
                       .build();
    }
}
