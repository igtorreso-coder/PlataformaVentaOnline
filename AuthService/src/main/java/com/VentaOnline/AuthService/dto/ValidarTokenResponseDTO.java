package com.VentaOnline.AuthService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta de validación de token")
public class ValidarTokenResponseDTO {

    @Schema(description = "Indica si el token es válido", example = "true")
    private boolean valido;

    @Schema(description = "ID del usuario asociado al token", example = "1")
    private Long usuarioId;

    @Schema(description = "Correo electrónico del usuario", example = "ignacio@email.com")
    private String correo;

    @Schema(description = "Rol del usuario", example = "ADMIN")
    private String rol;

    @Schema(description = "Mensaje descriptivo del resultado", example = "Token válido")
    private String mensaje;
}
