package com.VentaOnline.ProductService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
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
@Schema(description = "Solicitud para crear o actualizar un producto")
public class ProductoRequestDTO {

    @Schema(description = "Nombre del producto", example = "Smartphone X")
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 200, message = "El nombre debe tener máximo 200 caracteres")
    private String nombre;

    @Schema(description = "Descripción del producto", example = "Smartphone de última generación con 256GB")
    private String descripcion;

    @Schema(description = "Precio del producto", example = "25000.00")
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal precio;

    @Schema(description = "ID de la categoría a la que pertenece", example = "1")
    @NotNull(message = "La categoría es obligatoria")
    private Long categoriaId;

    @Schema(description = "Stock inicial del producto", example = "100")
    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
}
