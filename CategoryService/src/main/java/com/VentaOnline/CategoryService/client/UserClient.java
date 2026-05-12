package com.VentaOnline.CategoryService.client;

import java.util.List;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.VentaOnline.CategoryService.dto.UsuarioDTO;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserClient {
    @Autowired
    private WebClient usersWebClient;

    public List<UsuarioDTO> getUsers() {
        log.info("Obteniendo usuarios desde users-service");
        try {
            return usersWebClient.get()
                    .uri("/api/usuarios")
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<UsuarioDTO>>() {})
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error al obtener usuarios: {}", ex.getMessage());
            throw new RuntimeException("Error al obtener usuarios del microservicio", ex);
        }
    }
}
