package com.VentaOnline.InventoryService.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.VentaOnline.InventoryService.model.Inventario;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    List<Inventario> findByProductoIdOrderByCreatedAtDesc(Long productoId);
}
