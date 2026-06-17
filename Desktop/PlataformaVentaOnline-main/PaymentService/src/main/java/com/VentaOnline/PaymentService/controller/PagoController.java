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
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/pagos")
@Slf4j
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @GetMapping
    public ResponseEntity<List<PagoResponseDTO>> obtenerPagos() {
        log.info("GET /api/pagos");
        return ResponseEntity.ok(pagoService.obtenerTodosPagos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagoResponseDTO> obtenerPagoById(@PathVariable Long id) {
        log.info("GET /api/pagos/{}", id);
        return ResponseEntity.ok(pagoService.obtenerPagoById(id));
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<PagoResponseDTO>> obtenerPagosByPedido(@PathVariable Long pedidoId) {
        log.info("GET /api/pagos/pedido/{}", pedidoId);
        return ResponseEntity.ok(pagoService.obtenerPagosByPedido(pedidoId));
    }

    @PostMapping
    public ResponseEntity<PagoResponseDTO> crearPago(@Valid @RequestBody PagoRequestDTO request) {
        log.info("POST /api/pagos");
        PagoResponseDTO pago = pagoService.crearPago(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(pago.getId()).toUri();
        return ResponseEntity.created(location).body(pago);
    }

    @PostMapping("/{id}/procesar")
    public ResponseEntity<PagoResponseDTO> procesarPago(@PathVariable Long id) {
        log.info("POST /api/pagos/{}/procesar", id);
        return ResponseEntity.ok(pagoService.procesarPago(id));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<PagoResponseDTO> actualizarEstado(@PathVariable Long id,
            @Valid @RequestBody EstadoRequestDTO request) {
        log.info("PATCH /api/pagos/{}/estado", id);
        return ResponseEntity.ok(pagoService.actualizarEstadoPago(id, request.getEstado()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPago(@PathVariable Long id) {
        log.info("DELETE /api/pagos/{}", id);
        pagoService.eliminarPago(id);
        return ResponseEntity.noContent().build();
    }
}
