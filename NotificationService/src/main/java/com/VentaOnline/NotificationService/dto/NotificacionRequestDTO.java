package com.VentaOnline.NotificationService.dto;

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
@Schema(description = "Solicitud para crear una nueva notificación")
public class NotificacionRequestDTO {

    @Schema(description = "ID del usuario destinatario", example = "1")
    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    @Schema(description = "Tipo de notificación", example = "EMAIL", allowableValues = {"EMAIL", "SMS", "IN_APP"})
    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 20, message = "El tipo debe tener máximo 20 caracteres")
    private String tipo;

    @Schema(description = "Asunto de la notificación", example = "Compra confirmada")
    @NotBlank(message = "El asunto es obligatorio")
    @Size(max = 200, message = "El asunto debe tener máximo 200 caracteres")
    private String asunto;

    @Schema(description = "Cuerpo del mensaje", example = "Su compra ha sido confirmada exitosamente")
    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;

    @Schema(description = "Destinatario (correo, teléfono, etc.)", example = "ignacio@email.com")
    @NotBlank(message = "El destinatario es obligatorio")
    @Size(max = 150, message = "El destinatario debe tener máximo 150 caracteres")
    private String destinatario;
}
