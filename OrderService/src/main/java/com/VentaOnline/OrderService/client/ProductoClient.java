package com.VentaOnline.OrderService.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;
import com.VentaOnline.OrderService.dto.ProductoResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProductoClient {
    @Autowired
    private RestClient productsRestClient;

    public ProductoResponse getProductoById(Long productoId) {
        log.info("Obteniendo producto con ID: {}", productoId);
        try {
            return productsRestClient.get()
                    .uri("/api/productos/{productoId}", productoId)
                    .retrieve()
                    .body(ProductoResponse.class);
        } catch (HttpClientErrorException ex) {
            log.error("Error al obtener producto con ID {}: {}", productoId, ex.getMessage());
            switch (ex.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("Producto no encontrado con ID: " + productoId);
                default -> throw new RuntimeException("Error al obtener producto con ID: " + productoId, ex);
            }
        }
    }
}
