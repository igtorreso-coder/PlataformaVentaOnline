package com.VentaOnline.OrderService.mapper;

import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import com.VentaOnline.OrderService.dto.PedidoDetalleResponseDTO;
import com.VentaOnline.OrderService.dto.PedidoRequestDTO;
import com.VentaOnline.OrderService.dto.PedidoResponseDTO;
import com.VentaOnline.OrderService.model.Pedido;
import com.VentaOnline.OrderService.model.PedidoDetalle;

@Component
public class PedidoMapper {

    public Pedido toEntity(PedidoRequestDTO request) {
        Pedido pedido = Pedido.builder()
                .usuarioId(request.getUsuarioId())
                .build();
        if (request.getDetalles() != null) {
            pedido.setDetalles(request.getDetalles().stream()
                    .map(dto -> {
                        PedidoDetalle detalle = PedidoDetalle.builder()
                                .productoId(dto.getProductoId())
                                .cantidad(dto.getCantidad())
                                .build();
                        detalle.setPedido(pedido);
                        return detalle;
                    })
                    .collect(Collectors.toList()));
        }
        return pedido;
    }

    public PedidoResponseDTO toResponse(Pedido pedido, String nombreUsuario) {
        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .usuarioId(pedido.getUsuarioId())
                .nombreUsuario(nombreUsuario)
                .fecha(pedido.getFecha())
                .total(pedido.getTotal())
                .estado(pedido.getEstado())
                .detalles(pedido.getDetalles().stream()
                        .map(d -> toDetalleResponse(d, null))
                        .collect(Collectors.toList()))
                .createdAt(pedido.getCreatedAt())
                .updatedAt(pedido.getUpdatedAt())
                .build();
    }

    public PedidoResponseDTO toResponse(Pedido pedido, String nombreUsuario, java.util.Map<Long, String> nombresProductos) {
        return PedidoResponseDTO.builder()
                .id(pedido.getId())
                .usuarioId(pedido.getUsuarioId())
                .nombreUsuario(nombreUsuario)
                .fecha(pedido.getFecha())
                .total(pedido.getTotal())
                .estado(pedido.getEstado())
                .detalles(pedido.getDetalles().stream()
                        .map(d -> toDetalleResponse(d, nombresProductos.get(d.getProductoId())))
                        .collect(Collectors.toList()))
                .createdAt(pedido.getCreatedAt())
                .updatedAt(pedido.getUpdatedAt())
                .build();
    }

    public PedidoDetalleResponseDTO toDetalleResponse(PedidoDetalle detalle, String nombreProducto) {
        return PedidoDetalleResponseDTO.builder()
                .id(detalle.getId())
                .productoId(detalle.getProductoId())
                .nombreProducto(nombreProducto != null ? nombreProducto : "Producto ID: " + detalle.getProductoId())
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .subtotal(detalle.getSubtotal())
                .build();
    }
}
