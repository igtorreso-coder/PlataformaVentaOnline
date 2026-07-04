package com.VentaOnline.AuthService.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.VentaOnline.AuthService.model.LoginToken;

@Repository
public interface LoginTokenRepository extends JpaRepository<LoginToken, Long> {
    Optional<LoginToken> findByTokenAndActivoTrue(String token);
    void deleteByExpiracionBefore(LocalDateTime fecha);
}
