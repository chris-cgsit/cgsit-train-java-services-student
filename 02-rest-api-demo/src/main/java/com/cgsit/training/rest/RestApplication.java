package com.cgsit.training.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;
import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Contact;
import org.eclipse.microprofile.openapi.annotations.info.Info;

@OpenAPIDefinition(
    info = @Info(
        title = "Product API",
        version = "1.0.0",
        description = "REST API Demo — Java Services Training (CGS IT)",
        contact = @Contact(name = "CGS IT Solutions", url = "https://www.cgs.at")
    )
)
@ApplicationPath("/api")
public class RestApplication extends Application {
}
