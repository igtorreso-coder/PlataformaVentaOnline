package com.VentaOnline.NotificationService.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Respuesta con datos de una notificación")
public class NotificacionResponseDTO {

    @Schema(description = "ID de la notificación", example = "1")
    private Long id;

    @Schema(description = "ID del usuario destinatario", example = "1")
    private Long usuarioId;

    @Schema(description = "Tipo de notificación", example = "EMAIL")
    private String tipo;

    @Schema(description = "Asunto de la notificación", example = "Compra confirmada")
    private String asunto;

    @Schema(description = "Cuerpo del mensaje", example = "Su compra ha sido confirmada exitosamente")
    private String mensaje;

    @Schema(description = "Destinatario", example = "ignacio@email.com")
    private String destinatario;

    @Schema(description = "Estado de la notificación (PENDIENTE, ENVIADO, ERROR)", example = "ENVIADO")
    private String estado;

    @Schema(description = "Fecha y hora de envío")
    private LocalDateTime fechaEnvio;

    @Schema(description = "Mensaje de error en caso de fallo")
    private String errorMensaje;

    @Schema(description = "Fecha y hora de creación del registro")
    private LocalDateTime createdAt;

    @Schema(description = "Fecha y hora de la última actualización")
    private LocalDateTime updatedAt;
}
