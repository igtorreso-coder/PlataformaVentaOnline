package com.VentaOnline.AuthService.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.VentaOnline.AuthService.model.AuthUser;

@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {
    Optional<AuthUser> findByCorreo(String correo);
    boolean existsByCorreo(String correo);
}
