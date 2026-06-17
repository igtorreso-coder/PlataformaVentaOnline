package com.VentaOnline.DiscountService.dto;

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
public class CuponRequestDTO {

    @NotBlank(message = "El código es obligatorio")
    @Size(max = 50, message = "El código debe tener máximo 50 caracteres")
    private String codigo;

    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 20, message = "El tipo debe tener máximo 20 caracteres")
    private String tipo;

    @NotNull(message = "El valor es obligatorio")
    @DecimalMin(value = "0.01", message = "El valor debe ser mayor a 0")
    private BigDecimal valor;

    @DecimalMin(value = "0", message = "El monto mínimo no puede ser negativo")
    private BigDecimal montoMinimo;

    @NotNull(message = "Los usos máximos son obligatorios")
    @Min(value = 1, message = "Los usos máximos deben ser mayor a 0")
    private Integer usosMaximos;

    private LocalDateTime fechaExpiracion;

    private Boolean activo;
}
