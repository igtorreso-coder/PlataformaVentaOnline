package com.VentaOnline.CartService.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.VentaOnline.CartService.dto.UsuarioResponse;
import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;

@Component
public class UsuarioClient {

    @Autowired
    private WebClient webClient;

    @Value("${servicios.usuarios.url}")
    private String usuariosUrl;

    private WebClient usuariosWebClient;

    @PostConstruct
    public void init() {
        this.usuariosWebClient = this.webClient.mutate().baseUrl(this.usuariosUrl).build();
    }

    public Mono<UsuarioResponse> obtenerUsuario(Long id) {
        return usuariosWebClient.get()
                .uri("/api/usuarios/{id}", id)
                .retrieve()
                .bodyToMono(UsuarioResponse.class);
    }
}
