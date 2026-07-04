package com.VentaOnline.AuthService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
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
@Schema(description = "Solicitud de registro de nuevo usuario")
public class RegistroRequestDTO {

    @Schema(description = "Nombre completo del nuevo usuario", example = "Nuevo Usuario")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre debe tener máximo 100 caracteres")
    private String nombreCompleto;

    @Schema(description = "Correo electrónico del nuevo usuario", example = "nuevo@email.com")
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Size(max = 100, message = "El correo debe tener máximo 100 caracteres")
    @Email(message = "El correo debe ser válido")
    private String correo;

    @Schema(description = "Contraseña en texto plano (mínimo 6 caracteres)", example = "123456")
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String contrasena;
}
