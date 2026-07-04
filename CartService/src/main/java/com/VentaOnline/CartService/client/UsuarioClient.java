package com.VentaOnline.CartService.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.VentaOnline.CartService.dto.UsuarioResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UsuarioClient {

    @Autowired
    private WebClient usuariosWebClient;

    public UsuarioResponse obtenerUsuario(Long id) {
        log.info("Obteniendo usuario con ID: {}", id);
        try {
            return usuariosWebClient.get()
                    .uri("/api/usuarios/{id}", id)
                    .retrieve()
                    .bodyToMono(UsuarioResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error al obtener usuario con ID {}: {}", id, ex.getMessage());
            if (ex.getStatusCode().value() == 404) {
                throw new RuntimeException("Usuario no encontrado con ID: " + id);
            }
            throw new RuntimeException("Error al obtener usuario con ID: " + id, ex);
        }
    }
}
