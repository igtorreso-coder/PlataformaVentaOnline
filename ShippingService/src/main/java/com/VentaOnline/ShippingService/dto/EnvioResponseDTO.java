package com.VentaOnline.ShippingService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta con datos de un envío")
public class EnvioResponseDTO {

    @Schema(description = "ID del envío", example = "1")
    private Long id;

    @Schema(description = "ID del pedido asociado", example = "1")
    private Long pedidoId;

    @Schema(description = "Dirección de entrega", example = "Av. Siempre Viva 123")
    private String direccion;

    @Schema(description = "Ciudad de entrega", example = "Buenos Aires")
    private String ciudad;

    @Schema(description = "Estado del envío", example = "EN_TRANSITO")
    private String estado;

    @Schema(description = "Código de seguimiento", example = "TRK-ABC123XYZ")
    private String codigoSeguimiento;

    @Schema(description = "Fecha y hora del envío")
    private LocalDateTime fechaEnvio;

    @Schema(description = "Fecha y hora estimada de entrega")
    private LocalDateTime fechaEntrega;

    @Schema(description = "Fecha y hora de creación del registro")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de la última actualización")
    private LocalDateTime updatedAt;
}
