package com.VentaOnline.ShippingService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Solicitud para crear un nuevo envío")
public class EnvioRequestDTO {

    @Schema(description = "ID del pedido asociado", example = "1")
    @NotNull(message = "El ID del pedido es obligatorio")
    private Long pedidoId;

    @Schema(description = "Dirección de entrega", example = "Av. Siempre Viva 123")
    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 255, message = "La dirección debe tener máximo 255 caracteres")
    private String direccion;

    @Schema(description = "Ciudad de entrega", example = "Buenos Aires")
    @NotBlank(message = "La ciudad es obligatoria")
    @Size(max = 100, message = "La ciudad debe tener máximo 100 caracteres")
    private String ciudad;
}
