package com.VentaOnline.InventoryService.service;

import com.VentaOnline.InventoryService.client.ProductoClient;
import com.VentaOnline.InventoryService.dto.InventarioRequestDTO;
import com.VentaOnline.InventoryService.dto.InventarioResponseDTO;
import com.VentaOnline.InventoryService.dto.ProductoResponse;
import com.VentaOnline.InventoryService.model.Inventario;
import com.VentaOnline.InventoryService.repository.InventarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventarioServiceTest {

    @Mock
    private InventarioRepository inventarioRepository;

    @Mock
    private ProductoClient productoClient;

    @InjectMocks
    private InventarioService inventarioService;

    private InventarioRequestDTO entradaRequest;
    private InventarioRequestDTO salidaRequest;
    private InventarioRequestDTO ajusteRequest;
    private Inventario movimientoEntrada;
    private Inventario movimientoSalida;
    private ProductoResponse productoResponse;

    @BeforeEach
    void setUp() {
        entradaRequest = InventarioRequestDTO.builder()
                .productoId(1L)
                .tipo("ENTRADA")
                .cantidad(10)
                .observacion("Compra de proveedor")
                .build();

        salidaRequest = InventarioRequestDTO.builder()
                .productoId(1L)
                .tipo("SALIDA")
                .cantidad(3)
                .observacion("Venta")
                .build();

        ajusteRequest = InventarioRequestDTO.builder()
                .productoId(1L)
                .tipo("AJUSTE")
                .cantidad(50)
                .observacion("Ajuste de inventario")
                .build();

        movimientoEntrada = Inventario.builder()
                .id(1L)
                .productoId(1L)
                .tipo("ENTRADA")
                .cantidad(10)
                .observacion("Compra de proveedor")
                .build();

        movimientoSalida = Inventario.builder()
                .id(2L)
                .productoId(1L)
                .tipo("SALIDA")
                .cantidad(3)
                .observacion("Venta")
                .build();

        productoResponse = new ProductoResponse(1L, "Laptop", new BigDecimal("1500.00"), 10);
    }

    @Test
    void crearMovimiento_deberiaCrearEntrada() {
        when(productoClient.obtenerProductoPorId(1L)).thenReturn(productoResponse);
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(movimientoEntrada);

        InventarioResponseDTO result = inventarioService.crearMovimiento(entradaRequest);

        assertNotNull(result);
        assertEquals("ENTRADA", result.getTipo());
        assertEquals(10, result.getCantidad());
        verify(inventarioRepository).save(any(Inventario.class));
    }

    @Test
    void crearMovimiento_deberiaCrearSalida() {
        when(productoClient.obtenerProductoPorId(1L)).thenReturn(productoResponse);
        when(inventarioRepository.save(any(Inventario.class))).thenReturn(movimientoSalida);

        InventarioResponseDTO result = inventarioService.crearMovimiento(salidaRequest);

        assertNotNull(result);
        assertEquals("SALIDA", result.getTipo());
        verify(inventarioRepository).save(any(Inventario.class));
    }

    @Test
    void crearMovimiento_deberiaLanzarExcepcionCuandoTipoEsInvalido() {
        InventarioRequestDTO invalidRequest = InventarioRequestDTO.builder()
                .productoId(1L)
                .tipo("INVALIDO")
                .cantidad(5)
                .build();

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> inventarioService.crearMovimiento(invalidRequest));
        assertTrue(ex.getMessage().contains("Tipo de movimiento inválido"));
        verify(inventarioRepository, never()).save(any());
    }

    @Test
    void obtenerTodosMovimientos_deberiaRetornarLista() {
        when(inventarioRepository.findAll()).thenReturn(List.of(movimientoEntrada, movimientoSalida));
        when(productoClient.obtenerProductoPorId(1L)).thenReturn(productoResponse);

        List<InventarioResponseDTO> result = inventarioService.obtenerTodosMovimientos();

        assertEquals(2, result.size());
    }

    @Test
    void obtenerMovimientoPorId_deberiaRetornarMovimiento() {
        when(inventarioRepository.findById(1L)).thenReturn(Optional.of(movimientoEntrada));
        when(productoClient.obtenerProductoPorId(1L)).thenReturn(productoResponse);

        InventarioResponseDTO result = inventarioService.obtenerMovimientoPorId(1L);

        assertNotNull(result);
        assertEquals("ENTRADA", result.getTipo());
    }

    @Test
    void obtenerMovimientoPorId_deberiaLanzarExcepcionCuandoNoExiste() {
        when(inventarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> inventarioService.obtenerMovimientoPorId(99L));
    }

    @Test
    void obtenerStockPorProducto_deberiaCalcularStockCorrectamente() {
        List<Inventario> movimientos = List.of(
                Inventario.builder().tipo("ENTRADA").cantidad(10).build(),
                Inventario.builder().tipo("ENTRADA").cantidad(5).build(),
                Inventario.builder().tipo("SALIDA").cantidad(3).build()
        );
        when(inventarioRepository.findByProductoIdOrderByCreatedAtDesc(1L)).thenReturn(movimientos);

        Integer stock = inventarioService.obtenerStockPorProducto(1L);

        assertEquals(12, stock);
    }

    @Test
    void obtenerStockPorProducto_deberiaRetornarCeroCuandoNoHayMovimientos() {
        when(inventarioRepository.findByProductoIdOrderByCreatedAtDesc(1L)).thenReturn(List.of());

        Integer stock = inventarioService.obtenerStockPorProducto(1L);

        assertEquals(0, stock);
    }

    @Test
    void obtenerStockPorProducto_deberiaManejarAjuste() {
        List<Inventario> movimientos = List.of(
                Inventario.builder().tipo("ENTRADA").cantidad(10).build(),
                Inventario.builder().tipo("AJUSTE").cantidad(50).build()
        );
        when(inventarioRepository.findByProductoIdOrderByCreatedAtDesc(1L)).thenReturn(movimientos);

        Integer stock = inventarioService.obtenerStockPorProducto(1L);

        assertEquals(50, stock);
    }

    @Test
    void obtenerStockPorProducto_deberiaRetornarMinimoCero() {
        List<Inventario> movimientos = List.of(
                Inventario.builder().tipo("SALIDA").cantidad(10).build()
        );
        when(inventarioRepository.findByProductoIdOrderByCreatedAtDesc(1L)).thenReturn(movimientos);

        Integer stock = inventarioService.obtenerStockPorProducto(1L);

        assertEquals(0, stock);
    }
}
