package com.VentaOnline.UserServices.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.VentaOnline.UserServices.client.CategoriaClient;
import com.VentaOnline.UserServices.dto.CategoriaDTO;
import com.VentaOnline.UserServices.dto.UsuarioRequestDTO;
import com.VentaOnline.UserServices.dto.UsuarioResponseDTO;
import com.VentaOnline.UserServices.model.Usuario;
import com.VentaOnline.UserServices.repository.UsuarioRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Slf4j
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CategoriaClient categoriaClient;

    @Transactional
    public UsuarioResponseDTO crearUsuario(UsuarioRequestDTO request) {
        log.info("Creando nuevo usuario con email: {}", request.getCorreo());
        if (usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("El correo '" + request.getCorreo() + "' ya está registrado");
        }
        Usuario usuario = Usuario.builder()
                .nombreCompleto(request.getNombre())
                .correo(request.getCorreo())
                .build();
        usuario = usuarioRepository.save(usuario);
        return toResponse(usuario);
    }

    public List<UsuarioResponseDTO> obtenerUsuarios() {
        log.info("Obteniendo todos los usuarios");
        return usuarioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public UsuarioResponseDTO obtenerUsuarioById(Long id) {
        log.info("Obteniendo usuario con ID: {}", id);
        return usuarioRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con ID: " + id));
    }

    @Transactional
    public UsuarioResponseDTO actualizarUsuario(Long id, UsuarioRequestDTO request) {
        log.info("Actualizando usuario con ID: {}", id);
        Usuario existing = usuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Usuario no encontrado con ID: " + id));
        if (!existing.getCorreo().equals(request.getCorreo())
                && usuarioRepository.existsByCorreo(request.getCorreo())) {
            throw new IllegalArgumentException("El correo '" + request.getCorreo() + "' ya está registrado");
        }
        existing.setNombreCompleto(request.getNombre());
        existing.setCorreo(request.getCorreo());
        return toResponse(usuarioRepository.save(existing));
    }

    @Transactional
    public void eliminarUsuario(Long id) {
        log.info("Eliminando usuario con ID: {}", id);
        if (!usuarioRepository.existsById(id)) {
            throw new NoSuchElementException("Usuario no encontrado con ID: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    public List<CategoriaDTO> obtenerCategorias() {
        log.info("Consultando microservicio de categorías");
        return categoriaClient.getCategorias();
    }

    private UsuarioResponseDTO toResponse(Usuario usuario) {
        return UsuarioResponseDTO.builder()
                .id(usuario.getId())
                .nombreCompleto(usuario.getNombreCompleto())
                .correo(usuario.getCorreo())
                .createdAt(usuario.getCreatedAt())
                .updatedAt(usuario.getUpdatedAt())
                .build();
    }
}
