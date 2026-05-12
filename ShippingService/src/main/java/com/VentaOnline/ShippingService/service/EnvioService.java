package com.VentaOnline.ShippingService.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.VentaOnline.ShippingService.client.PedidoClient;
import com.VentaOnline.ShippingService.dto.EnvioRequestDTO;
import com.VentaOnline.ShippingService.dto.EnvioResponseDTO;
import com.VentaOnline.ShippingService.dto.PedidoResponse;
import com.VentaOnline.ShippingService.model.Envio;
import com.VentaOnline.ShippingService.repository.EnvioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Slf4j
public class EnvioService {

    @Autowired
    private EnvioRepository envioRepository;
    @Autowired
    private PedidoClient pedidoClient;

    @Transactional
    public EnvioResponseDTO crearEnvio(EnvioRequestDTO request) {
        log.info("Creando envío para pedido ID: {}", request.getPedidoId());

        PedidoResponse pedido = pedidoClient.getPedidoById(request.getPedidoId());

        if (!"CONFIRMADO".equalsIgnoreCase(pedido.getEstado())) {
            throw new IllegalArgumentException("El pedido ID " + request.getPedidoId()
                    + " no está confirmado. Estado actual: " + pedido.getEstado());
        }

        Envio envio = Envio.builder()
                .pedidoId(request.getPedidoId())
                .direccion(request.getDireccion())
                .ciudad(request.getCiudad())
                .build();
        envio = envioRepository.save(envio);
        log.info("Envío creado con ID: {} para pedido ID: {}", envio.getId(), envio.getPedidoId());
        return toResponse(envio);
    }

    @Transactional(readOnly = true)
    public List<EnvioResponseDTO> obtenerTodosEnvios() {
        log.info("Obteniendo todos los envíos");
        return envioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public EnvioResponseDTO obtenerEnvioById(Long id) {
        log.info("Obteniendo envío con ID: {}", id);
        return envioRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Envío no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<EnvioResponseDTO> obtenerEnviosByPedido(Long pedidoId) {
        log.info("Obteniendo envíos para pedido ID: {}", pedidoId);
        return envioRepository.findByPedidoIdOrderByCreatedAtDesc(pedidoId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public EnvioResponseDTO enviarEnvio(Long id) {
        log.info("Procesando envío con ID: {}", id);
        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Envío no encontrado con ID: " + id));

        if (!"PENDIENTE".equals(envio.getEstado())) {
            throw new IllegalArgumentException("El envío ID " + id + " no está pendiente. Estado actual: " + envio.getEstado());
        }

        envio.setEstado("ENVIADO");
        envio.setFechaEnvio(LocalDateTime.now());
        envio.setCodigoSeguimiento("TRACK-" + System.currentTimeMillis());
        envio = envioRepository.save(envio);
        log.info("Envío ID: {} enviado. Código de seguimiento: {}", id, envio.getCodigoSeguimiento());
        return toResponse(envio);
    }

    @Transactional
    public EnvioResponseDTO actualizarEstadoEnvio(Long id, String nuevoEstado) {
        log.info("Actualizando estado del envío ID: {} a {}", id, nuevoEstado);
        List<String> estadosValidos = List.of("PENDIENTE", "ENVIADO", "EN_TRANSITO", "ENTREGADO", "DEVUELTO");
        if (!estadosValidos.contains(nuevoEstado.toUpperCase())) {
            throw new IllegalArgumentException("Estado inválido: " + nuevoEstado
                    + ". Valores permitidos: " + estadosValidos);
        }

        Envio envio = envioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Envío no encontrado con ID: " + id));

        envio.setEstado(nuevoEstado.toUpperCase());
        if ("ENVIADO".equals(nuevoEstado.toUpperCase()) && envio.getFechaEnvio() == null) {
            envio.setFechaEnvio(LocalDateTime.now());
        }
        if ("ENTREGADO".equals(nuevoEstado.toUpperCase())) {
            envio.setFechaEntrega(LocalDateTime.now());
        }
        envio = envioRepository.save(envio);
        log.info("Estado del envío ID: {} actualizado a {}", id, envio.getEstado());
        return toResponse(envio);
    }

    @Transactional
    public void eliminarEnvio(Long id) {
        log.info("Eliminando envío con ID: {}", id);
        if (!envioRepository.existsById(id)) {
            throw new NoSuchElementException("Envío no encontrado con ID: " + id);
        }
        envioRepository.deleteById(id);
    }

    private EnvioResponseDTO toResponse(Envio envio) {
        return EnvioResponseDTO.builder()
                .id(envio.getId())
                .pedidoId(envio.getPedidoId())
                .direccion(envio.getDireccion())
                .ciudad(envio.getCiudad())
                .estado(envio.getEstado())
                .codigoSeguimiento(envio.getCodigoSeguimiento())
                .fechaEnvio(envio.getFechaEnvio())
                .fechaEntrega(envio.getFechaEntrega())
                .createdAt(envio.getCreatedAt())
                .updatedAt(envio.getUpdatedAt())
                .build();
    }
}
