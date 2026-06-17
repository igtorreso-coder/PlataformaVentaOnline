package com.VentaOnline.CartService.service;

import com.VentaOnline.CartService.client.ProductoClient;
import com.VentaOnline.CartService.client.UsuarioClient;
import com.VentaOnline.CartService.dto.*;
import com.VentaOnline.CartService.model.Carrito;
import com.VentaOnline.CartService.model.CarritoItem;
import com.VentaOnline.CartService.repository.CarritoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CarritoServiceTest {

    @Mock
    private CarritoRepository carritoRepository;

    @Mock
    private UsuarioClient usuarioClient;

    @Mock
    private ProductoClient productoClient;

    @InjectMocks
    private CarritoService carritoService;

    private CarritoRequestDTO createRequest;
    private Carrito testCarrito;
    private CarritoItem testItem;
    private UsuarioResponse usuarioResponse;
    private ProductoResponse productoResponse;

    @BeforeEach
    void setUp() {
        createRequest = CarritoRequestDTO.builder()
                .usuarioId(1L)
                .build();

        testCarrito = Carrito.builder()
                .id(1L)
                .usuarioId(1L)
                .estado("ACTIVO")
                .items(new ArrayList<>())
                .build();

        testItem = CarritoItem.builder()
                .id(1L)
                .carrito(testCarrito)
                .productoId(1L)
                .cantidad(2)
                .precioUnitario(new BigDecimal("500.00"))
                .subtotal(new BigDecimal("1000.00"))
                .build();

        usuarioResponse = new UsuarioResponse(1L, "Juan Perez", "juan@test.com");
        productoResponse = new ProductoResponse(1L, "Laptop", new BigDecimal("500.00"), 10);
    }

    @Test
    void crearCarrito_deberiaCrearYRetornar() {
        when(usuarioClient.obtenerUsuario(1L)).thenReturn(usuarioResponse);
        when(carritoRepository.save(any(Carrito.class))).thenReturn(testCarrito);

        CarritoResponseDTO result = carritoService.crearCarrito(createRequest);

        assertNotNull(result);
        assertEquals(testCarrito.getUsuarioId(), result.getUsuarioId());
        assertEquals("ACTIVO", result.getEstado());
        verify(carritoRepository).save(any(Carrito.class));
    }

    @Test
    void obtenerCarritoById_deberiaRetornarCarrito() {
        when(carritoRepository.findById(1L)).thenReturn(Optional.of(testCarrito));

        CarritoResponseDTO result = carritoService.obtenerCarritoById(1L);

        assertNotNull(result);
        assertEquals(testCarrito.getUsuarioId(), result.getUsuarioId());
    }

    @Test
    void obtenerCarritoById_deberiaLanzarExcepcionCuandoNoExiste() {
        when(carritoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> carritoService.obtenerCarritoById(99L));
    }

    @Test
    void obtenerCarritoActivoByUsuario_deberiaRetornarCarrito() {
        when(carritoRepository.findByUsuarioIdAndEstado(1L, "ACTIVO"))
                .thenReturn(Optional.of(testCarrito));

        CarritoResponseDTO result = carritoService.obtenerCarritoActivoByUsuario(1L);

        assertNotNull(result);
        assertEquals(1L, result.getUsuarioId());
    }

    @Test
    void obtenerCarritoActivoByUsuario_deberiaLanzarExcepcionCuandoNoHayActivo() {
        when(carritoRepository.findByUsuarioIdAndEstado(1L, "ACTIVO"))
                .thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> carritoService.obtenerCarritoActivoByUsuario(1L));
    }

    @Test
    void agregarItem_deberiaAgregarYRetornar() {
        CarritoItemRequestDTO itemRequest = CarritoItemRequestDTO.builder()
                .productoId(1L)
                .cantidad(2)
                .build();

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(testCarrito));
        when(productoClient.obtenerProducto(1L)).thenReturn(productoResponse);
        when(carritoRepository.save(any(Carrito.class))).thenReturn(testCarrito);

        CarritoResponseDTO result = carritoService.agregarItem(1L, itemRequest);

        assertNotNull(result);
        verify(carritoRepository).save(any(Carrito.class));
    }

    @Test
    void agregarItem_deberiaLanzarExcepcionCuandoCarritoNoActivo() {
        testCarrito.setEstado("COMPLETADO");
        CarritoItemRequestDTO itemRequest = CarritoItemRequestDTO.builder()
                .productoId(1L)
                .cantidad(2)
                .build();

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(testCarrito));

        assertThrows(IllegalArgumentException.class,
                () -> carritoService.agregarItem(1L, itemRequest));
        verify(carritoRepository, never()).save(any());
    }

    @Test
    void finalizarCarrito_deberiaCambiarEstadoACompletado() {
        when(carritoRepository.findById(1L)).thenReturn(Optional.of(testCarrito));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(testCarrito);

        CarritoResponseDTO result = carritoService.finalizarCarrito(1L);

        assertNotNull(result);
        assertEquals("COMPLETADO", testCarrito.getEstado());
        verify(carritoRepository).save(any(Carrito.class));
    }

    @Test
    void finalizarCarrito_deberiaLanzarExcepcionCuandoNoActivo() {
        testCarrito.setEstado("COMPLETADO");
        when(carritoRepository.findById(1L)).thenReturn(Optional.of(testCarrito));

        assertThrows(IllegalArgumentException.class,
                () -> carritoService.finalizarCarrito(1L));
        verify(carritoRepository, never()).save(any());
    }

    @Test
    void eliminarCarrito_deberiaEliminar() {
        when(carritoRepository.findById(1L)).thenReturn(Optional.of(testCarrito));

        carritoService.eliminarCarrito(1L);

        verify(carritoRepository).delete(testCarrito);
    }

    @Test
    void actualizarItem_deberiaActualizarCantidad() {
        testCarrito.getItems().add(testItem);
        CarritoItemUpdateRequestDTO updateRequest = CarritoItemUpdateRequestDTO.builder()
                .cantidad(3)
                .build();

        when(carritoRepository.findById(1L)).thenReturn(Optional.of(testCarrito));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(testCarrito);

        CarritoResponseDTO result = carritoService.actualizarItem(1L, 1L, updateRequest);

        assertNotNull(result);
        assertEquals(3, testItem.getCantidad());
        verify(carritoRepository).save(any(Carrito.class));
    }

    @Test
    void eliminarItem_deberiaRemoverItem() {
        testCarrito.getItems().add(testItem);
        when(carritoRepository.findById(1L)).thenReturn(Optional.of(testCarrito));
        when(carritoRepository.save(any(Carrito.class))).thenReturn(testCarrito);

        CarritoResponseDTO result = carritoService.eliminarItem(1L, 1L);

        assertNotNull(result);
        assertTrue(testCarrito.getItems().isEmpty());
        verify(carritoRepository).save(any(Carrito.class));
    }
}
