package com.VentaOnline.UserServices.client;

import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.VentaOnline.UserServices.dto.CategoriaDTO;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CategoriaClient {
    @Autowired
    private WebClient categoriesWebClient;

    public List<CategoriaDTO> getCategorias() {
        log.info("Obteniendo categorías desde categories-service");
        try {
            return categoriesWebClient.get()
                    .uri("/api/categorias")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<CategoriaDTO>>() {})
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error al obtener categorías: {}", ex.getMessage());
            throw new RuntimeException("Error al obtener categorías del microservicio", ex);
        }
    }
}
