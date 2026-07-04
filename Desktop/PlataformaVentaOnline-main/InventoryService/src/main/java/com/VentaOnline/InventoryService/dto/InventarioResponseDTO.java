package com.VentaOnline.InventoryService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta con datos de un movimiento de inventario")
public class InventarioResponseDTO {
    @Schema(description = "ID del movimiento", example = "1")
    private Long id;

    @Schema(description = "ID del producto", example = "1")
    private Long productoId;

    @Schema(description = "Nombre del producto", example = "Smartphone X")
    private String nombreProducto;

    @Schema(description = "Tipo de movimiento", example = "ENTRADA")
    private String tipo;

    @Schema(description = "Cantidad de unidades", example = "10")
    private Integer cantidad;

    @Schema(description = "Observación del movimiento", example = "Reabastecimiento semanal")
    private String observacion;

    @Schema(description = "Fecha y hora de creación del registro")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de la última actualización")
    private LocalDateTime updatedAt;
}
