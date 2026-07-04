package com.VentaOnline.ShippingService.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.VentaOnline.ShippingService.dto.EnvioRequestDTO;
import com.VentaOnline.ShippingService.dto.EnvioResponseDTO;
import com.VentaOnline.ShippingService.dto.EstadoRequestDTO;
import com.VentaOnline.ShippingService.service.EnvioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/envios")
@Slf4j
@Tag(name = "Envíos", description = "Endpoints para gestión de envíos y logística")
public class EnvioController {

    @Autowired
    private EnvioService envioService;

    @Operation(summary = "Obtener todos los envíos", description = "Retorna la lista completa de envíos registrados")
    @ApiResponse(responseCode = "200", description = "Lista de envíos")
    @GetMapping
    public ResponseEntity<List<EnvioResponseDTO>> obtenerEnvios() {
        log.info("GET /api/envios");
        return ResponseEntity.ok(envioService.obtenerTodosEnvios());
    }

    @Operation(summary = "Obtener envío por ID", description = "Retorna los detalles de un envío por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Envío encontrado"),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<EnvioResponseDTO> obtenerEnvioPorId(@Parameter(description = "ID del envío", example = "1") @PathVariable Long id) {
        log.info("GET /api/envios/{}", id);
        return ResponseEntity.ok(envioService.obtenerEnvioPorId(id));
    }

    @Operation(summary = "Obtener envíos por pedido", description = "Retorna los envíos filtrados por ID de pedido")
    @ApiResponse(responseCode = "200", description = "Lista de envíos del pedido")
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<EnvioResponseDTO>> obtenerEnviosPorPedido(@Parameter(description = "ID del pedido", example = "1") @PathVariable Long pedidoId) {
        log.info("GET /api/envios/pedido/{}", pedidoId);
        return ResponseEntity.ok(envioService.obtenerEnviosPorPedido(pedidoId));
    }

    @Operation(summary = "Crear nuevo envío", description = "Registra un nuevo envío asociado a un pedido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Envío creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del envío inválidos")
    })
    @PostMapping
    public ResponseEntity<EnvioResponseDTO> crearEnvio(@Valid @RequestBody EnvioRequestDTO request) {
        log.info("POST /api/envios");
        EnvioResponseDTO envio = envioService.crearEnvio(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(envio.getId()).toUri();
        return ResponseEntity.created(location).body(envio);
    }

    @Operation(summary = "Enviar envío", description = "Marca un envío como enviado y actualiza su estado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Envío procesado exitosamente"),
        @ApiResponse(responseCode = "400", description = "El envío no puede ser procesado"),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    @PostMapping("/{id}/enviar")
    public ResponseEntity<EnvioResponseDTO> enviarEnvio(@Parameter(description = "ID del envío a procesar", example = "1") @PathVariable Long id) {
        log.info("POST /api/envios/{}/enviar", id);
        return ResponseEntity.ok(envioService.enviarEnvio(id));
    }

    @Operation(summary = "Actualizar estado del envío", description = "Actualiza el estado de un envío existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Estado inválido"),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<EnvioResponseDTO> actualizarEstado(@Parameter(description = "ID del envío", example = "1") @PathVariable Long id,
            @Valid @RequestBody EstadoRequestDTO request) {
        log.info("PATCH /api/envios/{}/estado", id);
        return ResponseEntity.ok(envioService.actualizarEstadoEnvio(id, request.getEstado()));
    }

    @Operation(summary = "Eliminar envío", description = "Elimina un envío del sistema por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Envío eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Envío no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEnvio(@Parameter(description = "ID del envío a eliminar", example = "1") @PathVariable Long id) {
        log.info("DELETE /api/envios/{}", id);
        envioService.eliminarEnvio(id);
        return ResponseEntity.noContent().build();
    }
}
