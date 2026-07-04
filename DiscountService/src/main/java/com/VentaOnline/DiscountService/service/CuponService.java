package com.VentaOnline.DiscountService.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.VentaOnline.DiscountService.dto.CuponRequestDTO;
import com.VentaOnline.DiscountService.dto.CuponResponseDTO;
import com.VentaOnline.DiscountService.dto.ValidarCuponResponse;
import com.VentaOnline.DiscountService.model.Cupon;
import com.VentaOnline.DiscountService.repository.CuponRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Slf4j
public class CuponService {

    @Autowired
    private CuponRepository cuponRepository;

    @Transactional
    public CuponResponseDTO crearCupon(CuponRequestDTO request) {
        log.info("Creando nuevo cupón: {}", request.getCodigo());
        if (cuponRepository.existsByCodigo(request.getCodigo())) {
            throw new IllegalArgumentException("El código '" + request.getCodigo() + "' ya está registrado");
        }
        if (!List.of("PORCENTAJE", "FIJO").contains(request.getTipo().toUpperCase())) {
            throw new IllegalArgumentException("Tipo inválido: " + request.getTipo()
                    + ". Valores permitidos: PORCENTAJE, FIJO");
        }
        Cupon cupon = Cupon.builder()
                .codigo(request.getCodigo().toUpperCase())
                .tipo(request.getTipo().toUpperCase())
                .valor(request.getValor())
                .montoMinimo(request.getMontoMinimo())
                .usosMaximos(request.getUsosMaximos())
                .fechaExpiracion(request.getFechaExpiracion())
                .activo(request.getActivo() != null ? request.getActivo() : true)
                .build();
        cupon = cuponRepository.save(cupon);
        return toResponse(cupon);
    }

    public List<CuponResponseDTO> obtenerTodosCupones() {
        log.info("Obteniendo todos los cupones");
        return cuponRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public CuponResponseDTO obtenerCuponPorId(Long id) {
        log.info("Obteniendo cupón con ID: {}", id);
        return cuponRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Cupón no encontrado con ID: " + id));
    }

    public CuponResponseDTO obtenerCuponPorCodigo(String codigo) {
        log.info("Obteniendo cupón por código: {}", codigo);
        return cuponRepository.findByCodigo(codigo.toUpperCase())
                .map(this::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Cupón no encontrado con código: " + codigo));
    }

    @Transactional
    public CuponResponseDTO actualizarCupon(Long id, CuponRequestDTO request) {
        log.info("Actualizando cupón con ID: {}", id);
        Cupon existing = cuponRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cupón no encontrado con ID: " + id));
        if (!existing.getCodigo().equals(request.getCodigo().toUpperCase())
                && cuponRepository.existsByCodigo(request.getCodigo())) {
            throw new IllegalArgumentException("El código '" + request.getCodigo() + "' ya está registrado");
        }
        if (!List.of("PORCENTAJE", "FIJO").contains(request.getTipo().toUpperCase())) {
            throw new IllegalArgumentException("Tipo inválido: " + request.getTipo()
                    + ". Valores permitidos: PORCENTAJE, FIJO");
        }
        existing.setCodigo(request.getCodigo().toUpperCase());
        existing.setTipo(request.getTipo().toUpperCase());
        existing.setValor(request.getValor());
        existing.setMontoMinimo(request.getMontoMinimo());
        existing.setUsosMaximos(request.getUsosMaximos());
        existing.setFechaExpiracion(request.getFechaExpiracion());
        existing.setActivo(request.getActivo() != null ? request.getActivo() : existing.getActivo());
        return toResponse(cuponRepository.save(existing));
    }

    @Transactional
    public void eliminarCupon(Long id) {
        log.info("Eliminando cupón con ID: {}", id);
        if (!cuponRepository.existsById(id)) {
            throw new NoSuchElementException("Cupón no encontrado con ID: " + id);
        }
        cuponRepository.deleteById(id);
    }

    public ValidarCuponResponse validarCupon(Long id) {
        log.info("Validando cupón con ID: {}", id);
        Cupon cupon = cuponRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cupón no encontrado con ID: " + id));

        if (!cupon.getActivo()) {
            return ValidarCuponResponse.builder()
                    .valido(false)
                    .mensaje("El cupón no está activo")
                    .cupon(toResponse(cupon))
                    .build();
        }

        if (cupon.getFechaExpiracion() != null && cupon.getFechaExpiracion().isBefore(LocalDateTime.now())) {
            return ValidarCuponResponse.builder()
                    .valido(false)
                    .mensaje("El cupón ha expirado")
                    .cupon(toResponse(cupon))
                    .build();
        }

        if (cupon.getUsosActuales() >= cupon.getUsosMaximos()) {
            return ValidarCuponResponse.builder()
                    .valido(false)
                    .mensaje("El cupón ha alcanzado el límite de usos")
                    .cupon(toResponse(cupon))
                    .build();
        }

        return ValidarCuponResponse.builder()
                .valido(true)
                .mensaje("Cupón válido")
                .cupon(toResponse(cupon))
                .build();
    }

    @Transactional
    public CuponResponseDTO usarCupon(Long id) {
        log.info("Usando cupón con ID: {}", id);
        Cupon cupon = cuponRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cupón no encontrado con ID: " + id));

        ValidarCuponResponse validacion = validarCupon(id);
        if (!validacion.isValido()) {
            throw new IllegalArgumentException(validacion.getMensaje());
        }

        cupon.setUsosActuales(cupon.getUsosActuales() + 1);
        cupon = cuponRepository.save(cupon);
        log.info("Cupón ID: {} usado. Usos actuales: {}/{}", id, cupon.getUsosActuales(), cupon.getUsosMaximos());
        return toResponse(cupon);
    }

    private CuponResponseDTO toResponse(Cupon cupon) {
        return CuponResponseDTO.builder()
                .id(cupon.getId())
                .codigo(cupon.getCodigo())
                .tipo(cupon.getTipo())
                .valor(cupon.getValor())
                .montoMinimo(cupon.getMontoMinimo())
                .usosMaximos(cupon.getUsosMaximos())
                .usosActuales(cupon.getUsosActuales())
                .fechaExpiracion(cupon.getFechaExpiracion())
                .activo(cupon.getActivo())
                .createdAt(cupon.getCreatedAt())
                .updatedAt(cupon.getUpdatedAt())
                .build();
    }
}
