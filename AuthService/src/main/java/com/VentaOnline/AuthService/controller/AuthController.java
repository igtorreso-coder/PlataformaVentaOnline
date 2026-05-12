package com.VentaOnline.AuthService.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.VentaOnline.AuthService.dto.AuthUserResponseDTO;
import com.VentaOnline.AuthService.dto.LoginRequestDTO;
import com.VentaOnline.AuthService.dto.LoginResponseDTO;
import com.VentaOnline.AuthService.dto.RegistroRequestDTO;
import com.VentaOnline.AuthService.dto.RolRequestDTO;
import com.VentaOnline.AuthService.dto.ValidarTokenResponseDTO;
import com.VentaOnline.AuthService.service.AuthService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/registro")
    public ResponseEntity<LoginResponseDTO> registrar(@Valid @RequestBody RegistroRequestDTO request) {
        log.info("POST /api/auth/registro - {}", request.getCorreo());
        LoginResponseDTO response = authService.registrar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/usuarios/{id}").buildAndExpand(response.getUsuarioId()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        log.info("POST /api/auth/login - {}", request.getCorreo());
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/validar")
    public ResponseEntity<ValidarTokenResponseDTO> validarToken(@RequestParam String token) {
        log.info("GET /api/auth/validar");
        return ResponseEntity.ok(authService.validarToken(token));
    }

    @GetMapping("/usuarios")
    public ResponseEntity<List<AuthUserResponseDTO>> obtenerUsuarios() {
        log.info("GET /api/auth/usuarios");
        return ResponseEntity.ok(authService.obtenerTodos());
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<AuthUserResponseDTO> obtenerUsuario(@PathVariable Long id) {
        log.info("GET /api/auth/usuarios/{}", id);
        return ResponseEntity.ok(authService.obtenerById(id));
    }

    @PatchMapping("/usuarios/{id}/rol")
    public ResponseEntity<AuthUserResponseDTO> actualizarRol(@PathVariable Long id,
            @Valid @RequestBody RolRequestDTO request) {
        log.info("PATCH /api/auth/usuarios/{}/rol", id);
        return ResponseEntity.ok(authService.actualizarRol(id, request.getRol()));
    }

    @PostMapping("/usuarios/{id}/estado")
    public ResponseEntity<AuthUserResponseDTO> cambiarEstado(@PathVariable Long id) {
        log.info("POST /api/auth/usuarios/{}/estado", id);
        return ResponseEntity.ok(authService.cambiarEstado(id));
    }
}
