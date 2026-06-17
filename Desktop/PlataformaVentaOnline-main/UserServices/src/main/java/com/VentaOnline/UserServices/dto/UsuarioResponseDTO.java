package com.VentaOnline.UserServices.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponseDTO {
    private Long id;
    private String nombreCompleto;
    private String correo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
