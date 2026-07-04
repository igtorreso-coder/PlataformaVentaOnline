package com.VentaOnline.OrderService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Solicitud de detalle de producto para un pedido")
public class PedidoDetalleRequestDTO {
    @Schema(description = "ID del producto", example = "1")
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;

    @Schema(description = "Cantidad del producto", example = "2")
    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;
}
