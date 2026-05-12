package com.VentaOnline.CartService.controller;

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
public class CarritoController {

    @Autowired
    private CarritoService carritoService;

    @PostMapping
    public ResponseEntity<CarritoResponseDTO> crearCarrito(@Valid @RequestBody CarritoRequestDTO request) {
        CarritoResponseDTO response = carritoService.crearCarrito(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarritoResponseDTO> obtenerCarritoById(@PathVariable Long id) {
        CarritoResponseDTO response = carritoService.obtenerCarritoById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<CarritoResponseDTO> obtenerCarritoActivoByUsuario(@PathVariable Long usuarioId) {
        CarritoResponseDTO response = carritoService.obtenerCarritoActivoByUsuario(usuarioId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<CarritoResponseDTO> agregarItem(
            @PathVariable Long id,
            @Valid @RequestBody CarritoItemRequestDTO request) {
        CarritoResponseDTO response = carritoService.agregarItem(id, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}/items/{itemId}")
    public ResponseEntity<CarritoResponseDTO> actualizarItem(
            @PathVariable Long id,
            @PathVariable Long itemId,
            @Valid @RequestBody CarritoItemUpdateRequestDTO request) {
        CarritoResponseDTO response = carritoService.actualizarItem(id, itemId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/items/{itemId}")
    public ResponseEntity<Void> eliminarItem(@PathVariable Long id, @PathVariable Long itemId) {
        carritoService.eliminarItem(id, itemId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCarrito(@PathVariable Long id) {
        carritoService.eliminarCarrito(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/checkout")
    public ResponseEntity<CarritoResponseDTO> finalizarCarrito(@PathVariable Long id) {
        CarritoResponseDTO response = carritoService.finalizarCarrito(id);
        return ResponseEntity.ok(response);
    }
}
