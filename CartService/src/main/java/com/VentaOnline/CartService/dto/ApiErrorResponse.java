package com.VentaOnline.CartService.dto;

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

    @Schema(description = "Código de estado HTTP", example = "404")
    private Integer status;

    @Schema(description = "Descripción del error", example = "Not Found")
    private String error;

    @Schema(description = "Mensaje descriptivo del error", example = "Carrito no encontrado")
    private String message;

    @Schema(description = "Ruta del endpoint que generó el error", example = "/api/carritos/99")
    private String path;

    @Schema(description = "Lista de errores de validación específicos")
    private List<String> errors;
}
