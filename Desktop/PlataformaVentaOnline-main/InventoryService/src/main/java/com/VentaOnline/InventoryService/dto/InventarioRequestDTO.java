package com.VentaOnline.InventoryService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Solicitud para registrar un movimiento de inventario")
public class InventarioRequestDTO {
    @Schema(description = "ID del producto afectado", example = "1")
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    @Schema(description = "Tipo de movimiento: ENTRADA (aumenta stock) o SALIDA (disminuye stock)", example = "ENTRADA", allowableValues = {"ENTRADA", "SALIDA"})
    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Size(max = 20, message = "El tipo debe tener máximo 20 caracteres")
    private String tipo;

    @Schema(description = "Cantidad de unidades afectadas (siempre positiva)", example = "10")
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
    
    @Schema(description = "Observación opcional del movimiento", example = "Reabastecimiento semanal")
    private String observacion;
}
