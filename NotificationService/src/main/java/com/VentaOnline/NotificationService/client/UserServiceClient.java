package com.VentaOnline.NotificationService.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserServiceClient {

    @Autowired
    private RestClient usersRestClient;

    public void obtenerUsuario(Long usuarioId) {
        log.info("Verificando usuario ID: {}", usuarioId);
        try {
            usersRestClient.get()
                    .uri("/api/usuarios/{usuarioId}", usuarioId)
                    .retrieve()
                    .body(Void.class);
        } catch (HttpClientErrorException ex) {
            log.error("Error al verificar usuario ID {}: {}", usuarioId, ex.getMessage());
            switch (ex.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("Usuario no encontrado con ID: " + usuarioId);
                default -> throw new RuntimeException("Error al verificar usuario con ID: " + usuarioId, ex);
            }
        }
    }
}
