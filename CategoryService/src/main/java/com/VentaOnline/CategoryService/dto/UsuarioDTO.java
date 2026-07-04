package com.VentaOnline.CategoryService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO de usuario para comunicación entre servicios")
public class UsuarioDTO {
    @Schema(description = "ID del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre completo del usuario", example = "Ignacio Torres")
    private String nombreCompleto;

    @Schema(description = "Correo electrónico del usuario", example = "ignacio@email.com")
    private String correo;
}
