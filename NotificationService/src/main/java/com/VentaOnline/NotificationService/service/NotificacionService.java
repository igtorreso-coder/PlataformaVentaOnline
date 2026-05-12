package com.VentaOnline.NotificationService.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.VentaOnline.NotificationService.client.UserServiceClient;
import com.VentaOnline.NotificationService.dto.NotificacionRequestDTO;
import com.VentaOnline.NotificationService.dto.NotificacionResponseDTO;
import com.VentaOnline.NotificationService.model.Notificacion;
import com.VentaOnline.NotificationService.repository.NotificacionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Slf4j
public class NotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;
    @Autowired
    private UserServiceClient userServiceClient;

    public NotificacionResponseDTO crearNotificacion(NotificacionRequestDTO request) {
        log.info("Creando notificacion para usuario ID: {}, tipo: {}", request.getUsuarioId(), request.getTipo());

        if (!List.of("EMAIL", "SMS", "IN_APP").contains(request.getTipo().toUpperCase())) {
            throw new IllegalArgumentException("Tipo inválido: " + request.getTipo()
                    + ". Valores permitidos: EMAIL, SMS, IN_APP");
        }

        userServiceClient.obtenerUsuario(request.getUsuarioId());

        Notificacion notificacion = Notificacion.builder()
                .usuarioId(request.getUsuarioId())
                .tipo(request.getTipo().toUpperCase())
                .asunto(request.getAsunto())
                .mensaje(request.getMensaje())
                .destinatario(request.getDestinatario())
                .build();
        notificacion = notificacionRepository.save(notificacion);
        log.info("Notificacion creada con ID: {}", notificacion.getId());
        return toResponse(notificacion);
    }

    public List<NotificacionResponseDTO> obtenerTodasNotificaciones() {
        log.info("Obteniendo todas las notificaciones");
        return notificacionRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public NotificacionResponseDTO obtenerNotificacionById(Long id) {
        log.info("Obteniendo notificacion con ID: {}", id);
        return notificacionRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Notificación no encontrada con ID: " + id));
    }

    public List<NotificacionResponseDTO> obtenerNotificacionesByUsuario(Long usuarioId) {
        log.info("Obteniendo notificaciones para usuario ID: {}", usuarioId);
        return notificacionRepository.findByUsuarioIdOrderByCreatedAtDesc(usuarioId).stream()
                .map(this::toResponse)
                .toList();
    }

    public NotificacionResponseDTO enviarNotificacion(Long id) {
        log.info("Enviando notificacion con ID: {}", id);
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Notificación no encontrada con ID: " + id));

        if (!"PENDIENTE".equals(notificacion.getEstado())) {
            throw new IllegalArgumentException("La notificación ID " + id + " ya fue procesada. Estado: " + notificacion.getEstado());
        }

        try {
            log.info("Enviando {} a {}: {}", notificacion.getTipo(), notificacion.getDestinatario(), notificacion.getAsunto());
            notificacion.setEstado("ENVIADO");
            notificacion.setFechaEnvio(LocalDateTime.now());
        } catch (Exception e) {
            log.error("Error al enviar notificacion ID {}: {}", id, e.getMessage());
            notificacion.setEstado("FALLIDO");
            notificacion.setErrorMensaje(e.getMessage());
        }

        notificacion = notificacionRepository.save(notificacion);
        return toResponse(notificacion);
    }

    public void eliminarNotificacion(Long id) {
        log.info("Eliminando notificacion con ID: {}", id);
        if (!notificacionRepository.existsById(id)) {
            throw new NoSuchElementException("Notificación no encontrada con ID: " + id);
        }
        notificacionRepository.deleteById(id);
    }

    private NotificacionResponseDTO toResponse(Notificacion notificacion) {
        return NotificacionResponseDTO.builder()
                .id(notificacion.getId())
                .usuarioId(notificacion.getUsuarioId())
                .tipo(notificacion.getTipo())
                .asunto(notificacion.getAsunto())
                .mensaje(notificacion.getMensaje())
                .destinatario(notificacion.getDestinatario())
                .estado(notificacion.getEstado())
                .fechaEnvio(notificacion.getFechaEnvio())
                .errorMensaje(notificacion.getErrorMensaje())
                .createdAt(notificacion.getCreatedAt())
                .updatedAt(notificacion.getUpdatedAt())
                .build();
    }
}
