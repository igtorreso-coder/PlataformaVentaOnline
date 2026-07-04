package com.VentaOnline.PaymentService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
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

    @Schema(description = "Monto total del pedido", example = "25700.00")
    private BigDecimal total;

    @Schema(description = "Estado del pedido", example = "CONFIRMADO")
    private String estado;
}
