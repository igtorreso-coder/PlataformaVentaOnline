package com.VentaOnline.PaymentService.service;

import com.VentaOnline.PaymentService.client.PedidoClient;
import com.VentaOnline.PaymentService.dto.PagoRequestDTO;
import com.VentaOnline.PaymentService.dto.PagoResponseDTO;
import com.VentaOnline.PaymentService.dto.PedidoResponse;
import com.VentaOnline.PaymentService.model.Pago;
import com.VentaOnline.PaymentService.repository.PagoRepository;
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
class PagoServiceTest {

    @Mock
    private PagoRepository pagoRepository;

    @Mock
    private PedidoClient pedidoClient;

    @InjectMocks
    private PagoService pagoService;

    private PagoRequestDTO requestDTO;
    private Pago testPago;
    private PedidoResponse pedidoResponse;

    @BeforeEach
    void setUp() {
        requestDTO = PagoRequestDTO.builder()
                .pedidoId(1L)
                .monto(new BigDecimal("1000.00"))
                .metodoPago("TARJETA")
                .build();

        testPago = Pago.builder()
                .id(1L)
                .pedidoId(1L)
                .monto(new BigDecimal("1000.00"))
                .metodoPago("TARJETA")
                .estado("PENDIENTE")
                .build();

        pedidoResponse = new PedidoResponse(1L, new BigDecimal("1000.00"), "PENDIENTE");
    }

    @Test
    void crearPago_deberiaCrearYRetornar() {
        when(pedidoClient.getPedidoById(1L)).thenReturn(pedidoResponse);
        when(pagoRepository.save(any(Pago.class))).thenReturn(testPago);

        PagoResponseDTO result = pagoService.crearPago(requestDTO);

        assertNotNull(result);
        assertEquals(testPago.getMonto(), result.getMonto());
        assertEquals("TARJETA", result.getMetodoPago());
        verify(pagoRepository).save(any(Pago.class));
    }

    @Test
    void crearPago_deberiaLanzarExcepcionCuandoPedidoNoEstaPendiente() {
        pedidoResponse.setEstado("CONFIRMADO");
        when(pedidoClient.getPedidoById(1L)).thenReturn(pedidoResponse);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> pagoService.crearPago(requestDTO));
        assertTrue(ex.getMessage().contains("no está pendiente"));
        verify(pagoRepository, never()).save(any());
    }

    @Test
    void obtenerTodosPagos_deberiaRetornarLista() {
        when(pagoRepository.findAll()).thenReturn(List.of(testPago));

        List<PagoResponseDTO> result = pagoService.obtenerTodosPagos();

        assertEquals(1, result.size());
    }

    @Test
    void obtenerPagoById_deberiaRetornarPago() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(testPago));

        PagoResponseDTO result = pagoService.obtenerPagoById(1L);

        assertNotNull(result);
        assertEquals(testPago.getMonto(), result.getMonto());
    }

    @Test
    void obtenerPagoById_deberiaLanzarExcepcionCuandoNoExiste() {
        when(pagoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> pagoService.obtenerPagoById(99L));
    }

    @Test
    void procesarPago_deberiaProcesarCorrectamente() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(testPago));
        when(pagoRepository.save(any(Pago.class))).thenReturn(testPago);

        PagoResponseDTO result = pagoService.procesarPago(1L);

        assertNotNull(result);
        assertEquals("COMPLETADO", testPago.getEstado());
        assertNotNull(testPago.getReferencia());
        verify(pagoRepository).save(any(Pago.class));
    }

    @Test
    void procesarPago_deberiaLanzarExcepcionCuandoNoEstaPendiente() {
        testPago.setEstado("COMPLETADO");
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(testPago));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> pagoService.procesarPago(1L));
        assertTrue(ex.getMessage().contains("no está pendiente"));
        verify(pagoRepository, never()).save(any());
    }

    @Test
    void actualizarEstadoPago_deberiaActualizarCorrectamente() {
        when(pagoRepository.findById(1L)).thenReturn(Optional.of(testPago));
        when(pagoRepository.save(any(Pago.class))).thenReturn(testPago);

        PagoResponseDTO result = pagoService.actualizarEstadoPago(1L, "COMPLETADO");

        assertNotNull(result);
        verify(pagoRepository).save(any(Pago.class));
    }

    @Test
    void actualizarEstadoPago_deberiaLanzarExcepcionCuandoEstadoInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> pagoService.actualizarEstadoPago(1L, "INVALIDO"));
        verify(pagoRepository, never()).save(any());
    }

    @Test
    void eliminarPago_deberiaEliminarCuandoExiste() {
        when(pagoRepository.existsById(1L)).thenReturn(true);

        pagoService.eliminarPago(1L);

        verify(pagoRepository).deleteById(1L);
    }

    @Test
    void eliminarPago_deberiaLanzarExcepcionCuandoNoExiste() {
        when(pagoRepository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> pagoService.eliminarPago(99L));
        verify(pagoRepository, never()).deleteById(any());
    }
}
