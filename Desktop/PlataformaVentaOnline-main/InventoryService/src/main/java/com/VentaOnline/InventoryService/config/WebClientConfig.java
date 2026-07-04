package com.VentaOnline.InventoryService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${servicios.productos.url}")
    private String productsServiceUrl;

    @Bean
    public WebClient productsWebClient() {
        return WebClient.builder()
                .baseUrl(productsServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
