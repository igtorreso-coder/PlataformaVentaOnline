package com.VentaOnline.InventoryService.client;

import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.VentaOnline.InventoryService.dto.ProductoResponse;
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

    public List<ProductoResponse> getProductos() {
        log.info("Obteniendo todos los productos");
        try {
            return productsWebClient.get()
                    .uri("/api/productos")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<ProductoResponse>>() {})
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error al obtener productos: {}", ex.getMessage());
            throw new RuntimeException("Error al obtener productos del microservicio", ex);
        }
    }
}
