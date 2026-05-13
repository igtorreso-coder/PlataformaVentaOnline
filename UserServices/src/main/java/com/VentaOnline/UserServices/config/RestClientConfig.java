package com.VentaOnline.UserServices.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Value("${servicios.categorias.url}")
    private String categoriesServiceUrl;

    @Bean
    public RestClient categoriesRestClient() {
        return RestClient.builder()
                .baseUrl(categoriesServiceUrl)
                .defaultHeader("Content-Type", "application/json")
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
