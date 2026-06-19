package com.VentaOnline.OrderService.service;

import com.VentaOnline.OrderService.client.ProductoClient;
import com.VentaOnline.OrderService.client.UsuarioClient;
import com.VentaOnline.OrderService.dto.*;
import com.VentaOnline.OrderService.mapper.PedidoMapper;
import com.VentaOnline.OrderService.model.Pedido;
import com.VentaOnline.OrderService.model.PedidoDetalle;
import com.VentaOnline.OrderService.repository.PedidoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private PedidoMapper pedidoMapper;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private ProductoClient productoClient;

    @InjectMocks
    private PedidoService pedidoService;

    private PedidoRequestDTO requestDTO;
    private Pedido testPedido;
    private PedidoResponseDTO responseDTO;
    private UsuarioResponse usuarioResponse;
    private ProductoResponse productoResponse;
    private PedidoDetalle detalle;

    @BeforeEach
    void setUp() {
        PedidoDetalleRequestDTO detalleRequest = PedidoDetalleRequestDTO.builder()
                .productoId(1L)
                .cantidad(2)
                .build();

        requestDTO = PedidoRequestDTO.builder()
                .usuarioId(1L)
                .detalles(List.of(detalleRequest))
                .build();

        detalle = PedidoDetalle.builder()
                .id(1L)
                .productoId(1L)
                .cantidad(2)
                .precioUnitario(new BigDecimal("500.00"))
                .subtotal(new BigDecimal("1000.00"))
                .build();

        testPedido = Pedido.builder()
                .id(1L)
                .usuarioId(1L)
                .total(new BigDecimal("1000.00"))
                .estado("PENDIENTE")
                .fecha(LocalDateTime.now())
                .detalles(List.of(detalle))
                .build();

        detalle.setPedido(testPedido);

        responseDTO = PedidoResponseDTO.builder()
                .id(1L)
                .usuarioId(1L)
                .nombreUsuario("Juan Perez")
                .total(new BigDecimal("1000.00"))
                .estado("PENDIENTE")
                .build();

        usuarioResponse = new UsuarioResponse(1L, "Juan Perez", "juan@test.com");
        productoResponse = new ProductoResponse(1L, "Laptop", new BigDecimal("500.00"), 10);
    }

    @Test
    void crearPedido_deberiaCrearYRetornar() {
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(usuarioResponse);
        when(pedidoMapper.toEntity(requestDTO)).thenReturn(testPedido);
        when(productoClient.obtenerProductoPorId(1L)).thenReturn(productoResponse);
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(testPedido);
        when(pedidoMapper.toResponse(any(Pedido.class), anyString())).thenReturn(responseDTO);

        PedidoResponseDTO result = pedidoService.crearPedido(requestDTO);

        assertNotNull(result);
        assertEquals(responseDTO.getTotal(), result.getTotal());
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    void obtenerTodosPedidos_deberiaRetornarLista() {
        when(pedidoRepository.findAll()).thenReturn(List.of(testPedido));
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(usuarioResponse);
        when(productoClient.obtenerProductoPorId(1L)).thenReturn(productoResponse);
        when(pedidoMapper.toResponse(any(Pedido.class), anyString(), anyMap()))
                .thenReturn(responseDTO);

        List<PedidoResponseDTO> result = pedidoService.obtenerTodosPedidos();

        assertEquals(1, result.size());
    }

    @Test
    void obtenerPedidoPorId_deberiaRetornarPedido() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(testPedido));
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(usuarioResponse);
        when(productoClient.obtenerProductoPorId(1L)).thenReturn(productoResponse);
        when(pedidoMapper.toResponse(any(Pedido.class), anyString(), anyMap()))
                .thenReturn(responseDTO);

        PedidoResponseDTO result = pedidoService.obtenerPedidoPorId(1L);

        assertNotNull(result);
    }

    @Test
    void obtenerPedidoPorId_deberiaLanzarExcepcionCuandoNoExiste() {
        when(pedidoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> pedidoService.obtenerPedidoPorId(99L));
    }

    @Test
    void actualizarEstadoPedido_deberiaActualizarEstado() {
        when(pedidoRepository.findById(1L)).thenReturn(Optional.of(testPedido));
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(testPedido);
        when(usuarioClient.obtenerUsuarioPorId(1L)).thenReturn(usuarioResponse);
        when(pedidoMapper.toResponse(any(Pedido.class), anyString())).thenReturn(responseDTO);

        PedidoResponseDTO result = pedidoService.actualizarEstadoPedido(1L, "CONFIRMADO");

        assertNotNull(result);
        verify(pedidoRepository).save(any(Pedido.class));
    }

    @Test
    void actualizarEstadoPedido_deberiaLanzarExcepcionCuandoEstadoInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> pedidoService.actualizarEstadoPedido(1L, "INVALIDO"));
        verify(pedidoRepository, never()).save(any());
    }

    @Test
    void eliminarPedido_deberiaEliminarCuandoExiste() {
        when(pedidoRepository.existsById(1L)).thenReturn(true);

        pedidoService.eliminarPedido(1L);

        verify(pedidoRepository).deleteById(1L);
    }

    @Test
    void eliminarPedido_deberiaLanzarExcepcionCuandoNoExiste() {
        when(pedidoRepository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> pedidoService.eliminarPedido(99L));
        verify(pedidoRepository, never()).deleteById(any());
    }
}
