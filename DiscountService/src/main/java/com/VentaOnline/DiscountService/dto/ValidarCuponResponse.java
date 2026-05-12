package com.VentaOnline.DiscountService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidarCuponResponse {
    private boolean valido;
    private String mensaje;
    private CuponResponseDTO cupon;
}
