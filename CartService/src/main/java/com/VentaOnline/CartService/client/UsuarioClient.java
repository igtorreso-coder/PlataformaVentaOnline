package com.VentaOnline.CartService.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import com.VentaOnline.CartService.dto.UsuarioResponse;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UsuarioClient {

    @Autowired
    private RestClient restClient;

    @Value("${servicios.usuarios.url}")
    private String usuariosUrl;

    private RestClient usuariosRestClient;

    @PostConstruct
    public void init() {
        this.usuariosRestClient = this.restClient.mutate().baseUrl(this.usuariosUrl).build();
    }

    public UsuarioResponse obtenerUsuario(Long id) {
        log.info("Obteniendo usuario con ID: {}", id);
        try {
            return usuariosRestClient.get()
                    .uri("/api/usuarios/{id}", id)
                    .retrieve()
                    .body(UsuarioResponse.class);
        } catch (HttpClientErrorException ex) {
            log.error("Error al obtener usuario con ID {}: {}", id, ex.getMessage());
            if (ex.getStatusCode().value() == 404) {
                throw new RuntimeException("Usuario no encontrado con ID: " + id);
            }
            throw new RuntimeException("Error al obtener usuario con ID: " + id, ex);
        }
    }
}
