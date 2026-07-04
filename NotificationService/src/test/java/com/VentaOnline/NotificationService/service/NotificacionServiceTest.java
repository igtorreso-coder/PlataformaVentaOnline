package com.VentaOnline.NotificationService.service;

import com.VentaOnline.NotificationService.client.UserServiceClient;
import com.VentaOnline.NotificationService.dto.NotificacionRequestDTO;
import com.VentaOnline.NotificationService.dto.NotificacionResponseDTO;
import com.VentaOnline.NotificationService.model.Notificacion;
import com.VentaOnline.NotificationService.repository.NotificacionRepository;
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
class NotificacionServiceTest {

    @Mock
    private NotificacionRepository notificacionRepository;

    @Mock
    private UserServiceClient userServiceClient;

    @InjectMocks
    private NotificacionService notificacionService;

    private NotificacionRequestDTO requestDTO;
    private Notificacion notificacionPendiente;
    private Notificacion notificacionEnviada;

    @BeforeEach
    void setUp() {
        requestDTO = NotificacionRequestDTO.builder()
                .usuarioId(1L)
                .tipo("EMAIL")
                .asunto("Bienvenido")
                .mensaje("Gracias por registrarte")
                .destinatario("juan@test.com")
                .build();

        notificacionPendiente = Notificacion.builder()
                .id(1L)
                .usuarioId(1L)
                .tipo("EMAIL")
                .asunto("Bienvenido")
                .mensaje("Gracias por registrarte")
                .destinatario("juan@test.com")
                .estado("PENDIENTE")
                .build();

        notificacionEnviada = Notificacion.builder()
                .id(2L)
                .usuarioId(1L)
                .tipo("EMAIL")
                .asunto("Bienvenido")
                .mensaje("Gracias por registrarte")
                .destinatario("juan@test.com")
                .estado("ENVIADO")
                .build();
    }

    @Test
    void crearNotificacion_deberiaCrearYRetornar() {
        doNothing().when(userServiceClient).obtenerUsuario(1L);
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacionPendiente);

        NotificacionResponseDTO result = notificacionService.crearNotificacion(requestDTO);

        assertNotNull(result);
        assertEquals("EMAIL", result.getTipo());
        assertEquals("PENDIENTE", result.getEstado());
        verify(notificacionRepository).save(any(Notificacion.class));
    }

    @Test
    void crearNotificacion_deberiaLanzarExcepcionCuandoTipoEsInvalido() {
        requestDTO.setTipo("INVALIDO");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> notificacionService.crearNotificacion(requestDTO));
        assertTrue(ex.getMessage().contains("Tipo inválido"));
        verify(notificacionRepository, never()).save(any());
    }

    @Test
    void obtenerTodasNotificaciones_deberiaRetornarLista() {
        when(notificacionRepository.findAll()).thenReturn(List.of(notificacionPendiente));

        List<NotificacionResponseDTO> result = notificacionService.obtenerTodasNotificaciones();

        assertEquals(1, result.size());
    }

    @Test
    void obtenerNotificacionPorId_deberiaRetornarNotificacion() {
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacionPendiente));

        NotificacionResponseDTO result = notificacionService.obtenerNotificacionPorId(1L);

        assertNotNull(result);
        assertEquals(notificacionPendiente.getAsunto(), result.getAsunto());
    }

    @Test
    void obtenerNotificacionPorId_deberiaLanzarExcepcionCuandoNoExiste() {
        when(notificacionRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> notificacionService.obtenerNotificacionPorId(99L));
    }

    @Test
    void enviarNotificacion_deberiaCambiarEstadoAEnviado() {
        when(notificacionRepository.findById(1L)).thenReturn(Optional.of(notificacionPendiente));
        when(notificacionRepository.save(any(Notificacion.class))).thenReturn(notificacionPendiente);

        NotificacionResponseDTO result = notificacionService.enviarNotificacion(1L);

        assertNotNull(result);
        assertEquals("ENVIADO", notificacionPendiente.getEstado());
        assertNotNull(notificacionPendiente.getFechaEnvio());
        verify(notificacionRepository).save(any(Notificacion.class));
    }

    @Test
    void enviarNotificacion_deberiaLanzarExcepcionCuandoYaFueProcesada() {
        when(notificacionRepository.findById(2L)).thenReturn(Optional.of(notificacionEnviada));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> notificacionService.enviarNotificacion(2L));
        assertTrue(ex.getMessage().contains("ya fue procesada"));
        verify(notificacionRepository, never()).save(any());
    }

    @Test
    void eliminarNotificacion_deberiaEliminarCuandoExiste() {
        when(notificacionRepository.existsById(1L)).thenReturn(true);

        notificacionService.eliminarNotificacion(1L);

        verify(notificacionRepository).deleteById(1L);
    }

    @Test
    void eliminarNotificacion_deberiaLanzarExcepcionCuandoNoExiste() {
        when(notificacionRepository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class,
                () -> notificacionService.eliminarNotificacion(99L));
        verify(notificacionRepository, never()).deleteById(any());
    }
}
