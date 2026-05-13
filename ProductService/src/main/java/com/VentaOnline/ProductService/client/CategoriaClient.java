package com.VentaOnline.ProductService.client;

import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;
import com.VentaOnline.ProductService.dto.CategoriaResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CategoriaClient {
    @Autowired
    private RestClient categoriesRestClient;

    public CategoriaResponse getCategoriaById(Long categoriaId) {
        log.info("Obteniendo categoria con ID: {}", categoriaId);
        try {
            return categoriesRestClient.get()
                    .uri("/api/categorias/{categoriaId}", categoriaId)
                    .retrieve()
                    .body(CategoriaResponse.class);
        } catch (HttpClientErrorException ex) {
            log.error("Error al obtener categoria con ID {}: {}", categoriaId, ex.getMessage());
            switch (ex.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("Categoria no encontrada con ID: " + categoriaId);
                default -> throw new RuntimeException("Error al obtener categoria con ID: " + categoriaId, ex);
            }
        }
    }

    public List<CategoriaResponse> getCategorias() {
        log.info("Obteniendo todas las categorias");
        try {
            return categoriesRestClient.get()
                    .uri("/api/categorias")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<CategoriaResponse>>() {});
        } catch (HttpClientErrorException ex) {
            log.error("Error al obtener categorias: {}", ex.getMessage());
            throw new RuntimeException("Error al obtener categorias del microservicio", ex);
        }
    }
}
