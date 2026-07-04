package com.VentaOnline.ProductService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de categoría para comunicación entre servicios")
public class CategoriaResponse {
    @Schema(description = "ID de la categoría", example = "1")
    private Long id;

    @Schema(description = "Nombre de la categoría", example = "Electrónica")
    private String nombre;
}
