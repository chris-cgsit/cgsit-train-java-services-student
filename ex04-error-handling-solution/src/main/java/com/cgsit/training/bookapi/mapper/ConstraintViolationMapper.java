package com.cgsit.training.bookapi.mapper;

import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.List;
import java.util.Map;

@Provider
public class ConstraintViolationMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException e) {
        List<Map<String, String>> violations = e.getConstraintViolations().stream()
                .map(v -> Map.of(
                    "field", extractFieldName(v.getPropertyPath()),
                    "message", v.getMessage()
                ))
                .toList();

        Map<String, Object> body = Map.of(
            "status", 400,
            "title", "Validation Error",
            "violations", violations
        );

        return Response.status(Response.Status.BAD_REQUEST)
                       .entity(body)
                       .type(MediaType.APPLICATION_JSON)
                       .build();
    }

    private String extractFieldName(Path propertyPath) {
        String path = propertyPath.toString();
        int lastDot = path.lastIndexOf('.');
        return lastDot >= 0 ? path.substring(lastDot + 1) : path;
    }
}
