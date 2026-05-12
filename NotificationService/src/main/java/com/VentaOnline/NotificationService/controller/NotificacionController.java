package com.VentaOnline.NotificationService.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.VentaOnline.NotificationService.dto.NotificacionRequestDTO;
import com.VentaOnline.NotificationService.dto.NotificacionResponseDTO;
import com.VentaOnline.NotificationService.service.NotificacionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/notificaciones")
@Slf4j
public class NotificacionController {

    private final NotificacionService notificacionService;

    public NotificacionController(NotificacionService notificacionService) {
        this.notificacionService = notificacionService;
    }

    @GetMapping
    public ResponseEntity<List<NotificacionResponseDTO>> obtenerNotificaciones() {
        log.info("GET /api/notificaciones");
        return ResponseEntity.ok(notificacionService.obtenerTodasNotificaciones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificacionResponseDTO> obtenerNotificacionById(@PathVariable Long id) {
        log.info("GET /api/notificaciones/{}", id);
        return ResponseEntity.ok(notificacionService.obtenerNotificacionById(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<NotificacionResponseDTO>> obtenerNotificacionesByUsuario(@PathVariable Long usuarioId) {
        log.info("GET /api/notificaciones/usuario/{}", usuarioId);
        return ResponseEntity.ok(notificacionService.obtenerNotificacionesByUsuario(usuarioId));
    }

    @PostMapping
    public ResponseEntity<NotificacionResponseDTO> crearNotificacion(@Valid @RequestBody NotificacionRequestDTO request) {
        log.info("POST /api/notificaciones");
        NotificacionResponseDTO notificacion = notificacionService.crearNotificacion(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(notificacion.getId()).toUri();
        return ResponseEntity.created(location).body(notificacion);
    }

    @PostMapping("/{id}/enviar")
    public ResponseEntity<NotificacionResponseDTO> enviarNotificacion(@PathVariable Long id) {
        log.info("POST /api/notificaciones/{}/enviar", id);
        return ResponseEntity.ok(notificacionService.enviarNotificacion(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNotificacion(@PathVariable Long id) {
        log.info("DELETE /api/notificaciones/{}", id);
        notificacionService.eliminarNotificacion(id);
        return ResponseEntity.noContent().build();
    }
}
