package com.VentaOnline.CartService.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import com.VentaOnline.CartService.dto.ProductoResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class ProductoClient {

    @Autowired
    private RestClient productosRestClient;

    public ProductoResponse obtenerProducto(Long id) {
        log.info("Obteniendo producto con ID: {}", id);
        try {
            return productosRestClient.get()
                    .uri("/api/productos/{id}", id)
                    .retrieve()
                    .body(ProductoResponse.class);
        } catch (HttpClientErrorException ex) {
            log.error("Error al obtener producto con ID {}: {}", id, ex.getMessage());
            if (ex.getStatusCode().value() == 404) {
                throw new RuntimeException("Producto no encontrado con ID: " + id);
            }
            throw new RuntimeException("Error al obtener producto con ID: " + id, ex);
        }
    }
}
