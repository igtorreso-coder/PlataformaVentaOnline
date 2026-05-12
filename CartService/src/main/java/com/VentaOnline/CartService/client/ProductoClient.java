package com.VentaOnline.CartService.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.VentaOnline.CartService.dto.ProductoResponse;
import reactor.core.publisher.Mono;

@Component
public class ProductoClient {

    private final WebClient webClient;

    public ProductoClient(WebClient webClient, @Value("${microservicios.productos.url}") String productosUrl) {
        this.webClient = webClient.mutate().baseUrl(productosUrl).build();
    }

    public Mono<ProductoResponse> obtenerProducto(Long id) {
        return webClient.get()
                .uri("/api/productos/{id}", id)
                .retrieve()
                .bodyToMono(ProductoResponse.class);
    }
}
