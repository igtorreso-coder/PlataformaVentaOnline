package com.VentaOnline.ShippingService.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpClientErrorException;
import com.VentaOnline.ShippingService.dto.PedidoResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PedidoClient {

    @Autowired
    private RestClient ordersRestClient;

    public PedidoResponse getPedidoById(Long pedidoId) {
        log.info("Obteniendo pedido con ID: {}", pedidoId);
        try {
            return ordersRestClient.get()
                    .uri("/api/pedidos/{pedidoId}", pedidoId)
                    .retrieve()
                    .body(PedidoResponse.class);
        } catch (HttpClientErrorException ex) {
            log.error("Error al obtener pedido con ID {}: {}", pedidoId, ex.getMessage());
            switch (ex.getStatusCode().value()) {
                case 404 -> throw new RuntimeException("Pedido no encontrado con ID: " + pedidoId);
                default -> throw new RuntimeException("Error al obtener pedido con ID: " + pedidoId, ex);
            }
        }
    }
}
