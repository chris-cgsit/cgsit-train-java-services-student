package com.cgsit.training.testing.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class ProductNotFoundExceptionMapper implements ExceptionMapper<ProductNotFoundException> {

    @Override
    public Response toResponse(ProductNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND)
                .entity(Map.of(
                    "status", 404,
                    "error", e.getMessage()
                ))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
