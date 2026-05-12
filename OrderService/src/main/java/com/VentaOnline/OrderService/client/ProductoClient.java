package com.VentaOnline.OrderService.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.VentaOnline.OrderService.dto.ProductoResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProductoClient {
    private final WebClient productsWebClient;

    public ProductoClient(WebClient productsWebClient) {
        this.productsWebClient = productsWebClient;
    }

    public ProductoResponse getProductoById(Long productoId) {
        log.info("Obteniendo producto con ID: {}", productoId);
        try {
            return productsWebClient.get()
                    .uri("/api/productos/{productoId}", productoId)
                    .retrieve()
                    .bodyToMono(ProductoResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error al obtener producto con ID {}: {}", productoId, ex.getMessage());
            switch (ex.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("Producto no encontrado con ID: " + productoId);
                default -> throw new RuntimeException("Error al obtener producto con ID: " + productoId, ex);
            }
        }
    }
}
