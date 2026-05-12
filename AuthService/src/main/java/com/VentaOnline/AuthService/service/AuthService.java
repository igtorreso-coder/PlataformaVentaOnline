package com.VentaOnline.AuthService.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.VentaOnline.AuthService.client.UserServiceClient;
import com.VentaOnline.AuthService.dto.AuthUserResponseDTO;
import com.VentaOnline.AuthService.dto.LoginRequestDTO;
import com.VentaOnline.AuthService.dto.LoginResponseDTO;
import com.VentaOnline.AuthService.dto.RegistroRequestDTO;
import com.VentaOnline.AuthService.dto.ValidarTokenResponseDTO;
import com.VentaOnline.AuthService.model.AuthUser;
import com.VentaOnline.AuthService.model.LoginToken;
import com.VentaOnline.AuthService.repository.AuthUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Slf4j
public class AuthService {

    @Autowired
    private AuthUserRepository authUserRepository;
    @Autowired
    private TokenService tokenService;
    @Autowired
    private UserServiceClient userServiceClient;

    public LoginResponseDTO registrar(RegistroRequestDTO request) {
        log.info("Registrando nuevo usuario: {}", request.getCorreo());
        if (authUserRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("El correo '" + request.getCorreo() + "' ya está registrado");
        }

        String hash = hashContrasena(request.getContrasena());

        AuthUser user = AuthUser.builder()
                .correo(request.getCorreo())
                .contrasena(hash)
                .nombreCompleto(request.getNombreCompleto())
                .build();
        user = authUserRepository.save(user);

        userServiceClient.sincronizarUsuario(user.getId(), user.getNombreCompleto(),
                user.getCorreo());

        LoginToken token = tokenService.generarToken(user);
        log.info("Usuario registrado exitosamente con ID: {}", user.getId());
        return toLoginResponse(user, token);
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        log.info("Intento de login: {}", request.getCorreo());
        AuthUser user = authUserRepository.findByCorreo(request.getCorreo())
                .orElseThrow(() -> new NoSuchElementException("Credenciales inválidas"));

        if (!user.getActivo()) {
            throw new IllegalArgumentException("La cuenta está bloqueada");
        }

        if (!verificarContrasena(request.getContrasena(), user.getContrasena())) {
            throw new IllegalArgumentException("Credenciales inválidas");
        }

        user.setUltimoAcceso(LocalDateTime.now());
        authUserRepository.save(user);

        LoginToken token = tokenService.generarToken(user);
        log.info("Login exitoso para usuario ID: {}", user.getId());
        return toLoginResponse(user, token);
    }

    public ValidarTokenResponseDTO validarToken(String tokenStr) {
        log.info("Validando token");
        LoginToken token = tokenService.validarToken(tokenStr);
        if (token == null) {
            return ValidarTokenResponseDTO.builder()
                    .valido(false)
                    .mensaje("Token inválido o expirado")
                    .build();
        }
        return ValidarTokenResponseDTO.builder()
                .valido(true)
                .usuarioId(token.getUsuario().getId())
                .correo(token.getUsuario().getCorreo())
                .rol(token.getUsuario().getRol())
                .mensaje("Token válido")
                .build();
    }

    public List<AuthUserResponseDTO> obtenerTodos() {
        log.info("Obteniendo todos los usuarios auth");
        return authUserRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public AuthUserResponseDTO obtenerById(Long id) {
        log.info("Obteniendo usuario auth con ID: {}", id);
        return authUserRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Usuario auth no encontrado con ID: " + id));
    }

    public AuthUserResponseDTO actualizarRol(Long id, String nuevoRol) {
        log.info("Actualizando rol del usuario ID: {} a {}", id, nuevoRol);
        if (!List.of("USER", "ADMIN").contains(nuevoRol.toUpperCase())) {
            throw new IllegalArgumentException("Rol inválido: " + nuevoRol + ". Valores permitidos: USER, ADMIN");
        }
        AuthUser user = authUserRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario auth no encontrado con ID: " + id));
        user.setRol(nuevoRol.toUpperCase());
        return toResponse(authUserRepository.save(user));
    }

    public AuthUserResponseDTO cambiarEstado(Long id) {
        log.info("Cambiando estado del usuario ID: {}", id);
        AuthUser user = authUserRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario auth no encontrado con ID: " + id));
        user.setActivo(!user.getActivo());
        return toResponse(authUserRepository.save(user));
    }

    private String hashContrasena(String contrasena) {
        StringBuilder hash = new StringBuilder();
        for (int i = 0; i < contrasena.length(); i++) {
            hash.append((int) contrasena.charAt(i) * 7 + 13).append("x");
        }
        return "HASH_" + hash.toString();
    }

    private boolean verificarContrasena(String raw, String hash) {
        return hashContrasena(raw).equals(hash);
    }

    private LoginResponseDTO toLoginResponse(AuthUser user, LoginToken token) {
        return LoginResponseDTO.builder()
                .token(token.getToken())
                .usuarioId(user.getId())
                .nombreCompleto(user.getNombreCompleto())
                .correo(user.getCorreo())
                .rol(user.getRol())
                .build();
    }

    private AuthUserResponseDTO toResponse(AuthUser user) {
        return AuthUserResponseDTO.builder()
                .id(user.getId())
                .correo(user.getCorreo())
                .nombreCompleto(user.getNombreCompleto())
                .rol(user.getRol())
                .activo(user.getActivo())
                .ultimoAcceso(user.getUltimoAcceso())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
