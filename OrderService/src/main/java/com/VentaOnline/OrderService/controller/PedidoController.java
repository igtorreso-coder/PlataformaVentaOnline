package com.VentaOnline.OrderService.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.VentaOnline.OrderService.dto.EstadoRequestDTO;
import com.VentaOnline.OrderService.dto.PedidoRequestDTO;
import com.VentaOnline.OrderService.dto.PedidoResponseDTO;
import com.VentaOnline.OrderService.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/pedidos")
@Slf4j
@Tag(name = "Pedidos", description = "Endpoints para gestión de pedidos y sus detalles")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @Operation(summary = "Obtener todos los pedidos", description = "Retorna la lista completa de pedidos registrados")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos")
    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> obtenerPedidos() {
        log.info("GET /api/pedidos");
        return ResponseEntity.ok(pedidoService.obtenerTodosPedidos());
    }

    @Operation(summary = "Obtener pedido por ID", description = "Retorna los detalles de un pedido por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> obtenerPedidoPorId(@Parameter(description = "ID del pedido", example = "1") @PathVariable Long id) {
        log.info("GET /api/pedidos/{}", id);
        return ResponseEntity.ok(pedidoService.obtenerPedidoPorId(id));
    }

    @Operation(summary = "Obtener pedidos por usuario", description = "Retorna los pedidos filtrados por ID de usuario")
    @ApiResponse(responseCode = "200", description = "Lista de pedidos del usuario")
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PedidoResponseDTO>> obtenerPedidosPorUsuario(@Parameter(description = "ID del usuario", example = "1") @PathVariable Long usuarioId) {
        log.info("GET /api/pedidos/usuario/{}", usuarioId);
        return ResponseEntity.ok(pedidoService.obtenerPedidosPorUsuario(usuarioId));
    }

    @Operation(summary = "Crear nuevo pedido", description = "Crea un nuevo pedido con sus detalles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del pedido inválidos")
    })
    @PostMapping
    public ResponseEntity<PedidoResponseDTO> crearPedido(@Valid @RequestBody PedidoRequestDTO request) {
        log.info("POST /api/pedidos");
        PedidoResponseDTO pedido = pedidoService.crearPedido(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(location).body(pedido);
    }

    @Operation(summary = "Actualizar estado del pedido", description = "Actualiza el estado de un pedido existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Estado inválido"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @PatchMapping("/{id}/estado")
    public ResponseEntity<PedidoResponseDTO> actualizarEstado(@Parameter(description = "ID del pedido", example = "1") @PathVariable Long id,
            @Valid @RequestBody EstadoRequestDTO request) {
        log.info("PATCH /api/pedidos/{}/estado", id);
        return ResponseEntity.ok(pedidoService.actualizarEstadoPedido(id, request.getEstado()));
    }

    @Operation(summary = "Eliminar pedido", description = "Elimina un pedido del sistema por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pedido eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Pedido no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@Parameter(description = "ID del pedido a eliminar", example = "1") @PathVariable Long id) {
        log.info("DELETE /api/pedidos/{}", id);
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
