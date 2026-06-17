package com.VentaOnline.OrderService.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.VentaOnline.OrderService.client.ProductoClient;
import com.VentaOnline.OrderService.client.UsuarioClient;
import com.VentaOnline.OrderService.dto.PedidoRequestDTO;
import com.VentaOnline.OrderService.dto.PedidoResponseDTO;
import com.VentaOnline.OrderService.dto.ProductoResponse;
import com.VentaOnline.OrderService.mapper.PedidoMapper;
import com.VentaOnline.OrderService.model.Pedido;
import com.VentaOnline.OrderService.model.PedidoDetalle;
import com.VentaOnline.OrderService.repository.PedidoRepository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Slf4j
public class PedidoService {
    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private PedidoMapper pedidoMapper;
    @Autowired
    private UsuarioClient usuarioClient;
    @Autowired
    private ProductoClient productoClient;

    @Transactional
    public PedidoResponseDTO crearPedido(PedidoRequestDTO request) {
        log.info("Creando pedido para usuario ID: {}", request.getUsuarioId());
        String nombreUsuario = getNombreUsuario(request.getUsuarioId());
        Pedido pedido = pedidoMapper.toEntity(request);
        BigDecimal total = BigDecimal.ZERO;
        for (PedidoDetalle detalle : pedido.getDetalles()) {
            ProductoResponse producto = productoClient.getProductoById(detalle.getProductoId());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(producto.getPrecio().multiply(BigDecimal.valueOf(detalle.getCantidad())));
            total = total.add(detalle.getSubtotal());
        }
        pedido.setTotal(total);
        pedido = pedidoRepository.save(pedido);
        log.info("Pedido creado con ID: {}, total: {}", pedido.getId(), pedido.getTotal());
        return pedidoMapper.toResponse(pedido, nombreUsuario);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> obtenerTodosPedidos() {
        log.info("Obteniendo todos los pedidos");
        List<Pedido> pedidos = pedidoRepository.findAll();
        return pedidos.stream()
                .map(p -> {
                    String nombreUsuario = getNombreUsuarioSafe(p.getUsuarioId());
                    Map<Long, String> nombresProductos = getNombresProductos(p);
                    return pedidoMapper.toResponse(p, nombreUsuario, nombresProductos);
                })
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO obtenerPedidoById(Long id) {
        log.info("Obteniendo pedido con ID: {}", id);
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pedido no encontrado con ID: " + id));
        String nombreUsuario = getNombreUsuario(pedido.getUsuarioId());
        Map<Long, String> nombresProductos = getNombresProductos(pedido);
        return pedidoMapper.toResponse(pedido, nombreUsuario, nombresProductos);
    }

    @Transactional(readOnly = true)
    public List<PedidoResponseDTO> obtenerPedidosByUsuario(Long usuarioId) {
        log.info("Obteniendo pedidos para usuario ID: {}", usuarioId);
        List<Pedido> pedidos = pedidoRepository.findByUsuarioIdOrderByCreatedAtDesc(usuarioId);
        if (pedidos.isEmpty()) {
            getNombreUsuario(usuarioId);
        }
        return pedidos.stream()
                .map(p -> {
                    String nombreUsuario = getNombreUsuarioSafe(p.getUsuarioId());
                    Map<Long, String> nombresProductos = getNombresProductos(p);
                    return pedidoMapper.toResponse(p, nombreUsuario, nombresProductos);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public PedidoResponseDTO actualizarEstadoPedido(Long id, String nuevoEstado) {
        log.info("Actualizando estado del pedido ID: {} a {}", id, nuevoEstado);
        List<String> estadosValidos = List.of("PENDIENTE", "CONFIRMADO", "ENVIADO", "ENTREGADO", "CANCELADO");
        if (!estadosValidos.contains(nuevoEstado.toUpperCase())) {
            throw new IllegalArgumentException("Estado inválido: " + nuevoEstado
                    + ". Valores permitidos: " + estadosValidos);
        }
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pedido no encontrado con ID: " + id));
        pedido.setEstado(nuevoEstado.toUpperCase());
        pedido = pedidoRepository.save(pedido);
        String nombreUsuario = getNombreUsuario(pedido.getUsuarioId());
        return pedidoMapper.toResponse(pedido, nombreUsuario);
    }

    @Transactional
    public void eliminarPedido(Long id) {
        log.info("Eliminando pedido con ID: {}", id);
        if (!pedidoRepository.existsById(id)) {
            throw new NoSuchElementException("Pedido no encontrado con ID: " + id);
        }
        pedidoRepository.deleteById(id);
    }

    private String getNombreUsuario(Long usuarioId) {
        try {
            return usuarioClient.getUsuarioById(usuarioId).getNombreCompleto();
        } catch (RuntimeException e) {
            log.warn("No se pudo obtener el nombre del usuario ID {}: {}", usuarioId, e.getMessage());
            return "Usuario ID: " + usuarioId;
        }
    }

    private String getNombreUsuarioSafe(Long usuarioId) {
        try {
            return usuarioClient.getUsuarioById(usuarioId).getNombreCompleto();
        } catch (RuntimeException e) {
            return "Usuario ID: " + usuarioId;
        }
    }

    private Map<Long, String> getNombresProductos(Pedido pedido) {
        return pedido.getDetalles().stream()
                .map(PedidoDetalle::getProductoId)
                .distinct()
                .collect(Collectors.toMap(
                        id -> id,
                        id -> {
                            try {
                                return productoClient.getProductoById(id).getNombre();
                            } catch (RuntimeException e) {
                                return "Producto ID: " + id;
                            }
                        }
                ));
    }
}
