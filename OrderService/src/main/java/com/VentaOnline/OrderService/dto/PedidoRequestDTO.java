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
    // ID del usuario que realiza el pedido (obtenido del servicio de usuarios)
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    /*
     * @Valid fuerza a Bean Validation a validar CADA objeto dentro de la lista.
     * Sin @Valid, solo se verificaria que la lista no sea nula, pero NO se
     * validarian los campos internos de cada PedidoDetalleRequestDTO
     */
    @Valid
    @NotNull(message = "Debe incluir al menos un producto")
    private List<PedidoDetalleRequestDTO> detalles;
}
