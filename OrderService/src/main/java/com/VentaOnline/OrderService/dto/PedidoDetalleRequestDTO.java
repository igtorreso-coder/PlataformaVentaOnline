package com.VentaOnline.OrderService.dto;

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
public class PedidoDetalleRequestDTO {
    // Identificador del producto que se desea comprar (debe existir en el catalogo)
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    /*
     * Cantidad de unidades del producto.
     * @Min(1) asegura que la cantidad sea positiva (>0), ya que no tiene
     * sentido un pedido con cantidad cero o negativa.
     */
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
}
