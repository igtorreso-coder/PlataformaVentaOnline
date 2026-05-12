package com.VentaOnline.CategoryService.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.VentaOnline.CategoryService.client.UserClient;
import com.VentaOnline.CategoryService.dto.CategoriaRequestDTO;
import com.VentaOnline.CategoryService.dto.CategoriaResponseDTO;
import com.VentaOnline.CategoryService.dto.UsuarioDTO;
import com.VentaOnline.CategoryService.model.Categoria;
import com.VentaOnline.CategoryService.repository.CategoriaRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class CategoriaService {
    private final CategoriaRepository categoriaRepository;
    private final UserClient userClient;

    public CategoriaResponseDTO crearCategoria(CategoriaRequestDTO request) {
        log.info("Creando nueva categoría: {}", request.getNombre());
        if (categoriaRepository.existsByNombre(request.getNombre())) {
            throw new IllegalArgumentException("La categoría '" + request.getNombre() + "' ya existe");
        }
        Categoria categoria = Categoria.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .build();
        categoria = categoriaRepository.save(categoria);
        return toResponse(categoria);
    }

    public List<CategoriaResponseDTO> conseguirTodasCategorias() {
        log.info("Obteniendo todas las categorías");
        return categoriaRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    public CategoriaResponseDTO buscarCategoriaById(Long id) {
        log.info("Obteniendo categoría con ID: {}", id);
        return categoriaRepository.findById(id)
                .map(this::toResponse)
                .orElseThrow(() -> new NoSuchElementException("Categoría no encontrada con ID: " + id));
    }

    public CategoriaResponseDTO actualizarCategoria(Long id, CategoriaRequestDTO request) {
        log.info("Actualizando categoría con ID: {}", id);
        Categoria existing = categoriaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Categoría no encontrada con ID: " + id));
        if (!existing.getNombre().equals(request.getNombre())
                && categoriaRepository.existsByNombre(request.getNombre())) {
            throw new IllegalArgumentException("El nombre '" + request.getNombre() + "' ya está registrado");
        }
        existing.setNombre(request.getNombre());
        existing.setDescripcion(request.getDescripcion());
        return toResponse(categoriaRepository.save(existing));
    }

    public void eliminarCategoria(Long id) {
        log.info("Eliminando categoría con ID: {}", id);
        if (!categoriaRepository.existsById(id)) {
            throw new NoSuchElementException("Categoría no encontrada con ID: " + id);
        }
        categoriaRepository.deleteById(id);
    }

    public List<UsuarioDTO> obtenerUsuariosDelMicroservicio() {
        log.info("Consultando microservicio de usuarios");
        return userClient.getUsers();
    }

    private CategoriaResponseDTO toResponse(Categoria categoria) {
        return CategoriaResponseDTO.builder()
                .id(categoria.getId())
                .nombre(categoria.getNombre())
                .descripcion(categoria.getDescripcion())
                .createdAt(categoria.getCreatedAt())
                .updatedAt(categoria.getUpdatedAt())
                .build();
    }
}
