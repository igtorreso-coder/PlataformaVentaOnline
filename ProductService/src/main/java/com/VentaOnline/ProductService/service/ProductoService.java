package com.VentaOnline.ProductService.service;

import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import com.VentaOnline.ProductService.client.CategoriaClient;
import com.VentaOnline.ProductService.dto.CategoriaResponse;
import com.VentaOnline.ProductService.dto.ProductoRequestDTO;
import com.VentaOnline.ProductService.dto.ProductoResponseDTO;
import com.VentaOnline.ProductService.model.Producto;
import com.VentaOnline.ProductService.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@AllArgsConstructor
public class ProductoService {
    private final ProductoRepository productoRepository;
    private final CategoriaClient categoriaClient;

    public List<ProductoResponseDTO> obtenerTodosProductos() {
        log.info("Obteniendo todos los productos");
        return productoRepository.findAll().stream()
                .map(producto -> toResponse(producto, getCategoriaNombre(producto.getCategoriaId())))
                .toList();
    }

    public ProductoResponseDTO obtenerProductoById(Long id) {
        log.info("Obteniendo producto con ID: {}", id);
        return productoRepository.findById(id)
                .map(producto -> toResponse(producto, getCategoriaNombre(producto.getCategoriaId())))
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado con ID: " + id));
    }

    public List<ProductoResponseDTO> obtenerProductosByCategoria(Long categoriaId) {
        log.info("Obteniendo productos por categoría ID: {}", categoriaId);
        return productoRepository.findByCategoriaId(categoriaId).stream()
                .map(producto -> toResponse(producto, getCategoriaNombre(producto.getCategoriaId())))
                .toList();
    }

    public ProductoResponseDTO crearProducto(ProductoRequestDTO request) {
        log.info("Creando nuevo producto: {}", request.getNombre());
        CategoriaResponse categoria = categoriaClient.getCategoriaById(request.getCategoriaId());
        Producto producto = Producto.builder()
                .nombre(request.getNombre())
                .descripcion(request.getDescripcion())
                .precio(request.getPrecio())
                .categoriaId(request.getCategoriaId())
                .stock(request.getStock())
                .build();
        producto = productoRepository.save(producto);
        return toResponse(producto, categoria.getNombre());
    }

    public ProductoResponseDTO actualizarProducto(Long id, ProductoRequestDTO request) {
        log.info("Actualizando producto con ID: {}", id);
        Producto existing = productoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Producto no encontrado con ID: " + id));
        CategoriaResponse categoria = categoriaClient.getCategoriaById(request.getCategoriaId());
        existing.setNombre(request.getNombre());
        existing.setDescripcion(request.getDescripcion());
        existing.setPrecio(request.getPrecio());
        existing.setCategoriaId(request.getCategoriaId());
        existing.setStock(request.getStock());
        return toResponse(productoRepository.save(existing), categoria.getNombre());
    }

    public void eliminarProducto(Long id) {
        log.info("Eliminando producto con ID: {}", id);
        if (!productoRepository.existsById(id)) {
            throw new NoSuchElementException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }

    public List<CategoriaResponse> obtenerCategorias() {
        log.info("Consultando microservicio de categorías desde productos");
        return categoriaClient.getCategorias();
    }

    private String getCategoriaNombre(Long categoriaId) {
        try {
            CategoriaResponse categoria = categoriaClient.getCategoriaById(categoriaId);
            return categoria.getNombre();
        } catch (RuntimeException e) {
            log.warn("No se pudo obtener el nombre de la categoría ID {}: {}", categoriaId, e.getMessage());
            return "Categoría ID: " + categoriaId;
        }
    }

    private ProductoResponseDTO toResponse(Producto producto, String categoriaNombre) {
        return ProductoResponseDTO.builder()
                .id(producto.getId())
                .nombre(producto.getNombre())
                .descripcion(producto.getDescripcion())
                .precio(producto.getPrecio())
                .categoriaNombre(categoriaNombre)
                .stock(producto.getStock())
                .createdAt(producto.getCreatedAt())
                .updatedAt(producto.getUpdatedAt())
                .build();
    }
}
