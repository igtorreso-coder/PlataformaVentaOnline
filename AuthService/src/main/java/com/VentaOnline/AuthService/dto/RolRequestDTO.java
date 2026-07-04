package com.VentaOnline.AuthService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Solicitud para cambiar el rol de un usuario")
public class RolRequestDTO {
    @Schema(description = "Nombre del rol a asignar", example = "ADMIN", allowableValues = {"ADMIN", "USER", "MODERATOR"})
    @NotBlank(message = "El rol es obligatorio")
    private String rol;
}
