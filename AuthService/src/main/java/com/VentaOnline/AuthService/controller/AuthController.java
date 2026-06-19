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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@Tag(name = "Autenticación", description = "Endpoints para registro, login, validación de tokens y gestión de usuarios de autenticación")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Operation(summary = "Registrar nuevo usuario", description = "Crea un nuevo usuario en el sistema de autenticación y retorna un token de acceso")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Usuario registrado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos o correo ya registrado")
    })
    @PostMapping("/registro")
    public ResponseEntity<LoginResponseDTO> registrar(@Valid @RequestBody RegistroRequestDTO request) {
        log.info("POST /api/auth/registro - {}", request.getCorreo());
        LoginResponseDTO response = authService.registrar(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/usuarios/{id}").buildAndExpand(response.getUsuarioId()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @Operation(summary = "Iniciar sesión", description = "Autentica un usuario con correo y contraseña, retornando un token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login exitoso"),
        @ApiResponse(responseCode = "400", description = "Credenciales inválidas o cuenta bloqueada")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO request) {
        log.info("POST /api/auth/login - {}", request.getCorreo());
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "Validar token", description = "Verifica si un token de acceso es válido y no ha expirado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Resultado de validación del token"),
        @ApiResponse(responseCode = "400", description = "Token inválido o expirado")
    })
    @GetMapping("/validar")
    public ResponseEntity<ValidarTokenResponseDTO> validarToken(@RequestParam String token) {
        log.info("GET /api/auth/validar");
        return ResponseEntity.ok(authService.validarToken(token));
    }

    @Operation(summary = "Obtener todos los usuarios auth", description = "Retorna la lista completa de usuarios registrados en el sistema de autenticación")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios auth")
    @GetMapping("/usuarios")
    public ResponseEntity<List<AuthUserResponseDTO>> obtenerUsuarios() {
        log.info("GET /api/auth/usuarios");
        return ResponseEntity.ok(authService.obtenerTodos());
    }

    @Operation(summary = "Obtener usuario auth por ID", description = "Retorna los detalles de un usuario de autenticación por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario auth encontrado"),
        @ApiResponse(responseCode = "404", description = "Usuario auth no encontrado")
    })
    @GetMapping("/usuarios/{id}")
    public ResponseEntity<AuthUserResponseDTO> obtenerUsuario(@PathVariable Long id) {
        log.info("GET /api/auth/usuarios/{}", id);
        return ResponseEntity.ok(authService.obtenerPorId(id));
    }

    @Operation(summary = "Actualizar rol de usuario", description = "Cambia el rol (USER/ADMIN) de un usuario de autenticación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rol actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Rol inválido"),
        @ApiResponse(responseCode = "404", description = "Usuario auth no encontrado")
    })
    @PatchMapping("/usuarios/{id}/rol")
    public ResponseEntity<AuthUserResponseDTO> actualizarRol(@PathVariable Long id,
            @Valid @RequestBody RolRequestDTO request) {
        log.info("PATCH /api/auth/usuarios/{}/rol", id);
        return ResponseEntity.ok(authService.actualizarRol(id, request.getRol()));
    }

    @Operation(summary = "Cambiar estado de usuario", description = "Alterna el estado activo/inactivo de un usuario de autenticación")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado actualizado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Usuario auth no encontrado")
    })
    @PostMapping("/usuarios/{id}/estado")
    public ResponseEntity<AuthUserResponseDTO> cambiarEstado(@PathVariable Long id) {
        log.info("POST /api/auth/usuarios/{}/estado", id);
        return ResponseEntity.ok(authService.cambiarEstado(id));
    }
}
