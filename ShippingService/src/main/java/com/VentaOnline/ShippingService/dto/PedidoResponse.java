package com.VentaOnline.ShippingService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de pedido para comunicación entre servicios")
public class PedidoResponse {
    @Schema(description = "ID del pedido", example = "1")
    private Long id;

    @Schema(description = "Estado del pedido", example = "CONFIRMADO")
    private String estado;
}
