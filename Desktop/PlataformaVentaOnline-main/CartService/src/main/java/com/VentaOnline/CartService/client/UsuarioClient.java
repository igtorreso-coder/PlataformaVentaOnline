package com.VentaOnline.CartService.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import com.VentaOnline.CartService.dto.UsuarioResponse;
import jakarta.annotation.PostConstruct;

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
        return usuariosRestClient.get()
                .uri("/api/usuarios/{id}", id)
                .retrieve()
                .body(UsuarioResponse.class);
    }
}
