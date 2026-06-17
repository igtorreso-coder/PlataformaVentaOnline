package com.VentaOnline.ShippingService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${servicios.pedidos.url}")
    private String ordersServiceUrl;

    @Bean
    public RestClient ordersRestClient() {
        return RestClient.builder()
                .baseUrl(ordersServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
