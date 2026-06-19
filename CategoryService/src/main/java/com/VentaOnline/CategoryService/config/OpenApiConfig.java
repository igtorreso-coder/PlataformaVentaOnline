package com.VentaOnline.CategoryService.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Category Service API")
                        .description("Microservicio de Categorías - CRUD de categorías e integración con microservicio de usuarios")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Plataforma Venta Online")
                                .email("soporte@ventaonline.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
