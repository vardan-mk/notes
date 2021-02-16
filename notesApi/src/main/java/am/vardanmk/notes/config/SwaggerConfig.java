package am.vardanmk.notes.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "Notes API",
                version = "v1",
                description = "API for Notes app"))
//                contact = @Contact(name = "Vardan", email = "vardan2092@@gmail.com")))
@SecurityScheme(
        name = "jwtToken",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer")

@Configuration
public class SwaggerConfig {
}
