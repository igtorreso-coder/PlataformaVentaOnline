package com.VentaOnline.ShippingService.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.VentaOnline.ShippingService.model.Envio;

@Repository
public interface EnvioRepository extends JpaRepository<Envio, Long> {
    List<Envio> findByPedidoIdOrderByCreatedAtDesc(Long pedidoId);
}
