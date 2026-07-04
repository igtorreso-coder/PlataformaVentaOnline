package com.VentaOnline.CategoryService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Solicitud para crear o actualizar una categoría")
public class CategoriaRequestDTO {
    @Schema(description = "Nombre de la categoría", example = "Electrónica")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre debe tener máximo 100 caracteres")
    private String nombre;

    @Schema(description = "Descripción de la categoría", example = "Productos electrónicos y gadgets")
    private String descripcion;
}
