package com.VentaOnline.CartService.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.VentaOnline.CartService.dto.ProductoResponse;
import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;

@Component
public class ProductoClient {

    @Autowired
    private WebClient webClient;

    @Value("${servicios.productos.url}")
    private String productosUrl;

    private WebClient productosWebClient;

    @PostConstruct
    public void init() {
        this.productosWebClient = this.webClient.mutate().baseUrl(this.productosUrl).build();
    }

    public Mono<ProductoResponse> obtenerProducto(Long id) {
        return productosWebClient.get()
                .uri("/api/productos/{id}", id)
                .retrieve()
                .bodyToMono(ProductoResponse.class);
    }
}
