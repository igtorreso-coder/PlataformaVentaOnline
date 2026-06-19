package com.VentaOnline.CartService.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.VentaOnline.CartService.dto.CarritoItemRequestDTO;
import com.VentaOnline.CartService.dto.CarritoItemUpdateRequestDTO;
import com.VentaOnline.CartService.dto.CarritoRequestDTO;
import com.VentaOnline.CartService.dto.CarritoResponseDTO;
import com.VentaOnline.CartService.service.CarritoService;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/carritos")
@Tag(name = "Carrito de Compras", description = "Endpoints para gestión del carrito de compras")
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @Operation(summary = "Crear carrito", description = "Crea un nuevo carrito de compras para un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Carrito creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del carrito inválidos")
    })
    @PostMapping
    public ResponseEntity<CarritoResponseDTO> crearCarrito(@Valid @RequestBody CarritoRequestDTO request) {
        CarritoResponseDTO response = carritoService.crearCarrito(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener carrito por ID", description = "Retorna los detalles de un carrito por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Carrito encontrado"),
        @ApiResponse(responseCode = "404", description = "Carrito no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CarritoResponseDTO> obtenerCarritoById(@PathVariable Long id) {
        CarritoResponseDTO response = carritoService.obtenerCarritoById(id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Obtener carrito activo por usuario", description = "Retorna el carrito activo de un usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Carrito activo encontrado"),
        @ApiResponse(responseCode = "404", description = "No hay carrito activo para el usuario")
    })
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<CarritoResponseDTO> obtenerCarritoActivoByUsuario(@PathVariable Long usuarioId) {
        CarritoResponseDTO response = carritoService.obtenerCarritoActivoByUsuario(usuarioId);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Agregar item al carrito", description = "Agrega un producto al carrito de compras")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Item agregado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del item inválidos o carrito no activo"),
        @ApiResponse(responseCode = "404", description = "Carrito no encontrado")
    })
    @PostMapping("/{id}/items")
    public ResponseEntity<CarritoResponseDTO> agregarItem(
            @PathVariable Long id,
            @Valid @RequestBody CarritoItemRequestDTO request) {
        CarritoResponseDTO response = carritoService.agregarItem(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Actualizar item del carrito", description = "Actualiza la cantidad de un item en el carrito")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Item actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Carrito o item no encontrado")
    })
    @PutMapping("/{id}/items/{itemId}")
    public ResponseEntity<CarritoResponseDTO> actualizarItem(
            @PathVariable Long id,
            @PathVariable Long itemId,
            @Valid @RequestBody CarritoItemUpdateRequestDTO request) {
        CarritoResponseDTO response = carritoService.actualizarItem(id, itemId, request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar item del carrito", description = "Elimina un producto del carrito de compras")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Item eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Carrito o item no encontrado")
    })
    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long id, @PathVariable Long itemId) {
        carritoService.eliminarItem(id, itemId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Eliminar carrito", description = "Elimina un carrito de compras por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Carrito eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Carrito no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCarrito(@PathVariable Long id) {
        carritoService.eliminarCarrito(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Finalizar carrito", description = "Cambia el estado del carrito a COMPLETADO (checkout)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Carrito finalizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "El carrito no está activo"),
        @ApiResponse(responseCode = "404", description = "Carrito no encontrado")
    })
    @PostMapping("/{id}/checkout")
    public ResponseEntity<CarritoResponseDTO> finalizarCarrito(@PathVariable Long id) {
        CarritoResponseDTO response = carritoService.finalizarCarrito(id);
        return ResponseEntity.ok(response);
    }
}
