package com.VentaOnline.ShippingService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${servicios.pedidos.url}")
    private String ordersServiceUrl;

    @Bean
    public WebClient ordersWebClient() {
        return WebClient.builder()
                .baseUrl(ordersServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
