package com.VentaOnline.PaymentService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta con datos de un pago")
public class PagoResponseDTO {

    @Schema(description = "ID del pago", example = "1")
    private Long id;

    @Schema(description = "ID del pedido asociado", example = "1")
    private Long pedidoId;

    @Schema(description = "Monto del pago", example = "25700.00")
    private BigDecimal monto;

    @Schema(description = "Método de pago", example = "TARJETA")
    private String metodoPago;

    @Schema(description = "Estado del pago (PENDIENTE, COMPLETADO, RECHAZADO, REEMBOLSADO)", example = "COMPLETADO")
    private String estado;

    @Schema(description = "Referencia de la transacción", example = "REF-ABC123")
    private String referencia;

    @Schema(description = "Fecha y hora del pago")
    private LocalDateTime fechaPago;

    @Schema(description = "Fecha y hora de creación del registro")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de la última actualización")
    private LocalDateTime updatedAt;
}
