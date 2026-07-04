package com.VentaOnline.ProductService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta con datos de un producto")
public class ProductoResponseDTO {
    @Schema(description = "ID del producto", example = "1")
    private Long id;

    @Schema(description = "Nombre del producto", example = "Smartphone X")
    private String nombre;

    @Schema(description = "Descripción del producto", example = "Smartphone de última generación con 256GB")
    private String descripcion;

    @Schema(description = "Precio del producto", example = "25000.00")
    private BigDecimal precio;

    @Schema(description = "Nombre de la categoría", example = "Electrónica")
    private String categoriaNombre;

    @Schema(description = "Stock disponible", example = "100")
    private Integer stock;

    @Schema(description = "Fecha y hora de creación del registro")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de la última actualización")
    private LocalDateTime updatedAt;
}
