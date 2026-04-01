package com.cgsit.training.bookapi.mapper;

import com.cgsit.training.bookapi.exception.ServiceException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ServiceExceptionMapper implements ExceptionMapper<ServiceException> {

    @Override
    public Response toResponse(ServiceException e) {
        ErrorResponse error = new ErrorResponse(
            e.getStatusCode(),
            Response.Status.fromStatusCode(e.getStatusCode()).getReasonPhrase(),
            e.getMessage()
        );
        return Response.status(e.getStatusCode())
                       .entity(error)
                       .type(MediaType.APPLICATION_JSON)
                       .build();
    }
}
