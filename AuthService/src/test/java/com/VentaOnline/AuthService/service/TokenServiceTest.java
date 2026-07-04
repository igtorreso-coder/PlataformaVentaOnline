package com.VentaOnline.AuthService.service;

import com.VentaOnline.AuthService.model.AuthUser;
import com.VentaOnline.AuthService.model.LoginToken;
import com.VentaOnline.AuthService.repository.LoginTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private LoginTokenRepository loginTokenRepository;

    @InjectMocks
    private TokenService tokenService;

    private AuthUser testUser;
    private LoginToken testToken;

    @BeforeEach
    void setUp() {
        testUser = AuthUser.builder()
                .id(1L)
                .nombreCompleto("Juan Perez")
                .correo("juan@test.com")
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
    void generarToken_deberiaCrearYRetornarToken() {
        when(loginTokenRepository.save(any(LoginToken.class))).thenReturn(testToken);

        LoginToken result = tokenService.generarToken(testUser);

        assertNotNull(result);
        assertEquals(testToken.getToken(), result.getToken());
        assertEquals(testUser, result.getUsuario());
        assertTrue(result.getExpiracion().isAfter(LocalDateTime.now()));
        verify(loginTokenRepository).save(any(LoginToken.class));
    }

    @Test
    void validarToken_deberiaRetornarTokenCuandoValido() {
        when(loginTokenRepository.findByTokenAndActivoTrue("valid-token"))
                .thenReturn(Optional.of(testToken));

        LoginToken result = tokenService.validarToken("valid-token");

        assertNotNull(result);
        assertEquals(testToken.getToken(), result.getToken());
    }

    @Test
    void validarToken_deberiaRetornarNullCuandoTokenExpirado() {
        testToken.setExpiracion(LocalDateTime.now().minusHours(1));
        when(loginTokenRepository.findByTokenAndActivoTrue("expired-token"))
                .thenReturn(Optional.of(testToken));

        LoginToken result = tokenService.validarToken("expired-token");

        assertNull(result);
    }

    @Test
    void validarToken_deberiaRetornarNullCuandoNoExiste() {
        when(loginTokenRepository.findByTokenAndActivoTrue("unknown-token"))
                .thenReturn(Optional.empty());

        LoginToken result = tokenService.validarToken("unknown-token");

        assertNull(result);
    }

    @Test
    void invalidarToken_deberiaDesactivarToken() {
        when(loginTokenRepository.findByTokenAndActivoTrue("valid-token"))
                .thenReturn(Optional.of(testToken));

        tokenService.invalidarToken("valid-token");

        assertFalse(testToken.getActivo());
        verify(loginTokenRepository).save(testToken);
    }

    @Test
    void invalidarToken_noDeberiaHacerNadaCuandoTokenNoExiste() {
        when(loginTokenRepository.findByTokenAndActivoTrue("unknown-token"))
                .thenReturn(Optional.empty());

        tokenService.invalidarToken("unknown-token");

        verify(loginTokenRepository, never()).save(any());
    }
}
