package com.VentaOnline.UserServices.service;

import com.VentaOnline.UserServices.dto.UsuarioRequestDTO;
import com.VentaOnline.UserServices.dto.UsuarioResponseDTO;
import com.VentaOnline.UserServices.model.Usuario;
import com.VentaOnline.UserServices.repository.UsuarioRepository;
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
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private UsuarioRequestDTO requestDTO;
    private Usuario testUser;

    @BeforeEach
    void setUp() {
        requestDTO = UsuarioRequestDTO.builder()
                .nombre("Juan Perez")
                .correo("juan@test.com")
                .build();

        testUser = Usuario.builder()
                .id(1L)
                .nombreCompleto("Juan Perez")
                .correo("juan@test.com")
                .build();
    }

    @Test
    void crearUsuario_deberiaCrearYRetornarUsuario() {
        when(usuarioRepository.existsByCorreo(requestDTO.getCorreo())).thenReturn(false);
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(testUser);

        UsuarioResponseDTO result = usuarioService.crearUsuario(requestDTO);

        assertNotNull(result);
        assertEquals(testUser.getNombreCompleto(), result.getNombreCompleto());
        assertEquals(testUser.getCorreo(), result.getCorreo());
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void crearUsuario_deberiaLanzarExcepcionCuandoCorreoYaExiste() {
        when(usuarioRepository.existsByCorreo(requestDTO.getCorreo())).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.crearUsuario(requestDTO));
        assertTrue(ex.getMessage().contains("registrado"));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void obtenerUsuarios_deberiaRetornarLista() {
        when(usuarioRepository.findAll()).thenReturn(List.of(testUser));

        List<UsuarioResponseDTO> result = usuarioService.obtenerUsuarios();

        assertEquals(1, result.size());
        assertEquals(testUser.getCorreo(), result.get(0).getCorreo());
    }

    @Test
    void obtenerUsuarioPorId_deberiaRetornarUsuario() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(testUser));

        UsuarioResponseDTO result = usuarioService.obtenerUsuarioPorId(1L);

        assertNotNull(result);
        assertEquals(testUser.getNombreCompleto(), result.getNombreCompleto());
    }

    @Test
    void obtenerUsuarioPorId_deberiaLanzarExcepcionCuandoNoExiste() {
        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> usuarioService.obtenerUsuarioPorId(99L));
    }

    @Test
    void actualizarUsuario_deberiaActualizarCorrectamente() {
        UsuarioRequestDTO updateRequest = UsuarioRequestDTO.builder()
                .nombre("Juan Actualizado")
                .correo("juan@test.com")
                .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(testUser);

        UsuarioResponseDTO result = usuarioService.actualizarUsuario(1L, updateRequest);

        assertNotNull(result);
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void actualizarUsuario_deberiaLanzarExcepcionCuandoNuevoCorreoYaExiste() {
        UsuarioRequestDTO updateRequest = UsuarioRequestDTO.builder()
                .nombre("Juan Actualizado")
                .correo("otro@test.com")
                .build();

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(usuarioRepository.existsByCorreo("otro@test.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.actualizarUsuario(1L, updateRequest));
        assertTrue(ex.getMessage().contains("registrado"));
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    void eliminarUsuario_deberiaEliminarCuandoExiste() {
        when(usuarioRepository.existsById(1L)).thenReturn(true);

        usuarioService.eliminarUsuario(1L);

        verify(usuarioRepository).deleteById(1L);
    }

    @Test
    void eliminarUsuario_deberiaLanzarExcepcionCuandoNoExiste() {
        when(usuarioRepository.existsById(99L)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> usuarioService.eliminarUsuario(99L));
        verify(usuarioRepository, never()).deleteById(any());
    }
}
