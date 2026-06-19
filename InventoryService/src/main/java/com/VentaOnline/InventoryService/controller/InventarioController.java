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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/inventarios")
@Slf4j
@Tag(name = "Inventario", description = "Endpoints para control de inventario y movimientos de stock")
public class InventarioController {
    @Autowired
    private InventarioService inventarioService;

    @Operation(summary = "Obtener todos los movimientos", description = "Retorna la lista completa de movimientos de inventario")
    @ApiResponse(responseCode = "200", description = "Lista de movimientos")
    @GetMapping
    public ResponseEntity<List<InventarioResponseDTO>> obtenerTodosMovimientos() {
        log.info("GET /api/inventarios");
        return ResponseEntity.ok(inventarioService.obtenerTodosMovimientos());
    }

    @Operation(summary = "Obtener movimiento por ID", description = "Retorna los detalles de un movimiento de inventario por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Movimiento encontrado"),
        @ApiResponse(responseCode = "404", description = "Movimiento no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<InventarioResponseDTO> obtenerMovimientoPorId(@Parameter(description = "ID del movimiento", example = "1") @PathVariable Long id) {
        log.info("GET /api/inventarios/{}", id);
        return ResponseEntity.ok(inventarioService.obtenerMovimientoPorId(id));
    }

    @Operation(summary = "Obtener movimientos por producto", description = "Retorna los movimientos de inventario filtrados por ID de producto")
    @ApiResponse(responseCode = "200", description = "Lista de movimientos del producto")
    @GetMapping("/producto/{productoId}")
    public ResponseEntity<List<InventarioResponseDTO>> obtenerMovimientosPorProducto(@Parameter(description = "ID del producto", example = "1") @PathVariable Long productoId) {
        log.info("GET /api/inventarios/producto/{}", productoId);
        return ResponseEntity.ok(inventarioService.obtenerMovimientosPorProducto(productoId));
    }

    @Operation(summary = "Crear movimiento de inventario", description = "Registra un nuevo movimiento de entrada o salida de stock")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Movimiento creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del movimiento inválidos")
    })
    @PostMapping
    public ResponseEntity<InventarioResponseDTO> crearMovimiento(@Valid @RequestBody InventarioRequestDTO request) {
        log.info("POST /api/inventarios");
        InventarioResponseDTO movimiento = inventarioService.crearMovimiento(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(movimiento.getId()).toUri();
        return ResponseEntity.created(location).body(movimiento);
    }

    @Operation(summary = "Obtener stock por producto", description = "Retorna el stock actual disponible para un producto")
    @ApiResponse(responseCode = "200", description = "Stock actual del producto")
    @GetMapping("/stock/{productoId}")
    public ResponseEntity<Integer> obtenerStockPorProducto(@Parameter(description = "ID del producto a consultar", example = "1") @PathVariable Long productoId) {
        log.info("GET /api/inventarios/stock/{}", productoId);
        return ResponseEntity.ok(inventarioService.obtenerStockPorProducto(productoId));
    }

    @Operation(summary = "Obtener productos", description = "Retorna la lista de productos obtenidos desde el microservicio de productos")
    @ApiResponse(responseCode = "200", description = "Lista de productos")
    @GetMapping("/productos")
    public ResponseEntity<List<ProductoResponse>> obtenerProductos() {
        log.info("GET /api/inventarios/productos");
        return ResponseEntity.ok(inventarioService.obtenerProductos());
    }
}
