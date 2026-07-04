package com.VentaOnline.NotificationService.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.VentaOnline.NotificationService.dto.NotificacionRequestDTO;
import com.VentaOnline.NotificationService.dto.NotificacionResponseDTO;
import com.VentaOnline.NotificationService.service.NotificacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/notificaciones")
@Slf4j
@Tag(name = "Notificaciones", description = "Endpoints para gestión de notificaciones del sistema")
public class NotificacionController {

    @Autowired
    private NotificacionService notificacionService;

    @Operation(summary = "Obtener todas las notificaciones", description = "Retorna la lista completa de notificaciones del sistema")
    @ApiResponse(responseCode = "200", description = "Lista de notificaciones")
    @GetMapping
    public ResponseEntity<List<NotificacionResponseDTO>> obtenerNotificaciones() {
        log.info("GET /api/notificaciones");
        return ResponseEntity.ok(notificacionService.obtenerTodasNotificaciones());
    }

    @Operation(summary = "Obtener notificación por ID", description = "Retorna los detalles de una notificación por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación encontrada"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<NotificacionResponseDTO> obtenerNotificacionPorId(@Parameter(description = "ID de la notificación", example = "1") @PathVariable Long id) {
        log.info("GET /api/notificaciones/{}", id);
        return ResponseEntity.ok(notificacionService.obtenerNotificacionPorId(id));
    }

    @Operation(summary = "Obtener notificaciones por usuario", description = "Retorna las notificaciones filtradas por ID de usuario")
    @ApiResponse(responseCode = "200", description = "Lista de notificaciones del usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<NotificacionResponseDTO>> obtenerNotificacionesPorUsuario(@Parameter(description = "ID del usuario", example = "1") @PathVariable Long usuarioId) {
        log.info("GET /api/notificaciones/usuario/{}", usuarioId);
        return ResponseEntity.ok(notificacionService.obtenerNotificacionesPorUsuario(usuarioId));
    }

    @Operation(summary = "Crear nueva notificación", description = "Crea una nueva notificación para un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Notificación creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de la notificación inválidos")
    })
    @PostMapping
    public ResponseEntity<NotificacionResponseDTO> crearNotificacion(@Valid @RequestBody NotificacionRequestDTO request) {
        log.info("POST /api/notificaciones");
        NotificacionResponseDTO notificacion = notificacionService.crearNotificacion(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(notificacion.getId()).toUri();
        return ResponseEntity.created(location).body(notificacion);
    }

    @Operation(summary = "Enviar notificación", description = "Marca una notificación como enviada y la envía al usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Notificación enviada exitosamente"),
        @ApiResponse(responseCode = "400", description = "La notificación no puede ser enviada"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @PostMapping("/{id}/enviar")
    public ResponseEntity<NotificacionResponseDTO> enviarNotificacion(@Parameter(description = "ID de la notificación a enviar", example = "1") @PathVariable Long id) {
        log.info("POST /api/notificaciones/{}/enviar", id);
        return ResponseEntity.ok(notificacionService.enviarNotificacion(id));
    }

    @Operation(summary = "Eliminar notificación", description = "Elimina una notificación del sistema por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Notificación eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Notificación no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarNotificacion(@Parameter(description = "ID de la notificación a eliminar", example = "1") @PathVariable Long id) {
        log.info("DELETE /api/notificaciones/{}", id);
        notificacionService.eliminarNotificacion(id);
        return ResponseEntity.noContent().build();
    }
}
