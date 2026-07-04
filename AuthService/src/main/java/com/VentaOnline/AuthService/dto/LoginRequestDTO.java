package com.VentaOnline.AuthService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Solicitud de inicio de sesión")
public class LoginRequestDTO {

    @Schema(description = "Correo electrónico del usuario", example = "ignacio@email.com")
    @NotBlank(message = "El correo es obligatorio")
    private String correo;

    @Schema(description = "Contraseña del usuario", example = "123456")
    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasena;
}
