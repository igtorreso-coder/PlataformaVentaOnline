package com.VentaOnline.ProductService.service;

import com.VentaOnline.ProductService.client.CategoriaClient;
import com.VentaOnline.ProductService.dto.CategoriaResponse;
import com.VentaOnline.ProductService.dto.ProductoRequestDTO;
import com.VentaOnline.ProductService.dto.ProductoResponseDTO;
import com.VentaOnline.ProductService.model.Producto;
import com.VentaOnline.ProductService.repository.ProductoRepository;
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
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private CategoriaClient categoriaClient;

    @InjectMocks
    private ProductoService productoService;

    private ProductoRequestDTO requestDTO;
    private Producto testProducto;
    private CategoriaResponse categoriaResponse;

    @BeforeEach
    void setUp() {
        requestDTO = ProductoRequestDTO.builder()
                .nombre("Laptop")
                .descripcion("Laptop de alta gama")
                .precio(new BigDecimal("1500.00"))
                .categoriaId(1L)
                .stock(10)
                .build();

        testProducto = Producto.builder()
                .id(1L)
                .nombre("Laptop")
                .descripcion("Laptop de alta gama")
                .precio(new BigDecimal("1500.00"))
                .categoriaId(1L)
                .stock(10)
                .build();

        categoriaResponse = new CategoriaResponse(1L, "Electrónicos");
    }

    @Test
    void crearProducto_deberiaCrearYRetornar() {
        when(categoriaClient.obtenerCategoriaPorId(1L)).thenReturn(categoriaResponse);
        when(productoRepository.save(any(Producto.class))).thenReturn(testProducto);

        ProductoResponseDTO result = productoService.crearProducto(requestDTO);

        assertNotNull(result);
        assertEquals(testProducto.getNombre(), result.getNombre());
        assertEquals(categoriaResponse.getNombre(), result.getCategoriaNombre());
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void obtenerTodosProductos_deberiaRetornarLista() {
        when(productoRepository.findAll()).thenReturn(List.of(testProducto));
        when(categoriaClient.obtenerCategoriaPorId(1L)).thenReturn(categoriaResponse);

        List<ProductoResponseDTO> result = productoService.obtenerTodosProductos();

        assertEquals(1, result.size());
        assertEquals(testProducto.getNombre(), result.get(0).getNombre());
    }

    @Test
    void obtenerProductoPorId_deberiaRetornarProducto() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(testProducto));
        when(categoriaClient.obtenerCategoriaPorId(1L)).thenReturn(categoriaResponse);

        ProductoResponseDTO result = productoService.obtenerProductoPorId(1L);

        assertNotNull(result);
        assertEquals(testProducto.getNombre(), result.getNombre());
    }

    @Test
    void obtenerProductoPorId_deberiaLanzarExcepcionCuandoNoExiste() {
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> productoService.obtenerProductoPorId(99L));
    }

    @Test
    void obtenerProductosPorCategoria_deberiaRetornarLista() {
        when(productoRepository.findByCategoriaId(1L)).thenReturn(List.of(testProducto));
        when(categoriaClient.obtenerCategoriaPorId(1L)).thenReturn(categoriaResponse);

        List<ProductoResponseDTO> result = productoService.obtenerProductosPorCategoria(1L);

        assertEquals(1, result.size());
    }

    @Test
    void actualizarProducto_deberiaActualizarCorrectamente() {
        when(productoRepository.findById(1L)).thenReturn(Optional.of(testProducto));
        when(categoriaClient.obtenerCategoriaPorId(1L)).thenReturn(categoriaResponse);
        when(productoRepository.save(any(Producto.class))).thenReturn(testProducto);

        ProductoResponseDTO result = productoService.actualizarProducto(1L, requestDTO);

        assertNotNull(result);
        verify(productoRepository).save(any(Producto.class));
    }

    @Test
    void eliminarProducto_deberiaEliminarCuandoExiste() {
        when(productoRepository.existsById(1L)).thenReturn(true);

        productoService.eliminarProducto(1L);

        verify(productoRepository).deleteById(1L);
    }

    @Test
    void eliminarProducto_deberiaLanzarExcepcionCuandoNoExiste() {
        when(productoRepository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> productoService.eliminarProducto(99L));
        verify(productoRepository, never()).deleteById(any());
    }
}
