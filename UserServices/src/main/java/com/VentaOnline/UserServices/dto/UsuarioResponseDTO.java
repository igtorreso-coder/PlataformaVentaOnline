package com.VentaOnline.UserServices.dto;

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
@Schema(description = "Respuesta con datos de un usuario")
public class UsuarioResponseDTO {
    @Schema(description = "ID del usuario", example = "1")
    private Long id;

    @Schema(description = "Nombre completo del usuario", example = "Ignacio Torres")
    private String nombreCompleto;

    @Schema(description = "Correo electrónico del usuario", example = "ignacio@email.com")
    private String correo;

    @Schema(description = "Fecha y hora de creación del registro")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de la última actualización")
    private LocalDateTime updatedAt;
}
