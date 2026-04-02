package com.cgsit.training.bookapi.mapper;

import com.cgsit.training.bookapi.exception.ServiceException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class ServiceExceptionMapper implements ExceptionMapper<ServiceException> {

    @Override
    public Response toResponse(ServiceException e) {
        return Response.status(e.getStatusCode())
                .entity(Map.of(
                    "status", e.getStatusCode(),
                    "error", e.getMessage()
                ))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
