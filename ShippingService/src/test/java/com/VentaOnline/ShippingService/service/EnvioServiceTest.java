package com.VentaOnline.ShippingService.service;

import com.VentaOnline.ShippingService.client.PedidoClient;
import com.VentaOnline.ShippingService.dto.EnvioRequestDTO;
import com.VentaOnline.ShippingService.dto.EnvioResponseDTO;
import com.VentaOnline.ShippingService.dto.PedidoResponse;
import com.VentaOnline.ShippingService.model.Envio;
import com.VentaOnline.ShippingService.repository.EnvioRepository;
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
class EnvioServiceTest {

    @Mock
    private EnvioRepository envioRepository;

    @Mock
    private PedidoClient pedidoClient;

    @InjectMocks
    private EnvioService envioService;

    private EnvioRequestDTO requestDTO;
    private Envio testEnvio;
    private PedidoResponse pedidoResponse;
    private Envio envioPendiente;

    @BeforeEach
    void setUp() {
        requestDTO = EnvioRequestDTO.builder()
                .pedidoId(1L)
                .direccion("Av. Principal 123")
                .ciudad("Lima")
                .build();

        testEnvio = Envio.builder()
                .id(1L)
                .pedidoId(1L)
                .direccion("Av. Principal 123")
                .ciudad("Lima")
                .estado("PENDIENTE")
                .build();

        envioPendiente = Envio.builder()
                .id(2L)
                .pedidoId(1L)
                .direccion("Av. Principal 123")
                .ciudad("Lima")
                .estado("PENDIENTE")
                .build();

        pedidoResponse = new PedidoResponse(1L, "CONFIRMADO");
    }

    @Test
    void crearEnvio_deberiaCrearYRetornar() {
        when(pedidoClient.getPedidoById(1L)).thenReturn(pedidoResponse);
        when(envioRepository.save(any(Envio.class))).thenReturn(testEnvio);

        EnvioResponseDTO result = envioService.crearEnvio(requestDTO);

        assertNotNull(result);
        assertEquals(testEnvio.getDireccion(), result.getDireccion());
        assertEquals(testEnvio.getCiudad(), result.getCiudad());
        verify(envioRepository).save(any(Envio.class));
    }

    @Test
    void crearEnvio_deberiaLanzarExcepcionCuandoPedidoNoEstaConfirmado() {
        pedidoResponse.setEstado("PENDIENTE");
        when(pedidoClient.getPedidoById(1L)).thenReturn(pedidoResponse);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> envioService.crearEnvio(requestDTO));
        assertTrue(ex.getMessage().contains("no está confirmado"));
        verify(envioRepository, never()).save(any());
    }

    @Test
    void obtenerTodosEnvios_deberiaRetornarLista() {
        when(envioRepository.findAll()).thenReturn(List.of(testEnvio));

        List<EnvioResponseDTO> result = envioService.obtenerTodosEnvios();

        assertEquals(1, result.size());
    }

    @Test
    void obtenerEnvioById_deberiaRetornarEnvio() {
        when(envioRepository.findById(1L)).thenReturn(Optional.of(testEnvio));

        EnvioResponseDTO result = envioService.obtenerEnvioById(1L);

        assertNotNull(result);
        assertEquals(testEnvio.getDireccion(), result.getDireccion());
    }

    @Test
    void obtenerEnvioById_deberiaLanzarExcepcionCuandoNoExiste() {
        when(envioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> envioService.obtenerEnvioById(99L));
    }

    @Test
    void enviarEnvio_deberiaProcesarCorrectamente() {
        when(envioRepository.findById(2L)).thenReturn(Optional.of(envioPendiente));
        when(envioRepository.save(any(Envio.class))).thenReturn(envioPendiente);

        EnvioResponseDTO result = envioService.enviarEnvio(2L);

        assertNotNull(result);
        assertEquals("ENVIADO", envioPendiente.getEstado());
        assertNotNull(envioPendiente.getCodigoSeguimiento());
        verify(envioRepository).save(any(Envio.class));
    }

    @Test
    void enviarEnvio_deberiaLanzarExcepcionCuandoNoEstaPendiente() {
        testEnvio.setEstado("ENVIADO");
        when(envioRepository.findById(1L)).thenReturn(Optional.of(testEnvio));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> envioService.enviarEnvio(1L));
        assertTrue(ex.getMessage().contains("no está pendiente"));
        verify(envioRepository, never()).save(any());
    }

    @Test
    void actualizarEstadoEnvio_deberiaActualizarCorrectamente() {
        when(envioRepository.findById(1L)).thenReturn(Optional.of(testEnvio));
        when(envioRepository.save(any(Envio.class))).thenReturn(testEnvio);

        EnvioResponseDTO result = envioService.actualizarEstadoEnvio(1L, "ENVIADO");

        assertNotNull(result);
        verify(envioRepository).save(any(Envio.class));
    }

    @Test
    void actualizarEstadoEnvio_deberiaLanzarExcepcionCuandoEstadoInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> envioService.actualizarEstadoEnvio(1L, "INVALIDO"));
        verify(envioRepository, never()).save(any());
    }

    @Test
    void eliminarEnvio_deberiaEliminarCuandoExiste() {
        when(envioRepository.existsById(1L)).thenReturn(true);

        envioService.eliminarEnvio(1L);

        verify(envioRepository).deleteById(1L);
    }

    @Test
    void eliminarEnvio_deberiaLanzarExcepcionCuandoNoExiste() {
        when(envioRepository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> envioService.eliminarEnvio(99L));
        verify(envioRepository, never()).deleteById(any());
    }
}
