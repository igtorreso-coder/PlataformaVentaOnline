package com.VentaOnline.DiscountService.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.VentaOnline.DiscountService.model.Cupon;

@Repository
public interface CuponRepository extends JpaRepository<Cupon, Long> {
    Optional<Cupon> findByCodigo(String codigo);
    boolean existsByCodigo(String codigo);
}
