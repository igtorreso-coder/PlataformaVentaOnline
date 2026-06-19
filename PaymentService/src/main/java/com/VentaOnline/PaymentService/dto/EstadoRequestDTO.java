package com.VentaOnline.PaymentService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud para actualizar el estado de un pago")
public class EstadoRequestDTO {
    @Schema(description = "Nuevo estado del pago", example = "APROBADO", allowableValues = {"PENDIENTE", "COMPLETADO", "RECHAZADO", "REEMBOLSADO"})
    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}
