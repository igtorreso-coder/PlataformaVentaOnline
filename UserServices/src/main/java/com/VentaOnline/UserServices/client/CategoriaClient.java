package com.VentaOnline.UserServices.client;

import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;
import com.VentaOnline.UserServices.dto.CategoriaDTO;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CategoriaClient {
    @Autowired
    private RestClient categoriesRestClient;

    public List<CategoriaDTO> getCategorias() {
        log.info("Obteniendo categorias desde categories-service");
        try {
            return categoriesRestClient.get()
                    .uri("/api/categorias")
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<CategoriaDTO>>() {});
        } catch (HttpClientErrorException ex) {
            log.error("Error al obtener categorias: {}", ex.getMessage());
            throw new RuntimeException("Error al obtener categorias del microservicio", ex);
        }
    }
}
