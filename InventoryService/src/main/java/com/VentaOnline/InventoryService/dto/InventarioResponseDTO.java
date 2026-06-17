package com.VentaOnline.InventoryService.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventarioResponseDTO {
    private Long id;
    private Long productoId;
    private String nombreProducto;
    private String tipo;
    private Integer cantidad;
    private String observacion;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
