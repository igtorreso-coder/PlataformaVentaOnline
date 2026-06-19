package com.VentaOnline.PaymentService.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.VentaOnline.PaymentService.dto.EstadoRequestDTO;
import com.VentaOnline.PaymentService.dto.PagoRequestDTO;
import com.VentaOnline.PaymentService.dto.PagoResponseDTO;
import com.VentaOnline.PaymentService.service.PagoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/pagos")
@Slf4j
@Tag(name = "Pagos", description = "Endpoints para gestión de pagos y procesamiento de transacciones")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @Operation(summary = "Obtener todos los pagos", description = "Retorna la lista completa de pagos registrados")
    @ApiResponse(responseCode = "200", description = "Lista de pagos")
    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> obtenerPagos() {
        log.info("GET /api/pagos");
        return ResponseEntity.ok(pagoService.obtenerTodosPagos());
    }

    @Operation(summary = "Obtener pago por ID", description = "Retorna los detalles de un pago por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago encontrado"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> obtenerPagoPorId(@PathVariable Long id) {
        log.info("GET /api/pagos/{}", id);
        return ResponseEntity.ok(pagoService.obtenerPagoPorId(id));
    }

    @Operation(summary = "Obtener pagos por pedido", description = "Retorna los pagos filtrados por ID de pedido")
    @ApiResponse(responseCode = "200", description = "Lista de pagos del pedido")
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<PagoResponseDTO>> obtenerPagosPorPedido(@PathVariable Long pedidoId) {
        log.info("GET /api/pagos/pedido/{}", pedidoId);
        return ResponseEntity.ok(pagoService.obtenerPagosPorPedido(pedidoId));
    }

    @Operation(summary = "Crear nuevo pago", description = "Registra un nuevo pago asociado a un pedido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pago creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del pago inválidos")
    })
    @PostMapping
    public ResponseEntity<PagoResponseDTO> crearPago(@Valid @RequestBody PagoRequestDTO request) {
        log.info("POST /api/pagos");
        PagoResponseDTO pago = pagoService.crearPago(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(pago.getId()).toUri();
        return ResponseEntity.created(location).body(pago);
    }

    @Operation(summary = "Procesar pago", description = "Procesa un pago pendiente y actualiza su estado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pago procesado exitosamente"),
        @ApiResponse(responseCode = "400", description = "El pago no puede ser procesado"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @PostMapping("/{id}/procesar")
    public ResponseEntity<PagoResponseDTO> procesarPago(@PathVariable Long id) {
        log.info("POST /api/pagos/{}/procesar", id);
        return ResponseEntity.ok(pagoService.procesarPago(id));
    }

    @Operation(summary = "Actualizar estado del pago", description = "Actualiza el estado de un pago existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Estado inválido"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PagoResponseDTO> actualizarEstado(@PathVariable Long id,
            @Valid @RequestBody EstadoRequestDTO request) {
        log.info("PATCH /api/pagos/{}/estado", id);
        return ResponseEntity.ok(pagoService.actualizarEstadoPago(id, request.getEstado()));
    }

    @Operation(summary = "Eliminar pago", description = "Elimina un pago del sistema por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pago eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pago no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        log.info("DELETE /api/pagos/{}", id);
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }
}
