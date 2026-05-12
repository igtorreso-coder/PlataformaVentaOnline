package com.VentaOnline.NotificationService.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserServiceClient {

    private final WebClient usersWebClient;

    public UserServiceClient(WebClient usersWebClient) {
        this.usersWebClient = usersWebClient;
    }

    public void obtenerUsuario(Long usuarioId) {
        log.info("Verificando usuario ID: {}", usuarioId);
        try {
            usersWebClient.get()
                    .uri("/api/usuarios/{usuarioId}", usuarioId)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error al verificar usuario ID {}: {}", usuarioId, ex.getMessage());
            switch (ex.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("Usuario no encontrado con ID: " + usuarioId);
                default -> throw new RuntimeException("Error al verificar usuario con ID: " + usuarioId, ex);
            }
        }
    }
}
