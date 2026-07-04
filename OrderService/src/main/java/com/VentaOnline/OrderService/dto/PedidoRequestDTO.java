package com.VentaOnline.OrderService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Solicitud para crear un nuevo pedido")
public class PedidoRequestDTO {
    @Schema(description = "ID del usuario que realiza el pedido", example = "1")
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @Schema(description = "Lista de productos incluidos en el pedido")
    @Valid
    @NotNull(message = "Debe incluir al menos un producto")
    private List<PedidoDetalleRequestDTO> detalles;
}
