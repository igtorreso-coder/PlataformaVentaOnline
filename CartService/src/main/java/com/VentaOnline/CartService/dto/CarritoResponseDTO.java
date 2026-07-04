package com.VentaOnline.CartService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Respuesta con datos de un carrito de compras")
public class CarritoResponseDTO {
    @Schema(description = "ID del carrito", example = "1")
    private Long id;

    @Schema(description = "ID del usuario propietario", example = "1")
    private Long usuarioId;

    @Schema(description = "Estado del carrito (ACTIVO, FINALIZADO)", example = "ACTIVO")
    private String estado;

    @Schema(description = "Lista de items en el carrito")
    private List<CarritoItemResponseDTO> items;

    @Schema(description = "Fecha y hora de creación del registro")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de la última actualización")
    private LocalDateTime updatedAt;
}
