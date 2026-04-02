package com.cgsit.training.bookapi.mapper;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Catch-all ExceptionMapper for unhandled technical exceptions.
 *
 * Catches everything that is NOT a ServiceException (those are handled
 * by ServiceExceptionMapper). Returns a generic 500 response without
 * leaking internal details (stack traces, class names) to the client.
 */
@Provider
public class CatchAllExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(CatchAllExceptionMapper.class.getName());

    @Override
    public Response toResponse(Exception e) {
        LOG.log(Level.SEVERE, "Unhandled exception: " + e.getMessage(), e);

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(Map.of(
                    "status", 500,
                    "error", "Internal Server Error",
                    "detail", "An unexpected error occurred"
                ))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
