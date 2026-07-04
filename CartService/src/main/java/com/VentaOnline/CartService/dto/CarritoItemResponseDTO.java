package com.VentaOnline.CartService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta con datos de un item del carrito")
public class CarritoItemResponseDTO {
    @Schema(description = "ID del item", example = "1")
    private Long id;

    @Schema(description = "ID del producto", example = "1")
    private Long productoId;

    @Schema(description = "Nombre del producto", example = "Smartphone X")
    private String nombreProducto;

    @Schema(description = "Cantidad seleccionada", example = "2")
    private Integer cantidad;

    @Schema(description = "Precio unitario del producto", example = "25000.00")
    private BigDecimal precioUnitario;

    @Schema(description = "Subtotal (cantidad x precio unitario)", example = "50000.00")
    private BigDecimal subtotal;
}
