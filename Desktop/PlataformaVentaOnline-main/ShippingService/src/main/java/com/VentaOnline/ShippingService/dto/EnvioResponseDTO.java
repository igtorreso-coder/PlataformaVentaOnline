package com.VentaOnline.ShippingService.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnvioResponseDTO {

    private Long id;
    private Long pedidoId;
    private String direccion;
    private String ciudad;
    private String estado;
    private String codigoSeguimiento;
    private LocalDateTime fechaEnvio;
    private LocalDateTime fechaEntrega;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
