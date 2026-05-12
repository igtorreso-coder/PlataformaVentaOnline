package com.VentaOnline.OrderService.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.VentaOnline.OrderService.dto.UsuarioResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UsuarioClient {
    private final WebClient usersWebClient;

    public UsuarioClient(WebClient usersWebClient) {
        this.usersWebClient = usersWebClient;
    }

    public UsuarioResponse getUsuarioById(Long usuarioId) {
        log.info("Obteniendo usuario con ID: {}", usuarioId);
        try {
            return usersWebClient.get()
                    .uri("/api/usuarios/{usuarioId}", usuarioId)
                    .retrieve()
                    .bodyToMono(UsuarioResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error al obtener usuario con ID {}: {}", usuarioId, ex.getMessage());
            switch (ex.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("Usuario no encontrado con ID: " + usuarioId);
                default -> throw new RuntimeException("Error al obtener usuario con ID: " + usuarioId, ex);
            }
        }
    }
}
