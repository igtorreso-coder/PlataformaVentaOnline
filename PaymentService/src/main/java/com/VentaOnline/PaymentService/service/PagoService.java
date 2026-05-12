package com.VentaOnline.PaymentService.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.VentaOnline.PaymentService.client.PedidoClient;
import com.VentaOnline.PaymentService.dto.PagoRequestDTO;
import com.VentaOnline.PaymentService.dto.PagoResponseDTO;
import com.VentaOnline.PaymentService.dto.PedidoResponse;
import com.VentaOnline.PaymentService.model.Pago;
import com.VentaOnline.PaymentService.repository.PagoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class PagoService {

    private final PagoRepository pagoRepository;
    private final PedidoClient pedidoClient;

    @Transactional
    public PagoResponseDTO crearPago(PagoRequestDTO request) {
        log.info("Creando pago para pedido ID: {}, monto: {}, método: {}",
                request.getPedidoId(), request.getMonto(), request.getMetodoPago());

        PedidoResponse pedido = pedidoClient.getPedidoById(request.getPedidoId());

        if (!"PENDIENTE".equalsIgnoreCase(pedido.getEstado())) {
            throw new IllegalArgumentException("El pedido ID " + request.getPedidoId()
                    + " no está pendiente. Estado actual: " + pedido.getEstado());
        }

        Pago pago = Pago.builder()
                .pedidoId(request.getPedidoId())
                .monto(request.getMonto())
                .metodoPago(request.getMetodoPago().toUpperCase())
                .build();
        pago = pagoRepository.save(pago);
        log.info("Pago creado con ID: {} para pedido ID: {}", pago.getId(), pago.getPedidoId());
        return toResponse(pago);
    }

    @Transactional(readOnly = true)
    public List<PagoResponseDTO> obtenerTodosPagos() {
        log.info("Obteniendo todos los pagos");
        return pagoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public PagoResponseDTO obtenerPagoById(Long id) {
        log.info("Obteniendo pago con ID: {}", id);
        return pagoRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Pago no encontrado con ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<PagoResponseDTO> obtenerPagosByPedido(Long pedidoId) {
        log.info("Obteniendo pagos para pedido ID: {}", pedidoId);
        return pagoRepository.findByPedidoIdOrderByCreatedAtDesc(pedidoId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public PagoResponseDTO procesarPago(Long id) {
        log.info("Procesando pago con ID: {}", id);
        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pago no encontrado con ID: " + id));

        if (!"PENDIENTE".equals(pago.getEstado())) {
            throw new IllegalArgumentException("El pago ID " + id + " no está pendiente. Estado actual: " + pago.getEstado());
        }

        pago.setEstado("COMPLETADO");
        pago.setFechaPago(LocalDateTime.now());
        pago.setReferencia("PAY-" + System.currentTimeMillis());
        pago = pagoRepository.save(pago);
        log.info("Pago ID: {} procesado exitosamente. Referencia: {}", id, pago.getReferencia());
        return toResponse(pago);
    }

    @Transactional
    public PagoResponseDTO actualizarEstadoPago(Long id, String nuevoEstado) {
        log.info("Actualizando estado del pago ID: {} a {}", id, nuevoEstado);
        List<String> estadosValidos = List.of("PENDIENTE", "COMPLETADO", "FALLIDO", "REEMBOLSADO");
        if (!estadosValidos.contains(nuevoEstado.toUpperCase())) {
            throw new IllegalArgumentException("Estado inválido: " + nuevoEstado
                    + ". Valores permitidos: " + estadosValidos);
        }

        Pago pago = pagoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pago no encontrado con ID: " + id));

        pago.setEstado(nuevoEstado.toUpperCase());
        if ("COMPLETADO".equals(nuevoEstado.toUpperCase()) && pago.getFechaPago() == null) {
            pago.setFechaPago(LocalDateTime.now());
        }
        pago = pagoRepository.save(pago);
        log.info("Estado del pago ID: {} actualizado a {}", id, pago.getEstado());
        return toResponse(pago);
    }

    @Transactional
    public void eliminarPago(Long id) {
        log.info("Eliminando pago con ID: {}", id);
        if (!pagoRepository.existsById(id)) {
            throw new NoSuchElementException("Pago no encontrado con ID: " + id);
        }
        pagoRepository.deleteById(id);
    }

    private PagoResponseDTO toResponse(Pago pago) {
        return PagoResponseDTO.builder()
                .id(pago.getId())
                .pedidoId(pago.getPedidoId())
                .monto(pago.getMonto())
                .metodoPago(pago.getMetodoPago())
                .estado(pago.getEstado())
                .referencia(pago.getReferencia())
                .fechaPago(pago.getFechaPago())
                .createdAt(pago.getCreatedAt())
                .updatedAt(pago.getUpdatedAt())
                .build();
    }
}
