package com.VentaOnline.CartService.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import com.VentaOnline.CartService.dto.ProductoResponse;
import jakarta.annotation.PostConstruct;

@Component
public class ProductoClient {

    @Autowired
    private RestClient restClient;

    @Value("${servicios.productos.url}")
    private String productosUrl;

    private RestClient productosRestClient;

    @PostConstruct
    public void init() {
        this.productosRestClient = this.restClient.mutate().baseUrl(this.productosUrl).build();
    }

    public ProductoResponse obtenerProducto(Long id) {
        return productosRestClient.get()
                .uri("/api/productos/{id}", id)
                .retrieve()
                .body(ProductoResponse.class);
    }
}
