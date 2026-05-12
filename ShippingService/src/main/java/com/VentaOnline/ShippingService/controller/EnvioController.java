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
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/envios")
@Slf4j
public class EnvioController {

    private final EnvioService envioService;

    public EnvioController(EnvioService envioService) {
        this.envioService = envioService;
    }

    @GetMapping
    public ResponseEntity<List<EnvioResponseDTO>> obtenerEnvios() {
        log.info("GET /api/envios");
        return ResponseEntity.ok(envioService.obtenerTodosEnvios());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EnvioResponseDTO> obtenerEnvioById(@PathVariable Long id) {
        log.info("GET /api/envios/{}", id);
        return ResponseEntity.ok(envioService.obtenerEnvioById(id));
    }

    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<List<EnvioResponseDTO>> obtenerEnviosByPedido(@PathVariable Long pedidoId) {
        log.info("GET /api/envios/pedido/{}", pedidoId);
        return ResponseEntity.ok(envioService.obtenerEnviosByPedido(pedidoId));
    }

    @PostMapping
    public ResponseEntity<EnvioResponseDTO> crearEnvio(@Valid @RequestBody EnvioRequestDTO request) {
        log.info("POST /api/envios");
        EnvioResponseDTO envio = envioService.crearEnvio(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(envio.getId()).toUri();
        return ResponseEntity.created(location).body(envio);
    }

    @PostMapping("/{id}/enviar")
    public ResponseEntity<EnvioResponseDTO> enviarEnvio(@PathVariable Long id) {
        log.info("POST /api/envios/{}/enviar", id);
        return ResponseEntity.ok(envioService.enviarEnvio(id));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<EnvioResponseDTO> actualizarEstado(@PathVariable Long id,
            @Valid @RequestBody EstadoRequestDTO request) {
        log.info("PATCH /api/envios/{}/estado", id);
        return ResponseEntity.ok(envioService.actualizarEstadoEnvio(id, request.getEstado()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEnvio(@PathVariable Long id) {
        log.info("DELETE /api/envios/{}", id);
        envioService.eliminarEnvio(id);
        return ResponseEntity.noContent().build();
    }
}
