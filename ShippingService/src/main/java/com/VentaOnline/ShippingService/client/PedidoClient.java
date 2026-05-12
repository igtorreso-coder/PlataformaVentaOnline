package com.VentaOnline.ShippingService.client;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import com.VentaOnline.ShippingService.dto.PedidoResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PedidoClient {

    private final WebClient ordersWebClient;

    public PedidoClient(WebClient ordersWebClient) {
        this.ordersWebClient = ordersWebClient;
    }

    public PedidoResponse getPedidoById(Long pedidoId) {
        log.info("Obteniendo pedido con ID: {}", pedidoId);
        try {
            return ordersWebClient.get()
                    .uri("/api/pedidos/{pedidoId}", pedidoId)
                    .retrieve()
                    .bodyToMono(PedidoResponse.class)
                    .block();
        } catch (WebClientResponseException ex) {
            log.error("Error al obtener pedido con ID {}: {}", pedidoId, ex.getMessage());
            switch (ex.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("Pedido no encontrado con ID: " + pedidoId);
                default -> throw new RuntimeException("Error al obtener pedido con ID: " + pedidoId, ex);
            }
        }
    }
}
