package com.VentaOnline.DiscountService.dto;

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
public class CuponResponseDTO {

    private Long id;
    private String codigo;
    private String tipo;
    private BigDecimal valor;
    private BigDecimal montoMinimo;
    private Integer usosMaximos;
    private Integer usosActuales;
    private LocalDateTime fechaExpiracion;
    private Boolean activo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
