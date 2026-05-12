package com.VentaOnline.InventoryService.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioRequestDTO {
    // Producto al que se le registrara el movimiento de stock
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    /*
     * Tipo de movimiento: "ENTRADA" (aumenta stock) o "SALIDA" (disminuye stock).
     * Las salidas se usan cuando se confirma un pedido.
     */
    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Size(max = 20, message = "El tipo debe tener máximo 20 caracteres")
    private String tipo;

    /*
     * Cantidad de unidades afectadas en el movimiento.
     * Siempre debe ser positiva; el signo (sumar o restar stock)
     * lo determina el campo "tipo".
     */
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
    
    private String observacion;
}
