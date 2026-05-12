package com.VentaOnline.OrderService.dto;

import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoRequestDTO {
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @Valid
    @NotNull(message = "Debe incluir al menos un producto")
    private List<PedidoDetalleRequestDTO> detalles;
}
