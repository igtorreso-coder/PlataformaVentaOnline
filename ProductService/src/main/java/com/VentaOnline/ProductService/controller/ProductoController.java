package com.VentaOnline.ProductService.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.VentaOnline.ProductService.dto.CategoriaResponse;
import com.VentaOnline.ProductService.dto.ProductoRequestDTO;
import com.VentaOnline.ProductService.dto.ProductoResponseDTO;
import com.VentaOnline.ProductService.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/productos")
@Slf4j
@Tag(name = "Productos", description = "Endpoints para gestión de productos del catálogo")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @Operation(summary = "Obtener todos los productos", description = "Retorna la lista completa de productos del catálogo")
    @ApiResponse(responseCode = "200", description = "Lista de productos")
    @GetMapping
    public ResponseEntity<List<ProductoResponseDTO>> obtenerProductos() {
        log.info("GET /api/productos");
        return ResponseEntity.ok(productoService.obtenerTodosProductos());
    }

    @Operation(summary = "Obtener producto por ID", description = "Retorna los detalles de un producto por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> obtenerProductoPorId(@PathVariable Long id) {
        log.info("GET /api/productos/{}", id);
        return ResponseEntity.ok(productoService.obtenerProductoPorId(id));
    }

    @Operation(summary = "Obtener productos por categoría", description = "Retorna los productos filtrados por ID de categoría")
    @ApiResponse(responseCode = "200", description = "Lista de productos de la categoría")
    @GetMapping("/categoria/{categoriaId}")
    public ResponseEntity<List<ProductoResponseDTO>> obtenerProductosPorCategoria(@PathVariable Long categoriaId) {
        log.info("GET /api/productos/categoria/{}", categoriaId);
        return ResponseEntity.ok(productoService.obtenerProductosPorCategoria(categoriaId));
    }

    @Operation(summary = "Crear nuevo producto", description = "Crea un nuevo producto en el catálogo")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Producto creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de producto inválidos")
    })
    @PostMapping
    public ResponseEntity<ProductoResponseDTO> crearProducto(@Valid @RequestBody ProductoRequestDTO request) {
        log.info("POST /api/productos");
        ProductoResponseDTO producto = productoService.crearProducto(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(producto.getId()).toUri();
        return ResponseEntity.created(location).body(producto);
    }

    @Operation(summary = "Actualizar producto", description = "Actualiza los datos de un producto existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductoResponseDTO> actualizarProducto(@PathVariable Long id,
            @Valid @RequestBody ProductoRequestDTO request) {
        log.info("PUT /api/productos/{}", id);
        return ResponseEntity.ok(productoService.actualizarProducto(id, request));
    }

    @Operation(summary = "Eliminar producto", description = "Elimina un producto del catálogo por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Producto eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        log.info("DELETE /api/productos/{}", id);
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener categorías", description = "Retorna la lista de categorías obtenidas desde el microservicio de categorías")
    @ApiResponse(responseCode = "200", description = "Lista de categorías")
    @GetMapping("/categorias")
    public ResponseEntity<List<CategoriaResponse>> obtenerCategorias() {
        log.info("GET /api/productos/categorias");
        return ResponseEntity.ok(productoService.obtenerCategorias());
    }
}
