package com.VentaOnline.CategoryService.service;

import com.VentaOnline.CategoryService.client.UserClient;
import com.VentaOnline.CategoryService.dto.CategoriaRequestDTO;
import com.VentaOnline.CategoryService.dto.CategoriaResponseDTO;
import com.VentaOnline.CategoryService.model.Categoria;
import com.VentaOnline.CategoryService.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private UserClient userClient;

    @InjectMocks
    private CategoriaService categoriaService;

    private CategoriaRequestDTO requestDTO;
    private Categoria testCategoria;

    @BeforeEach
    void setUp() {
        requestDTO = CategoriaRequestDTO.builder()
                .nombre("Electrónicos")
                .descripcion("Productos electrónicos")
                .build();

        testCategoria = Categoria.builder()
                .id(1L)
                .nombre("Electrónicos")
                .descripcion("Productos electrónicos")
                .build();
    }

    @Test
    void crearCategoria_deberiaCrearYRetornar() {
        when(categoriaRepository.existsByNombre(requestDTO.getNombre())).thenReturn(false);
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(testCategoria);

        CategoriaResponseDTO result = categoriaService.crearCategoria(requestDTO);

        assertNotNull(result);
        assertEquals(testCategoria.getNombre(), result.getNombre());
        assertEquals(testCategoria.getDescripcion(), result.getDescripcion());
        verify(categoriaRepository).save(any(Categoria.class));
    }

    @Test
    void crearCategoria_deberiaLanzarExcepcionCuandoNombreYaExiste() {
        when(categoriaRepository.existsByNombre(requestDTO.getNombre())).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> categoriaService.crearCategoria(requestDTO));
        assertTrue(ex.getMessage().contains("ya existe"));
        verify(categoriaRepository, never()).save(any());
    }

    @Test
    void conseguirTodasCategorias_deberiaRetornarLista() {
        when(categoriaRepository.findAll()).thenReturn(List.of(testCategoria));

        List<CategoriaResponseDTO> result = categoriaService.conseguirTodasCategorias();

        assertEquals(1, result.size());
        assertEquals(testCategoria.getNombre(), result.get(0).getNombre());
    }

    @Test
    void buscarCategoriaById_deberiaRetornarCategoria() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(testCategoria));

        CategoriaResponseDTO result = categoriaService.buscarCategoriaById(1L);

        assertNotNull(result);
        assertEquals(testCategoria.getNombre(), result.getNombre());
    }

    @Test
    void buscarCategoriaById_deberiaLanzarExcepcionCuandoNoExiste() {
        when(categoriaRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> categoriaService.buscarCategoriaById(99L));
    }

    @Test
    void actualizarCategoria_deberiaActualizarCorrectamente() {
        CategoriaRequestDTO updateRequest = CategoriaRequestDTO.builder()
                .nombre("Electrónicos Actualizado")
                .descripcion("Nueva descripción")
                .build();

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(testCategoria));
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(testCategoria);

        CategoriaResponseDTO result = categoriaService.actualizarCategoria(1L, updateRequest);

        assertNotNull(result);
        verify(categoriaRepository).save(any(Categoria.class));
    }

    @Test
    void actualizarCategoria_deberiaLanzarExcepcionCuandoNuevoNombreYaExiste() {
        CategoriaRequestDTO updateRequest = CategoriaRequestDTO.builder()
                .nombre("Ropa")
                .descripcion("Nueva descripción")
                .build();

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(testCategoria));
        when(categoriaRepository.existsByNombre("Ropa")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> categoriaService.actualizarCategoria(1L, updateRequest));
        assertTrue(ex.getMessage().contains("ya está registrado"));
        verify(categoriaRepository, never()).save(any());
    }

    @Test
    void eliminarCategoria_deberiaEliminarCuandoExiste() {
        when(categoriaRepository.existsById(1L)).thenReturn(true);

        categoriaService.eliminarCategoria(1L);

        verify(categoriaRepository).deleteById(1L);
    }

    @Test
    void eliminarCategoria_deberiaLanzarExcepcionCuandoNoExiste() {
        when(categoriaRepository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> categoriaService.eliminarCategoria(99L));
        verify(categoriaRepository, never()).deleteById(any());
    }
}
