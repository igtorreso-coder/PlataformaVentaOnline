package com.VentaOnline.AuthService.client;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserServiceClient {

    @Autowired
    private WebClient usersWebClient;

    public void sincronizarUsuario(Long id, String nombre, String correo) {
        log.info("Sincronizando usuario ID: {} con UserServices", id);
        try {
            usersWebClient.post()
                    .uri("/api/usuarios")
                    .bodyValue(Map.of(
                            "nombre", nombre,
                            "correo", correo
                    ))
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();
        } catch (Exception e) {
            log.warn("No se pudo sincronizar usuario con UserServices: {}", e.getMessage());
        }
    }
}
