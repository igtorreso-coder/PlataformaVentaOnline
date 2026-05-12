package com.VentaOnline.PaymentService.dto;

import java.math.BigDecimal;
import jakarta.validation.constraints.DecimalMin;
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
public class PagoRequestDTO {

    @NotNull(message = "El ID del pedido es obligatorio")
    private Long pedidoId;

    /*
     * Monto total a pagar. Debe coincidir con el total calculado del pedido.
     * @DecimalMin("0.01") asegura que sea un valor positivo.
     */
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto debe ser mayor a 0")
    private BigDecimal monto;

    /*
     * Metodo de pago seleccionado "TARJETA_CREDITO", "TRANSFERENCIA",
     * "PAYPAL", "MERCADO_PAGO").
     */
    @NotBlank(message = "El método de pago es obligatorio")
    @Size(max = 20, message = "El método de pago debe tener máximo 20 caracteres")
    private String metodoPago;
}
