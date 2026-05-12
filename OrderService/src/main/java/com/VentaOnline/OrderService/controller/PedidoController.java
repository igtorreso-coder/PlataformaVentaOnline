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
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/pedidos")
@Slf4j
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity<List<PedidoResponseDTO>> obtenerPedidos() {
        log.info("GET /api/pedidos");
        return ResponseEntity.ok(pedidoService.obtenerTodosPedidos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> obtenerPedidoById(@PathVariable Long id) {
        log.info("GET /api/pedidos/{}", id);
        return ResponseEntity.ok(pedidoService.obtenerPedidoById(id));
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<PedidoResponseDTO>> obtenerPedidosByUsuario(@PathVariable Long usuarioId) {
        log.info("GET /api/pedidos/usuario/{}", usuarioId);
        return ResponseEntity.ok(pedidoService.obtenerPedidosByUsuario(usuarioId));
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> crearPedido(@Valid @RequestBody PedidoRequestDTO request) {
        log.info("POST /api/pedidos");
        PedidoResponseDTO pedido = pedidoService.crearPedido(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(pedido.getId()).toUri();
        return ResponseEntity.created(location).body(pedido);
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<PedidoResponseDTO> actualizarEstado(@PathVariable Long id,
            @Valid @RequestBody EstadoRequestDTO request) {
        log.info("PATCH /api/pedidos/{}/estado", id);
        return ResponseEntity.ok(pedidoService.actualizarEstadoPedido(id, request.getEstado()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        log.info("DELETE /api/pedidos/{}", id);
        pedidoService.eliminarPedido(id);
        return ResponseEntity.noContent().build();
    }
}
