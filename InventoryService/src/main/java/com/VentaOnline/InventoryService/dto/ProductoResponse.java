package com.VentaOnline.InventoryService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de producto para comunicación entre servicios")
public class ProductoResponse {
    @Schema(description = "ID del producto", example = "1")
    private Long id;

    @Schema(description = "Nombre del producto", example = "Smartphone X")
    private String nombre;

    @Schema(description = "Precio del producto", example = "25000.00")
    private BigDecimal precio;

    @Schema(description = "Stock disponible", example = "100")
    private Integer stock;
}
