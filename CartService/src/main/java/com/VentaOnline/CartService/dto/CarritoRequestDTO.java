package com.VentaOnline.CartService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Solicitud para crear un nuevo carrito de compras")
public class CarritoRequestDTO {
    @Schema(description = "ID del usuario propietario del carrito", example = "1")
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;
}
