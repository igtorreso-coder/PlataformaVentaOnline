package com.VentaOnline.AuthService.service;

import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.stereotype.Service;
import com.VentaOnline.AuthService.model.AuthUser;
import com.VentaOnline.AuthService.model.LoginToken;
import com.VentaOnline.AuthService.repository.LoginTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class TokenService {

    private final LoginTokenRepository loginTokenRepository;

    public LoginToken generarToken(AuthUser usuario) {
        String tokenStr = UUID.randomUUID().toString().replace("-", "") + System.currentTimeMillis();
        LoginToken loginToken = LoginToken.builder()
                .token(tokenStr)
                .usuario(usuario)
                .expiracion(LocalDateTime.now().plusHours(24))
                .build();
        loginToken = loginTokenRepository.save(loginToken);
        log.info("Token generado para usuario ID: {}", usuario.getId());
        return loginToken;
    }

    public LoginToken validarToken(String tokenStr) {
        log.info("Validando token");
        return loginTokenRepository.findByTokenAndActivoTrue(tokenStr)
                .filter(t -> t.getExpiracion().isAfter(LocalDateTime.now()))
                .orElse(null);
    }

    public void invalidarToken(String tokenStr) {
        loginTokenRepository.findByTokenAndActivoTrue(tokenStr).ifPresent(token -> {
            token.setActivo(false);
            loginTokenRepository.save(token);
            log.info("Token invalidado");
        });
    }
}
