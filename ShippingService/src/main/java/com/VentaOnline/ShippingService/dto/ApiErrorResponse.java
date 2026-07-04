package com.VentaOnline.ShippingService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Estructura estándar de respuesta de error")
public class ApiErrorResponse {
    @Schema(description = "Momento en que ocurrió el error")
    private LocalDateTime timestamp;

    @Schema(description = "Código de estado HTTP", example = "400")
    private Integer status;

    @Schema(description = "Descripción del error", example = "Bad Request")
    private String error;

    @Schema(description = "Mensaje descriptivo del error", example = "El pedido no está confirmado")
    private String message;

    @Schema(description = "Ruta del endpoint que generó el error", example = "/api/envios")
    private String path;

    @Schema(description = "Lista de errores de validación específicos")
    private List<String> errors;
}
