package com.VentaOnline.AuthService.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ValidarTokenResponseDTO {

    private boolean valido;
    private Long usuarioId;
    private String correo;
    private String rol;
    private String mensaje;
}
