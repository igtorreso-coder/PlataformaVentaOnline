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
@Schema(description = "Respuesta de inicio de sesión con token JWT")
public class LoginResponseDTO {

    @Schema(description = "Token de autenticación", example = "5548534372e94a8b85b9e8ec28e08dca1781879243942")
    private String token;

    @Schema(description = "ID del usuario autenticado", example = "1")
    private Long usuarioId;

    @Schema(description = "Nombre completo del usuario", example = "Ignacio Torres")
    private String nombreCompleto;

    @Schema(description = "Correo electrónico del usuario", example = "ignacio@email.com")
    private String correo;

    @Schema(description = "Rol del usuario (ADMIN, USER)", example = "ADMIN")
    private String rol;
}
