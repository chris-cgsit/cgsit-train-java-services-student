package com.cgsit.training.customer.mapper;

import com.cgsit.training.customer.exception.ServiceException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;

/**
 * TODO: Implementiere diesen ExceptionMapper.
 *
 * Aufgabe:
 *   1. Annotiere die Klasse mit @Provider (damit WildFly sie findet)
 *   2. Implementiere toResponse():
 *      - Lies den statusCode aus der ServiceException
 *      - Erstelle eine JSON Response mit status + error message
 *      - Setze den Content-Type auf APPLICATION_JSON
 *
 * Ohne diesen Mapper gibt WildFly eine hässliche 500 Response
 * statt der erwarteten 404/409 mit JSON Body.
 *
 * Hinweis: Schau dir 05-error-handling-demo als Referenz an.
 */
public class ServiceExceptionMapper implements ExceptionMapper<ServiceException> {

    @Override
    public Response toResponse(ServiceException e) {
        // TODO: Implementiere die Fehler-Response
        //
        // Erwartetes JSON Format:
        // {
        //   "status": 404,
        //   "error": "Customer with id 999 not found"
        // }
        //
        // Tipps:
        //   - Response.status(e.getStatusCode())
        //   - .entity(Map.of("status", ..., "error", ...))
        //   - .type(MediaType.APPLICATION_JSON)
        //   - .build()

        throw new UnsupportedOperationException("TODO: Implementiere ServiceExceptionMapper");
    }
}
