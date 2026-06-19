package com.VentaOnline.OrderService.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;
import com.VentaOnline.OrderService.dto.UsuarioResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UsuarioClient {
    @Autowired
    private RestClient usersRestClient;

    public UsuarioResponse obtenerUsuarioPorId(Long usuarioId) {
        log.info("Obteniendo usuario con ID: {}", usuarioId);
        try {
            return usersRestClient.get()
                    .uri("/api/usuarios/{usuarioId}", usuarioId)
                    .retrieve()
                    .body(UsuarioResponse.class);
        } catch (HttpClientErrorException ex) {
            log.error("Error al obtener usuario con ID {}: {}", usuarioId, ex.getMessage());
            switch (ex.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("Usuario no encontrado con ID: " + usuarioId);
                default -> throw new RuntimeException("Error al obtener usuario con ID: " + usuarioId, ex);
            }
        }
    }
}
