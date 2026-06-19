package com.VentaOnline.ShippingService.config;

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
                        .title("Shipping Service API")
                        .description("Microservicio de Envíos - Gestión de envíos, actualización de estados y seguimiento por pedido")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Plataforma Venta Online")
                                .email("soporte@ventaonline.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
