package com.VentaOnline.CartService.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarritoItemRequestDTO {
    // Producto que se va agregar al carrito debe existir en el catalogo
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    //@Min(1) garantiza que solo se agreguen cantidades positivas
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
}
