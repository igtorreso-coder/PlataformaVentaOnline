package com.VentaOnline.ShippingService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstadoRequestDTO {

    // Nuevo estado del envio "EN_PREPARACION", "DESPACHADO", "EN_TRANSITO", "ENTREGADO")
    @NotBlank(message = "El estado es obligatorio")
    private String estado;
}
