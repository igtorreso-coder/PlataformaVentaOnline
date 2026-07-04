package com.VentaOnline.OrderService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud para actualizar el estado de un pedido")
public class EstadoRequestDTO {

    @Schema(description = "Nuevo estado del pedido", example = "ENVIADO", allowableValues = {"PENDIENTE", "CONFIRMADO", "ENVIADO", "ENTREGADO", "CANCELADO"})
    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}
