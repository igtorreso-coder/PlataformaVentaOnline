package com.VentaOnline.CartService.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.VentaOnline.CartService.client.ProductoClient;
import com.VentaOnline.CartService.client.UsuarioClient;
import com.VentaOnline.CartService.dto.CarritoItemRequestDTO;
import com.VentaOnline.CartService.dto.CarritoItemResponseDTO;
import com.VentaOnline.CartService.dto.CarritoItemUpdateRequestDTO;
import com.VentaOnline.CartService.dto.CarritoRequestDTO;
import com.VentaOnline.CartService.dto.CarritoResponseDTO;
import com.VentaOnline.CartService.model.Carrito;
import com.VentaOnline.CartService.model.CarritoItem;
import com.VentaOnline.CartService.repository.CarritoRepository;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class CarritoService {

    @Autowired
    private CarritoRepository carritoRepository;
    @Autowired
    private UsuarioClient usuarioClient;
    @Autowired
    private ProductoClient productoClient;

    @Transactional
    public CarritoResponseDTO crearCarrito(CarritoRequestDTO request) {
        usuarioClient.obtenerUsuario(request.getUsuarioId())
                .block();

        Carrito carrito = Carrito.builder()
                .usuarioId(request.getUsuarioId())
                .items(List.of())
                .build();
        carrito = carritoRepository.save(carrito);
        return toResponse(carrito);
    }

    public CarritoResponseDTO obtenerCarritoById(Long id) {
        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado con ID: " + id));
        return toResponse(carrito);
    }

    public CarritoResponseDTO obtenerCarritoActivoByUsuario(Long usuarioId) {
        Carrito carrito = carritoRepository.findByUsuarioIdAndEstado(usuarioId, "ACTIVO")
                .orElseThrow(() -> new IllegalArgumentException("No hay carrito activo para el usuario: " + usuarioId));
        return toResponse(carrito);
    }

    @Transactional
    public CarritoResponseDTO agregarItem(Long carritoId, CarritoItemRequestDTO request) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado con ID: " + carritoId));

        if (!"ACTIVO".equals(carrito.getEstado())) {
            throw new IllegalArgumentException("El carrito no está activo");
        }

        var productoResponse = productoClient.obtenerProducto(request.getProductoId())
                .block();

        if (productoResponse == null) {
            throw new IllegalArgumentException("Producto no encontrado con ID: " + request.getProductoId());
        }

        BigDecimal precioUnitario = productoResponse.getPrecio();
        BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(request.getCantidad()));

        CarritoItem item = CarritoItem.builder()
                .carrito(carrito)
                .productoId(request.getProductoId())
                .cantidad(request.getCantidad())
                .precioUnitario(precioUnitario)
                .subtotal(subtotal)
                .build();

        carrito.getItems().add(item);
        carrito.setUpdatedAt(LocalDateTime.now());
        carrito = carritoRepository.save(carrito);
        return toResponse(carrito);
    }

    @Transactional
    public CarritoResponseDTO actualizarItem(Long carritoId, Long itemId, CarritoItemUpdateRequestDTO request) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado con ID: " + carritoId));

        CarritoItem item = carrito.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item no encontrado con ID: " + itemId));

        item.setCantidad(request.getCantidad());
        item.setSubtotal(item.getPrecioUnitario().multiply(BigDecimal.valueOf(request.getCantidad())));

        carrito.setUpdatedAt(LocalDateTime.now());
        carrito = carritoRepository.save(carrito);
        return toResponse(carrito);
    }

    @Transactional
    public CarritoResponseDTO eliminarItem(Long carritoId, Long itemId) {
        Carrito carrito = carritoRepository.findById(carritoId)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado con ID: " + carritoId));

        CarritoItem item = carrito.getItems().stream()
                .filter(i -> i.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Item no encontrado con ID: " + itemId));

        carrito.getItems().remove(item);
        carrito.setUpdatedAt(LocalDateTime.now());
        carrito = carritoRepository.save(carrito);
        return toResponse(carrito);
    }

    @Transactional
    public void eliminarCarrito(Long id) {
        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado con ID: " + id));
        carritoRepository.delete(carrito);
    }

    @Transactional
    public CarritoResponseDTO finalizarCarrito(Long id) {
        Carrito carrito = carritoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Carrito no encontrado con ID: " + id));

        if (!"ACTIVO".equals(carrito.getEstado())) {
            throw new IllegalArgumentException("El carrito no está activo");
        }

        carrito.setEstado("COMPLETADO");
        carrito.setUpdatedAt(LocalDateTime.now());
        carrito = carritoRepository.save(carrito);
        return toResponse(carrito);
    }

    private CarritoResponseDTO toResponse(Carrito carrito) {
        List<CarritoItemResponseDTO> itemsDTO = carrito.getItems().stream()
                .map(item -> {
                    String nombreProducto = "Producto #" + item.getProductoId();
                    try {
                        var prod = productoClient.obtenerProducto(item.getProductoId()).block();
                        if (prod != null) {
                            nombreProducto = prod.getNombre();
                        }
                    } catch (Exception e) {
                        nombreProducto = "Producto #" + item.getProductoId();
                    }
                    return CarritoItemResponseDTO.builder()
                            .id(item.getId())
                            .productoId(item.getProductoId())
                            .nombreProducto(nombreProducto)
                            .cantidad(item.getCantidad())
                            .precioUnitario(item.getPrecioUnitario())
                            .subtotal(item.getSubtotal())
                            .build();
                })
                .collect(Collectors.toList());

        return CarritoResponseDTO.builder()
                .id(carrito.getId())
                .usuarioId(carrito.getUsuarioId())
                .estado(carrito.getEstado())
                .items(itemsDTO)
                .createdAt(carrito.getCreatedAt())
                .updatedAt(carrito.getUpdatedAt())
                .build();
    }
}
