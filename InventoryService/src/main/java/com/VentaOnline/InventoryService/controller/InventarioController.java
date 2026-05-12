package com.VentaOnline.InventoryService.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.VentaOnline.InventoryService.dto.InventarioRequestDTO;
import com.VentaOnline.InventoryService.dto.InventarioResponseDTO;
import com.VentaOnline.InventoryService.dto.ProductoResponse;
import com.VentaOnline.InventoryService.service.InventarioService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/inventarios")
@Slf4j
public class InventarioController {
    @Autowired
    private InventarioService inventarioService;

    @GetMapping
    public ResponseEntity<List<InventarioResponseDTO>> obtenerTodosMovimientos() {
        log.info("GET /api/inventarios");
        return ResponseEntity.ok(inventarioService.obtenerTodosMovimientos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InventarioResponseDTO> obtenerMovimientoById(@PathVariable Long id) {
        log.info("GET /api/inventarios/{}", id);
        return ResponseEntity.ok(inventarioService.obtenerMovimientoById(id));
    }

    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<InventarioResponseDTO>> obtenerMovimientosByProducto(@PathVariable Long productoId) {
        log.info("GET /api/inventarios/producto/{}", productoId);
        return ResponseEntity.ok(inventarioService.obtenerMovimientosByProducto(productoId));
    }

    @PostMapping
    public ResponseEntity<InventarioResponseDTO> crearMovimiento(@Valid @RequestBody InventarioRequestDTO request) {
        log.info("POST /api/inventarios");
        InventarioResponseDTO movimiento = inventarioService.crearMovimiento(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(movimiento.getId()).toUri();
        return ResponseEntity.created(location).body(movimiento);
    }

    @GetMapping("/stock/{productoId}")
    public ResponseEntity<Integer> obtenerStockByProducto(@PathVariable Long productoId) {
        log.info("GET /api/inventarios/stock/{}", productoId);
        return ResponseEntity.ok(inventarioService.obtenerStockByProducto(productoId));
    }

    @GetMapping("/productos")
    public ResponseEntity<List<ProductoResponse>> obtenerProductos() {
        log.info("GET /api/inventarios/productos");
        return ResponseEntity.ok(inventarioService.obtenerProductos());
    }
}
