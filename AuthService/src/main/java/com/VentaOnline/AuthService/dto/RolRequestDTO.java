package com.VentaOnline.AuthService.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RolRequestDTO {
    // Nombre del rol a asignar ("ADMIN", "USER", "MODERATOR")
    @NotBlank(message = "El rol es obligatorio")
    private String rol;
}
