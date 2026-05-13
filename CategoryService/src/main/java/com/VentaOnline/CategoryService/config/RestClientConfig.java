package com.VentaOnline.CategoryService.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Value("${servicios.usuarios.url}")
    private String usersServiceUrl;

    @Bean
    public RestClient usersRestClient() {
        return RestClient.builder()
                .baseUrl(usersServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
