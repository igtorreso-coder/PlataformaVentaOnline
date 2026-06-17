package com.VentaOnline.NotificationService.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificacionResponseDTO {

    private Long id;
    private Long usuarioId;
    private String tipo;
    private String asunto;
    private String mensaje;
    private String destinatario;
    private String estado;
    private LocalDateTime fechaEnvio;
    private String errorMensaje;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
