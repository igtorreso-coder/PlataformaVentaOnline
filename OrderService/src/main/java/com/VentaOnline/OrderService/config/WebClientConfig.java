package com.VentaOnline.OrderService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${services.users.url}")
    private String usersServiceUrl;

    @Value("${services.products.url}")
    private String productsServiceUrl;

    @Bean
    public WebClient usersWebClient() {
        return WebClient.builder()
                .baseUrl(usersServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }

    @Bean
    public WebClient productsWebClient() {
        return WebClient.builder()
                .baseUrl(productsServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
