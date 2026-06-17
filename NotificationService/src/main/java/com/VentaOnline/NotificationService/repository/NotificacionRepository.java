package com.VentaOnline.NotificationService.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.VentaOnline.NotificationService.model.Notificacion;

@Repository
public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByUsuarioIdOrderByCreatedAtDesc(Long usuarioId);
    List<Notificacion> findByEstadoOrderByCreatedAtAsc(String estado);
}
