package com.VentaOnline.CartService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${servicios.usuarios.url}")
    private String usersServiceUrl;

    @Value("${servicios.productos.url}")
    private String productsServiceUrl;

    @Bean
    public RestClient usuariosRestClient() {
        return RestClient.builder()
                .baseUrl(usersServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Bean
    public RestClient productosRestClient() {
        return RestClient.builder()
                .baseUrl(productsServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
