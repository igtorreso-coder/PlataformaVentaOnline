package com.VentaOnline.UserServices.dto;

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
@Schema(description = "Solicitud para crear o actualizar un usuario")
public class UsuarioRequestDTO {
    @Schema(description = "Nombre completo del usuario", example = "Ignacio Torres")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100, message = "El nombre debe tener máximo 100 caracteres")
    private String nombre;

    @Schema(description = "Correo electrónico del usuario", example = "ignacio@email.com")
    @NotBlank(message = "El correo electrónico es obligatorio")
    @Size(max = 100, message = "El correo debe tener máximo 100 caracteres")
    @Email(message = "El correo debe ser válido")
    private String correo;
}
