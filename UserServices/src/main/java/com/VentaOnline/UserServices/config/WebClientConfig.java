package com.VentaOnline.UserServices.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${servicios.categorias.url}")
    private String categoriesServiceUrl;

    @Bean
    public WebClient categoriesWebClient() {
        return WebClient.builder()
                .baseUrl(categoriesServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
