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
public class CarritoItemUpdateRequestDTO {
    /**
     * @Min(1) evita valores cero o negativos que no tienen sentido en un carrito.
     * Si se desea eliminar el item, debe usarse el endpoint de eliminacion.
     */
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
}
