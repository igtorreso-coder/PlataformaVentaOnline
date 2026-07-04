package com.VentaOnline.DiscountService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Solicitud para crear o actualizar un cupón de descuento")
public class CuponRequestDTO {

    @Schema(description = "Código único del cupón", example = "BIENVENIDO")
    @NotBlank(message = "El código es obligatorio")
    @Size(max = 50, message = "El código debe tener máximo 50 caracteres")
    private String codigo;

    @Schema(description = "Tipo de descuento (PORCENTAJE, MONTO_FIJO)", example = "PORCENTAJE")
    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 20, message = "El tipo debe tener máximo 20 caracteres")
    private String tipo;

    @Schema(description = "Valor del descuento (porcentaje o monto fijo)", example = "10.00")
    @NotNull(message = "El valor es obligatorio")
    @DecimalMin(value = "0.01", message = "El valor debe ser mayor a 0")
    private BigDecimal valor;

    @Schema(description = "Monto mínimo de compra para aplicar el cupón", example = "1000.00")
    @DecimalMin(value = "0", message = "El monto mínimo no puede ser negativo")
    private BigDecimal montoMinimo;

    @Schema(description = "Número máximo de usos del cupón", example = "100")
    @NotNull(message = "Los usos máximos son obligatorios")
    @Min(value = 1, message = "Los usos máximos deben ser mayor a 0")
    private Integer usosMaximos;

    @Schema(description = "Fecha de expiración del cupón")
    private LocalDateTime fechaExpiracion;

    @Schema(description = "Indica si el cupón está activo", example = "true")
    private Boolean activo;
}
