package com.VentaOnline.NotificationService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${servicios.usuarios.url}")
    private String usersServiceUrl;

    @Bean
    public WebClient usersWebClient() {
        return WebClient.builder()
                .baseUrl(usersServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
