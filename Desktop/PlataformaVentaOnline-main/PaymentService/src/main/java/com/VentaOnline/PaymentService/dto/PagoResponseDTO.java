package com.VentaOnline.PaymentService.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagoResponseDTO {

    private Long id;
    private Long pedidoId;
    private BigDecimal monto;
    private String metodoPago;
    private String estado;
    private String referencia;
    private LocalDateTime fechaPago;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
