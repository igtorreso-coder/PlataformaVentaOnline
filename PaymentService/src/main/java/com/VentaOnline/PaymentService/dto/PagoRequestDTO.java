package com.VentaOnline.PaymentService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
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
@Schema(description = "Solicitud para crear un nuevo pago")
public class PagoRequestDTO {

    @Schema(description = "ID del pedido asociado", example = "1")
    @NotNull(message = "El ID del pedido es obligatorio")
    private Long pedidoId;

    @Schema(description = "Monto total del pago", example = "25700.00")
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    @Schema(description = "Método de pago (TARJETA, TRANSFERENCIA, etc.)", example = "TARJETA")
    @NotBlank(message = "El método de pago es obligatorio")
    @Size(max = 20, message = "El método de pago debe tener máximo 20 caracteres")
    private String metodoPago;
}
