package com.VentaOnline.InventoryService.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.VentaOnline.InventoryService.client.ProductoClient;
import com.VentaOnline.InventoryService.dto.InventarioRequestDTO;
import com.VentaOnline.InventoryService.dto.InventarioResponseDTO;
import com.VentaOnline.InventoryService.dto.ProductoResponse;
import com.VentaOnline.InventoryService.model.Inventario;
import com.VentaOnline.InventoryService.repository.InventarioRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class InventarioService {
    private final InventarioRepository inventarioRepository;
    private final ProductoClient productoClient;

    public List<InventarioResponseDTO> obtenerTodosMovimientos() {
        log.info("Obteniendo todos los movimientos de inventario");
        return inventarioRepository.findAll().stream()
                .map(mov -> toResponse(mov, obtenerNombreProducto(mov.getProductoId())))
                .toList();
    }

    public InventarioResponseDTO obtenerMovimientoById(Long id) {
        log.info("Obteniendo movimiento de inventario con ID: {}", id);
        return inventarioRepository.findById(id)
                .map(mov -> toResponse(mov, obtenerNombreProducto(mov.getProductoId())))
                .orElseThrow(() -> new NoSuchElementException("Movimiento de inventario no encontrado con ID: " + id));
    }

    public List<InventarioResponseDTO> obtenerMovimientosByProducto(Long productoId) {
        log.info("Obteniendo movimientos de inventario para producto ID: {}", productoId);
        return inventarioRepository.findByProductoIdOrderByCreatedAtDesc(productoId).stream()
                .map(mov -> toResponse(mov, obtenerNombreProducto(mov.getProductoId())))
                .toList();
    }

    public InventarioResponseDTO crearMovimiento(InventarioRequestDTO request) {
        log.info("Registrando movimiento de inventario - producto: {}, tipo: {}, cantidad: {}",
                request.getProductoId(), request.getTipo(), request.getCantidad());
        if (!List.of("ENTRADA", "SALIDA", "AJUSTE").contains(request.getTipo().toUpperCase())) {
            throw new IllegalArgumentException("Tipo de movimiento inválido: " + request.getTipo()
                    + ". Valores permitidos: ENTRADA, SALIDA, AJUSTE");
        }
        ProductoResponse producto = productoClient.getProductoById(request.getProductoId());
        Inventario inventario = Inventario.builder()
                .productoId(request.getProductoId())
                .tipo(request.getTipo().toUpperCase())
                .cantidad(request.getCantidad())
                .observacion(request.getObservacion())
                .build();
        inventario = inventarioRepository.save(inventario);
        return toResponse(inventario, producto.getNombre());
    }

    public Integer obtenerStockByProducto(Long productoId) {
        log.info("Calculando stock para producto ID: {}", productoId);
        List<Inventario> movimientos = inventarioRepository.findByProductoIdOrderByCreatedAtDesc(productoId);
        if (movimientos.isEmpty()) {
            return 0;
        }
        int stock = 0;
        for (Inventario mov : movimientos) {
            switch (mov.getTipo()) {
                case "ENTRADA" -> stock += mov.getCantidad();
                case "SALIDA" -> stock -= mov.getCantidad();
                case "AJUSTE" -> stock = mov.getCantidad();
            }
        }
        return Math.max(stock, 0);
    }

    public List<ProductoResponse> obtenerProductos() {
        log.info("Consultando microservicio de productos desde inventario");
        return productoClient.getProductos();
    }

    private String obtenerNombreProducto(Long productoId) {
        try {
            ProductoResponse producto = productoClient.getProductoById(productoId);
            return producto.getNombre();
        } catch (RuntimeException e) {
            log.warn("No se pudo obtener el nombre del producto ID {}: {}", productoId, e.getMessage());
            return "Producto ID: " + productoId;
        }
    }

    private InventarioResponseDTO toResponse(Inventario inventario, String nombreProducto) {
        return InventarioResponseDTO.builder()
                .id(inventario.getId())
                .productoId(inventario.getProductoId())
                .nombreProducto(nombreProducto)
                .tipo(inventario.getTipo())
                .cantidad(inventario.getCantidad())
                .observacion(inventario.getObservacion())
                .createdAt(inventario.getCreatedAt())
                .updatedAt(inventario.getUpdatedAt())
                .build();
    }
}
