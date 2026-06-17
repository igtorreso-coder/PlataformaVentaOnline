package com.VentaOnline.NotificationService.dto;

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
public class NotificacionRequestDTO {

    @NotNull(message = "El ID del usuario es obligatorio")
    private Long usuarioId;

    /*
     * Tipo de notificacion: "EMAIL", "SMS" o "PUSH".
     * Se determinara el canal por el que se enviara el mensaje.
     */
    @NotBlank(message = "El tipo es obligatorio")
    @Size(max = 20, message = "El tipo debe tener máximo 20 caracteres")
    private String tipo;

    @NotBlank(message = "El asunto es obligatorio")
    @Size(max = 200, message = "El asunto debe tener máximo 200 caracteres")
    private String asunto;

    @NotBlank(message = "El mensaje es obligatorio")
    private String mensaje;

    @NotBlank(message = "El destinatario es obligatorio")
    @Size(max = 150, message = "El destinatario debe tener máximo 150 caracteres")
    private String destinatario;
}
