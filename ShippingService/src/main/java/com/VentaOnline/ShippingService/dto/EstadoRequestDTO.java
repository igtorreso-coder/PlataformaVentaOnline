package com.VentaOnline.ShippingService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud para actualizar el estado de un envío")
public class EstadoRequestDTO {

    @Schema(description = "Nuevo estado del envío", example = "EN_TRANSITO", allowableValues = {"EN_PREPARACION", "DESPACHADO", "EN_TRANSITO", "ENTREGADO"})
    @NotBlank(message = "El estado es obligatorio. Debe ser uno de los siguientes: EN_PREPARACION, DESPACHADO, EN_TRANSITO, ENTREGADO")
    private String estado;
}
