package com.VentaOnline.CategoryService.controller;

import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.VentaOnline.CategoryService.dto.CategoriaRequestDTO;
import com.VentaOnline.CategoryService.dto.CategoriaResponseDTO;
import com.VentaOnline.CategoryService.dto.UsuarioDTO;
import com.VentaOnline.CategoryService.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@RestController
@RequestMapping("/api/categorias")
@Slf4j
@Tag(name = "Categorías", description = "Endpoints para gestión de categorías de productos")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    @Operation(summary = "Obtener todas las categorías", description = "Retorna la lista completa de categorías de productos")
    @ApiResponse(responseCode = "200", description = "Lista de categorías")
    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> obtenerCategorias() {
        log.info("GET /api/categorias");
        return ResponseEntity.ok(categoriaService.conseguirTodasCategorias());
    }

    @Operation(summary = "Obtener categoría por ID", description = "Retorna los detalles de una categoría por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría encontrada"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> obtenerCategoria(@PathVariable Long id) {
        log.info("GET /api/categorias/{}", id);
        return ResponseEntity.ok(categoriaService.buscarCategoriaById(id));
    }

    @Operation(summary = "Crear nueva categoría", description = "Crea una nueva categoría de producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Categoría creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de categoría inválidos")
    })
    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> crearCategoria(@Valid @RequestBody CategoriaRequestDTO request) {
        log.info("POST /api/categorias");
        CategoriaResponseDTO categoria = categoriaService.crearCategoria(request);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(categoria.getId()).toUri();
        return ResponseEntity.created(location).body(categoria);
    }

    @Operation(summary = "Actualizar categoría", description = "Actualiza los datos de una categoría existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Categoría actualizada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponseDTO> actualizarCategoria(@PathVariable Long id,
            @Valid @RequestBody CategoriaRequestDTO request) {
        log.info("PUT /api/categorias/{}", id);
        return ResponseEntity.ok(categoriaService.actualizarCategoria(id, request));
    }

    @Operation(summary = "Eliminar categoría", description = "Elimina una categoría del sistema por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Categoría eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Categoría no encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Long id) {
        log.info("DELETE /api/categorias/{}", id);
        categoriaService.eliminarCategoria(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Obtener usuarios", description = "Retorna la lista de usuarios obtenidos desde el microservicio de usuarios")
    @ApiResponse(responseCode = "200", description = "Lista de usuarios")
    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioDTO>> obtenerUsuarios() {
        log.info("GET /api/categorias/usuarios");
        return ResponseEntity.ok(categoriaService.obtenerUsuariosDelMicroservicio());
    }
}
