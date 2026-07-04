package com.VentaOnline.AuthService.service;

import com.VentaOnline.AuthService.client.UserServiceClient;
import com.VentaOnline.AuthService.dto.*;
import com.VentaOnline.AuthService.model.AuthUser;
import com.VentaOnline.AuthService.model.LoginToken;
import com.VentaOnline.AuthService.repository.AuthUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthUserRepository authUserRepository;

    @Mock
    private TokenService tokenService;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private AuthService authService;

    private BCryptPasswordEncoder encoder;
    private RegistroRequestDTO registroRequest;
    private LoginRequestDTO loginRequest;
    private AuthUser testUser;
    private LoginToken testToken;
    private final String rawPassword = "password123";

    @BeforeEach
    void setUp() {
        encoder = new BCryptPasswordEncoder();
        registroRequest = RegistroRequestDTO.builder()
                .nombreCompleto("Juan Perez")
                .correo("juan@test.com")
                .contrasena(rawPassword)
                .build();

        loginRequest = LoginRequestDTO.builder()
                .correo("juan@test.com")
                .contrasena(rawPassword)
                .build();

        testUser = AuthUser.builder()
                .id(1L)
                .nombreCompleto("Juan Perez")
                .correo("juan@test.com")
                .contrasena(encoder.encode(rawPassword))
                .rol("USER")
                .activo(true)
                .build();

        testToken = LoginToken.builder()
                .id(1L)
                .token("test-token-123")
                .usuario(testUser)
                .expiracion(LocalDateTime.now().plusHours(24))
                .activo(true)
                .build();
    }

    @Test
    void registrar_deberiaCrearUsuarioYRetornarToken() {
        when(authUserRepository.existsByCorreo(registroRequest.getCorreo())).thenReturn(false);
        when(authUserRepository.save(any(AuthUser.class))).thenReturn(testUser);
        when(tokenService.generarToken(any(AuthUser.class))).thenReturn(testToken);
        doNothing().when(userServiceClient).sincronizarUsuario(anyLong(), anyString(), anyString());

        LoginResponseDTO response = authService.registrar(registroRequest);

        assertNotNull(response);
        assertEquals(testToken.getToken(), response.getToken());
        assertEquals(testUser.getId(), response.getUsuarioId());
        assertEquals(testUser.getNombreCompleto(), response.getNombreCompleto());
        assertEquals(testUser.getCorreo(), response.getCorreo());
        assertEquals("USER", response.getRol());
        verify(authUserRepository).save(any(AuthUser.class));
        verify(tokenService).generarToken(any(AuthUser.class));
        verify(userServiceClient).sincronizarUsuario(anyLong(), anyString(), anyString());
    }

    @Test
    void registrar_deberiaLanzarExcepcionCuandoCorreoYaExiste() {
        when(authUserRepository.existsByCorreo(registroRequest.getCorreo())).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.registrar(registroRequest));
        assertTrue(ex.getMessage().contains("ya está registrado"));
        verify(authUserRepository, never()).save(any());
    }

    @Test
    void login_deberiaRetornarTokenCuandoCredencialesValidas() {
        when(authUserRepository.findByCorreo(loginRequest.getCorreo())).thenReturn(Optional.of(testUser));
        when(tokenService.generarToken(testUser)).thenReturn(testToken);

        LoginResponseDTO response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals(testToken.getToken(), response.getToken());
        assertEquals(testUser.getId(), response.getUsuarioId());
        verify(authUserRepository).save(any(AuthUser.class));
    }

    @Test
    void login_deberiaLanzarExcepcionCuandoCredencialesInvalidas() {
        when(authUserRepository.findByCorreo(loginRequest.getCorreo())).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> authService.login(loginRequest));
    }

    @Test
    void login_deberiaLanzarExcepcionCuandoCuentaBloqueada() {
        testUser.setActivo(false);
        when(authUserRepository.findByCorreo(loginRequest.getCorreo())).thenReturn(Optional.of(testUser));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.login(loginRequest));
        assertTrue(ex.getMessage().contains("bloqueada"));
    }

    @Test
    void login_deberiaLanzarExcepcionCuandoContrasenaIncorrecta() {
        loginRequest.setContrasena("wrongpassword");
        when(authUserRepository.findByCorreo(loginRequest.getCorreo())).thenReturn(Optional.of(testUser));

        assertThrows(IllegalArgumentException.class, () -> authService.login(loginRequest));
    }

    @Test
    void validarToken_deberiaRetornarValido() {
        when(tokenService.validarToken("valid-token")).thenReturn(testToken);

        ValidarTokenResponseDTO response = authService.validarToken("valid-token");

        assertTrue(response.isValido());
        assertEquals(testUser.getId(), response.getUsuarioId());
        assertEquals(testUser.getCorreo(), response.getCorreo());
    }

    @Test
    void validarToken_deberiaRetornarInvalido() {
        when(tokenService.validarToken("invalid-token")).thenReturn(null);

        ValidarTokenResponseDTO response = authService.validarToken("invalid-token");

        assertFalse(response.isValido());
    }

    @Test
    void obtenerTodos_deberiaRetornarListaDeUsuarios() {
        when(authUserRepository.findAll()).thenReturn(List.of(testUser));

        List<AuthUserResponseDTO> result = authService.obtenerTodos();

        assertEquals(1, result.size());
        assertEquals(testUser.getCorreo(), result.get(0).getCorreo());
    }

    @Test
    void obtenerById_deberiaRetornarUsuario() {
        when(authUserRepository.findById(1L)).thenReturn(Optional.of(testUser));

        AuthUserResponseDTO result = authService.obtenerPorId(1L);

        assertNotNull(result);
        assertEquals(testUser.getCorreo(), result.getCorreo());
    }

    @Test
    void obtenerById_deberiaLanzarExcepcionCuandoNoExiste() {
        when(authUserRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> authService.obtenerPorId(99L));
    }

    @Test
    void actualizarRol_deberiaActualizarCorrectamente() {
        when(authUserRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(authUserRepository.save(any(AuthUser.class))).thenReturn(testUser);

        AuthUserResponseDTO result = authService.actualizarRol(1L, "ADMIN");

        assertNotNull(result);
        verify(authUserRepository).save(any(AuthUser.class));
    }

    @Test
    void actualizarRol_deberiaLanzarExcepcionCuandoRolInvalido() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> authService.actualizarRol(1L, "INVALIDO"));
        assertTrue(ex.getMessage().contains("Rol inválido"));
        verify(authUserRepository, never()).save(any());
    }

    @Test
    void cambiarEstado_deberiaAlternarEstado() {
        when(authUserRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(authUserRepository.save(any(AuthUser.class))).thenReturn(testUser);

        AuthUserResponseDTO result = authService.cambiarEstado(1L);

        assertNotNull(result);
        verify(authUserRepository).save(any(AuthUser.class));
    }
}
