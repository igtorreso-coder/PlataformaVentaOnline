package com.VentaOnline.InventoryService.client;

import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;
import com.VentaOnline.InventoryService.dto.ProductoResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ProductoClient {
    @Autowired
    private RestClient productsRestClient;

    public ProductoResponse obtenerProductoPorId(Long productoId) {
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

    public List<ProductoResponse> obtenerProductos() {
        log.info("Obteniendo todos los productos");
        try {
            return productsRestClient.get()
                    .uri("/api/productos")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<ProductoResponse>>() {});
        } catch (HttpClientErrorException ex) {
            log.error("Error al obtener productos: {}", ex.getMessage());
            throw new RuntimeException("Error al obtener productos del microservicio", ex);
        }
    }
}
