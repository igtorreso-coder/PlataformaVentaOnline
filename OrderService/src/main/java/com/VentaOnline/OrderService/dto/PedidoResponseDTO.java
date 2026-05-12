package com.VentaOnline.OrderService.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoResponseDTO {
    private Long id;
    private Long usuarioId;
    private String nombreUsuario;
    private LocalDateTime fecha;
    private BigDecimal total;
    private String estado;
    private List<PedidoDetalleResponseDTO> detalles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
