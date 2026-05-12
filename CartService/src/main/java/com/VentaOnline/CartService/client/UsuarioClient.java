package com.VentaOnline.CartService.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import com.VentaOnline.CartService.dto.UsuarioResponse;
import reactor.core.publisher.Mono;

@Component
public class UsuarioClient {

    private final WebClient webClient;

    public UsuarioClient(WebClient webClient, @Value("${microservicios.usuarios.url}") String usuariosUrl) {
        this.webClient = webClient.mutate().baseUrl(usuariosUrl).build();
    }

    public Mono<UsuarioResponse> obtenerUsuario(Long id) {
        return webClient.get()
                .uri("/api/usuarios/{id}", id)
                .retrieve()
                .bodyToMono(UsuarioResponse.class);
    }
}
