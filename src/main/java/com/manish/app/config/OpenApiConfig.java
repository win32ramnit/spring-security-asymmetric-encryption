package com.manish.app.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
    info = @Info(
        contact = @Contact(
            name = "Spring security JWT asymmetric encryption demo",
            email = "contact@mail.com",
            url = "https://contact.com"
        ),
        description = "OpenAPi for Spring Security project",
        title = "OpenAPI Specification",
        version = "1.0.0",
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0.html"
        ),
        termsOfService = "https://example.com/terms"
    ),
    servers = {
        @Server(
            description = "Local development server",
            url = "http://localhost:8080"
        ),
        @Server(
            description = "Production server",
            url = "https://api.example.com"
        )
    },
    security = {
        @SecurityRequirement(
            name = "BearerAuth"
        )
    }
)
@SecurityScheme(
    name = "BearerAuth",
    description = "JWT Bearer token authentication",
    scheme = "bearer",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
