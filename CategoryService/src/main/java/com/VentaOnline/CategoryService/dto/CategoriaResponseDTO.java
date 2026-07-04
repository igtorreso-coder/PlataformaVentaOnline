package com.VentaOnline.CategoryService.dto;

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
@Schema(description = "Respuesta con datos de una categoría")
public class CategoriaResponseDTO {
    @Schema(description = "ID de la categoría", example = "1")
    private Long id;

    @Schema(description = "Nombre de la categoría", example = "Electrónica")
    private String nombre;

    @Schema(description = "Descripción de la categoría", example = "Productos electrónicos y gadgets")
    private String descripcion;

    @Schema(description = "Fecha y hora de creación del registro")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de la última actualización")
    private LocalDateTime updatedAt;
}
