package com.VentaOnline.CartService.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.VentaOnline.CartService.dto.ProductoResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProductoClient {

    @Autowired
    private WebClient productosWebClient;

    public ProductoResponse obtenerProducto(Long id) {
        log.info("Obteniendo producto con ID: {}", id);
        try {
            return productosWebClient.get()
                    .uri("/api/productos/{id}", id)
                    .retrieve()
                    .bodyToMono(ProductoResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error al obtener producto con ID {}: {}", id, ex.getMessage());
            if (ex.getStatusCode().value() == 404) {
                throw new RuntimeException("Producto no encontrado con ID: " + id);
            }
            throw new RuntimeException("Error al obtener producto con ID: " + id, ex);
        }
    }
}
