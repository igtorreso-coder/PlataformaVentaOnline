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
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/descuentos")
@Slf4j
public class CuponController {

    @Autowired
    private CuponService cuponService;

    @GetMapping
    public ResponseEntity<List<CuponResponseDTO>> obtenerCupones() {
        log.info("GET /api/descuentos");
        return ResponseEntity.ok(cuponService.obtenerTodosCupones());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuponResponseDTO> obtenerCuponById(@PathVariable Long id) {
        log.info("GET /api/descuentos/{}", id);
        return ResponseEntity.ok(cuponService.obtenerCuponById(id));
    }

    @GetMapping("/codigo/{codigo}")
    public ResponseEntity<CuponResponseDTO> obtenerCuponByCodigo(@PathVariable String codigo) {
        log.info("GET /api/descuentos/codigo/{}", codigo);
        return ResponseEntity.ok(cuponService.obtenerCuponByCodigo(codigo));
    }

    @PostMapping
    public ResponseEntity<CuponResponseDTO> crearCupon(@Valid @RequestBody CuponRequestDTO request) {
        log.info("POST /api/descuentos");
        CuponResponseDTO cupon = cuponService.crearCupon(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(cupon.getId()).toUri();
        return ResponseEntity.created(location).body(cupon);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CuponResponseDTO> actualizarCupon(@PathVariable Long id,
            @Valid @RequestBody CuponRequestDTO request) {
        log.info("PUT /api/descuentos/{}", id);
        return ResponseEntity.ok(cuponService.actualizarCupon(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCupon(@PathVariable Long id) {
        log.info("DELETE /api/descuentos/{}", id);
        cuponService.eliminarCupon(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/validar")
    public ResponseEntity<ValidarCuponResponse> validarCupon(@PathVariable Long id) {
        log.info("POST /api/descuentos/{}/validar", id);
        return ResponseEntity.ok(cuponService.validarCupon(id));
    }

    @PostMapping("/{id}/usar")
    public ResponseEntity<CuponResponseDTO> usarCupon(@PathVariable Long id) {
        log.info("POST /api/descuentos/{}/usar", id);
        return ResponseEntity.ok(cuponService.usarCupon(id));
    }
}
