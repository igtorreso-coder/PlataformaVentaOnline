package com.VentaOnline.DiscountService.dto;

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
@Schema(description = "Respuesta con datos de un cupón de descuento")
public class CuponResponseDTO {

    @Schema(description = "ID del cupón", example = "1")
    private Long id;

    @Schema(description = "Código único del cupón", example = "BIENVENIDO")
    private String codigo;

    @Schema(description = "Tipo de descuento", example = "PORCENTAJE")
    private String tipo;

    @Schema(description = "Valor del descuento", example = "10.00")
    private BigDecimal valor;

    @Schema(description = "Monto mínimo de compra", example = "1000.00")
    private BigDecimal montoMinimo;

    @Schema(description = "Usos máximos permitidos", example = "100")
    private Integer usosMaximos;

    @Schema(description = "Usos realizados actualmente", example = "5")
    private Integer usosActuales;

    @Schema(description = "Fecha de expiración del cupón")
    private LocalDateTime fechaExpiracion;

    @Schema(description = "Indica si el cupón está activo", example = "true")
    private Boolean activo;

    @Schema(description = "Fecha y hora de creación del registro")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de la última actualización")
    private LocalDateTime updatedAt;
}
