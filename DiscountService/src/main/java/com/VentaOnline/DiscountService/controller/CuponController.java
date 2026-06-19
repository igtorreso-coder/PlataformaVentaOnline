package com.VentaOnline.DiscountService.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.VentaOnline.DiscountService.dto.CuponRequestDTO;
import com.VentaOnline.DiscountService.dto.CuponResponseDTO;
import com.VentaOnline.DiscountService.dto.ValidarCuponResponse;
import com.VentaOnline.DiscountService.service.CuponService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/descuentos")
@Slf4j
@Tag(name = "Descuentos y Cupones", description = "Endpoints para gestión de cupones de descuento")
public class CuponController {

    @Autowired
    private CuponService cuponService;

    @Operation(summary = "Obtener todos los cupones", description = "Retorna la lista completa de cupones de descuento")
    @ApiResponse(responseCode = "200", description = "Lista de cupones")
    @GetMapping
    public ResponseEntity<List<CuponResponseDTO>> obtenerCupones() {
        log.info("GET /api/descuentos");
        return ResponseEntity.ok(cuponService.obtenerTodosCupones());
    }

    @Operation(summary = "Obtener cupón por ID", description = "Retorna los detalles de un cupón por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cupón encontrado"),
        @ApiResponse(responseCode = "404", description = "Cupón no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CuponResponseDTO> obtenerCuponPorId(@PathVariable Long id) {
        log.info("GET /api/descuentos/{}", id);
        return ResponseEntity.ok(cuponService.obtenerCuponPorId(id));
    }

    @Operation(summary = "Obtener cupón por código", description = "Retorna un cupón buscando por su código único")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cupón encontrado"),
        @ApiResponse(responseCode = "404", description = "Cupón no encontrado")
    })
    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CuponResponseDTO> obtenerCuponPorCodigo(@PathVariable String codigo) {
        log.info("GET /api/descuentos/codigo/{}", codigo);
        return ResponseEntity.ok(cuponService.obtenerCuponPorCodigo(codigo));
    }

    @Operation(summary = "Crear nuevo cupón", description = "Crea un nuevo cupón de descuento")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cupón creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos del cupón inválidos")
    })
    @PostMapping
    public ResponseEntity<CuponResponseDTO> crearCupon(@Valid @RequestBody CuponRequestDTO request) {
        log.info("POST /api/descuentos");
        CuponResponseDTO cupon = cuponService.crearCupon(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(cupon.getId()).toUri();
        return ResponseEntity.created(location).body(cupon);
    }

    @Operation(summary = "Actualizar cupón", description = "Actualiza los datos de un cupón existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cupón actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Cupón no encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CuponResponseDTO> actualizarCupon(@PathVariable Long id,
            @Valid @RequestBody CuponRequestDTO request) {
        log.info("PUT /api/descuentos/{}", id);
        return ResponseEntity.ok(cuponService.actualizarCupon(id, request));
    }

    @Operation(summary = "Eliminar cupón", description = "Elimina un cupón del sistema por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cupón eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cupón no encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCupon(@PathVariable Long id) {
        log.info("DELETE /api/descuentos/{}", id);
        cuponService.eliminarCupon(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Validar cupón", description = "Verifica si un cupón es válido (no expirado, con usos disponibles)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resultado de validación del cupón"),
        @ApiResponse(responseCode = "404", description = "Cupón no encontrado")
    })
    @PostMapping("/{id}/validar")
    public ResponseEntity<ValidarCuponResponse> validarCupon(@PathVariable Long id) {
        log.info("POST /api/descuentos/{}/validar", id);
        return ResponseEntity.ok(cuponService.validarCupon(id));
    }

    @Operation(summary = "Usar cupón", description = "Registra el uso de un cupón, decrementando sus usos disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cupón usado exitosamente"),
        @ApiResponse(responseCode = "400", description = "El cupón no puede ser usado"),
        @ApiResponse(responseCode = "404", description = "Cupón no encontrado")
    })
    @PostMapping("/{id}/usar")
    public ResponseEntity<CuponResponseDTO> usarCupon(@PathVariable Long id) {
        log.info("POST /api/descuentos/{}/usar", id);
        return ResponseEntity.ok(cuponService.usarCupon(id));
    }
}
