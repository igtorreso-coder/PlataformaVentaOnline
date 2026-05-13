package com.VentaOnline.InventoryService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Value("${servicios.productos.url}")
    private String productsServiceUrl;

    @Bean
    public RestClient productsRestClient() {
        return RestClient.builder()
                .baseUrl(productsServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
