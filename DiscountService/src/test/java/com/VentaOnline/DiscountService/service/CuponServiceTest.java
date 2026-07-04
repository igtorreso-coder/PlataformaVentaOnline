package com.VentaOnline.DiscountService.service;

import com.VentaOnline.DiscountService.dto.CuponRequestDTO;
import com.VentaOnline.DiscountService.dto.CuponResponseDTO;
import com.VentaOnline.DiscountService.dto.ValidarCuponResponse;
import com.VentaOnline.DiscountService.model.Cupon;
import com.VentaOnline.DiscountService.repository.CuponRepository;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CuponServiceTest {

    @Mock
    private CuponRepository cuponRepository;

    @InjectMocks
    private CuponService cuponService;

    private CuponRequestDTO requestDTO;
    private Cupon cuponActivo;
    private Cupon cuponInactivo;
    private Cupon cuponExpirado;
    private Cupon cuponSinUsos;

    @BeforeEach
    void setUp() {
        requestDTO = CuponRequestDTO.builder()
                .codigo("DESC10")
                .tipo("PORCENTAJE")
                .valor(new BigDecimal("10.00"))
                .montoMinimo(new BigDecimal("50.00"))
                .usosMaximos(100)
                .fechaExpiracion(LocalDateTime.now().plusDays(30))
                .activo(true)
                .build();

        cuponActivo = Cupon.builder()
                .id(1L)
                .codigo("DESC10")
                .tipo("PORCENTAJE")
                .valor(new BigDecimal("10.00"))
                .montoMinimo(new BigDecimal("50.00"))
                .usosMaximos(100)
                .usosActuales(5)
                .fechaExpiracion(LocalDateTime.now().plusDays(30))
                .activo(true)
                .build();

        cuponInactivo = Cupon.builder()
                .id(2L)
                .codigo("INACTIVO")
                .tipo("FIJO")
                .valor(new BigDecimal("5.00"))
                .usosMaximos(50)
                .usosActuales(0)
                .activo(false)
                .build();

        cuponExpirado = Cupon.builder()
                .id(3L)
                .codigo("EXPIRADO")
                .tipo("PORCENTAJE")
                .valor(new BigDecimal("20.00"))
                .usosMaximos(100)
                .usosActuales(0)
                .fechaExpiracion(LocalDateTime.now().minusDays(1))
                .activo(true)
                .build();

        cuponSinUsos = Cupon.builder()
                .id(4L)
                .codigo("SINUSOS")
                .tipo("PORCENTAJE")
                .valor(new BigDecimal("15.00"))
                .usosMaximos(10)
                .usosActuales(10)
                .fechaExpiracion(LocalDateTime.now().plusDays(30))
                .activo(true)
                .build();
    }

    @Test
    void crearCupon_deberiaCrearYRetornar() {
        when(cuponRepository.existsByCodigo("DESC10")).thenReturn(false);
        when(cuponRepository.save(any(Cupon.class))).thenReturn(cuponActivo);

        CuponResponseDTO result = cuponService.crearCupon(requestDTO);

        assertNotNull(result);
        assertEquals("DESC10", result.getCodigo());
        assertEquals("PORCENTAJE", result.getTipo());
        verify(cuponRepository).save(any(Cupon.class));
    }

    @Test
    void crearCupon_deberiaLanzarExcepcionCuandoCodigoYaExiste() {
        when(cuponRepository.existsByCodigo("DESC10")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cuponService.crearCupon(requestDTO));
        assertTrue(ex.getMessage().contains("ya está registrado"));
        verify(cuponRepository, never()).save(any());
    }

    @Test
    void crearCupon_deberiaLanzarExcepcionCuandoTipoEsInvalido() {
        requestDTO.setTipo("INVALIDO");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cuponService.crearCupon(requestDTO));
        assertTrue(ex.getMessage().contains("Tipo inválido"));
        verify(cuponRepository, never()).save(any());
    }

    @Test
    void obtenerTodosCupones_deberiaRetornarLista() {
        when(cuponRepository.findAll()).thenReturn(List.of(cuponActivo, cuponInactivo));

        List<CuponResponseDTO> result = cuponService.obtenerTodosCupones();

        assertEquals(2, result.size());
    }

    @Test
    void obtenerCuponPorId_deberiaRetornarCupon() {
        when(cuponRepository.findById(1L)).thenReturn(Optional.of(cuponActivo));

        CuponResponseDTO result = cuponService.obtenerCuponPorId(1L);

        assertNotNull(result);
        assertEquals("DESC10", result.getCodigo());
    }

    @Test
    void obtenerCuponPorId_deberiaLanzarExcepcionCuandoNoExiste() {
        when(cuponRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> cuponService.obtenerCuponPorId(99L));
    }

    @Test
    void obtenerCuponPorCodigo_deberiaRetornarCupon() {
        when(cuponRepository.findByCodigo("DESC10")).thenReturn(Optional.of(cuponActivo));

        CuponResponseDTO result = cuponService.obtenerCuponPorCodigo("DESC10");

        assertNotNull(result);
        assertEquals("DESC10", result.getCodigo());
    }

    @Test
    void obtenerCuponPorCodigo_deberiaLanzarExcepcionCuandoNoExiste() {
        when(cuponRepository.findByCodigo("NOEXISTE")).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> cuponService.obtenerCuponPorCodigo("NOEXISTE"));
    }

    @Test
    void validarCupon_deberiaRetornarValido() {
        when(cuponRepository.findById(1L)).thenReturn(Optional.of(cuponActivo));

        ValidarCuponResponse result = cuponService.validarCupon(1L);

        assertTrue(result.isValido());
        assertEquals("Cupón válido", result.getMensaje());
    }

    @Test
    void validarCupon_deberiaRetornarInactivo() {
        when(cuponRepository.findById(2L)).thenReturn(Optional.of(cuponInactivo));

        ValidarCuponResponse result = cuponService.validarCupon(2L);

        assertFalse(result.isValido());
        assertTrue(result.getMensaje().contains("no está activo"));
    }

    @Test
    void validarCupon_deberiaRetornarExpirado() {
        when(cuponRepository.findById(3L)).thenReturn(Optional.of(cuponExpirado));

        ValidarCuponResponse result = cuponService.validarCupon(3L);

        assertFalse(result.isValido());
        assertTrue(result.getMensaje().contains("expirado"));
    }

    @Test
    void validarCupon_deberiaRetornarLimiteDeUsos() {
        when(cuponRepository.findById(4L)).thenReturn(Optional.of(cuponSinUsos));

        ValidarCuponResponse result = cuponService.validarCupon(4L);

        assertFalse(result.isValido());
        assertTrue(result.getMensaje().contains("límite de usos"));
    }

    @Test
    void usarCupon_deberiaIncrementarUsos() {
        when(cuponRepository.findById(1L)).thenReturn(Optional.of(cuponActivo));
        when(cuponRepository.save(any(Cupon.class))).thenReturn(cuponActivo);

        CuponResponseDTO result = cuponService.usarCupon(1L);

        assertNotNull(result);
        verify(cuponRepository).save(any(Cupon.class));
    }

    @Test
    void usarCupon_deberiaLanzarExcepcionCuandoInvalido() {
        when(cuponRepository.findById(2L)).thenReturn(Optional.of(cuponInactivo));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cuponService.usarCupon(2L));
        assertTrue(ex.getMessage().contains("no está activo"));
        verify(cuponRepository, never()).save(any());
    }

    @Test
    void eliminarCupon_deberiaEliminarCuandoExiste() {
        when(cuponRepository.existsById(1L)).thenReturn(true);

        cuponService.eliminarCupon(1L);

        verify(cuponRepository).deleteById(1L);
    }

    @Test
    void eliminarCupon_deberiaLanzarExcepcionCuandoNoExiste() {
        when(cuponRepository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> cuponService.eliminarCupon(99L));
        verify(cuponRepository, never()).deleteById(any());
    }

    @Test
    void actualizarCupon_deberiaActualizarCorrectamente() {
        when(cuponRepository.findById(1L)).thenReturn(Optional.of(cuponActivo));
        when(cuponRepository.save(any(Cupon.class))).thenReturn(cuponActivo);

        CuponResponseDTO result = cuponService.actualizarCupon(1L, requestDTO);

        assertNotNull(result);
        verify(cuponRepository).save(any(Cupon.class));
    }
}
