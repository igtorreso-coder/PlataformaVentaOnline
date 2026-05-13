package com.VentaOnline.AuthService.client;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserServiceClient {

    @Autowired
    private RestClient usersRestClient;

    public void sincronizarUsuario(Long id, String nombre, String correo) {
        log.info("Sincronizando usuario ID: {} con UserServices", id);
        try {
            usersRestClient.post()
                    .uri("/api/usuarios")
                    .body(Map.of(
                            "nombre", nombre,
                            "correo", correo
                    ))
                    .retrieve()
                    .body(Void.class);
        } catch (Exception e) {
            log.warn("No se pudo sincronizar usuario con UserServices: {}", e.getMessage());
        }
    }
}
