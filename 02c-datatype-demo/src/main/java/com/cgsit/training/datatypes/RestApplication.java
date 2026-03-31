package com.cgsit.training.datatypes;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "Data Type Mapping Demo",
        version = "1.0.0",
        description = "Zeigt wie Java-Typen auf JSON und OpenAPI gemappt werden"
    )
)
@ApplicationPath("/api")
public class RestApplication extends Application {
}
