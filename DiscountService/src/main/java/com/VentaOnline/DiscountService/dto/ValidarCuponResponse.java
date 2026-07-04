package com.VentaOnline.DiscountService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta de validación de un cupón de descuento")
public class ValidarCuponResponse {
    @Schema(description = "Indica si el cupón es válido", example = "true")
    private boolean valido;

    @Schema(description = "Mensaje descriptivo del resultado", example = "Cupón válido")
    private String mensaje;

    @Schema(description = "Datos del cupón validado")
    private CuponResponseDTO cupon;
}
