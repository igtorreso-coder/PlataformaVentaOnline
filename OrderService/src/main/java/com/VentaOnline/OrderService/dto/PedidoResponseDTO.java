package com.VentaOnline.OrderService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta con datos de un pedido")
public class PedidoResponseDTO {
    @Schema(description = "ID del pedido", example = "1")
    private Long id;

    @Schema(description = "ID del usuario", example = "1")
    private Long usuarioId;

    @Schema(description = "Nombre del usuario", example = "Ignacio Torres")
    private String nombreUsuario;

    @Schema(description = "Fecha del pedido")
    private LocalDateTime fecha;

    @Schema(description = "Monto total del pedido", example = "25700.00")
    private BigDecimal total;

    @Schema(description = "Estado del pedido", example = "CONFIRMADO")
    private String estado;

    @Schema(description = "Detalle de los productos del pedido")
    private List<PedidoDetalleResponseDTO> detalles;

    @Schema(description = "Fecha y hora de creación del registro")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de la última actualización")
    private LocalDateTime updatedAt;
}
