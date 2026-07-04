package com.VentaOnline.GatewayService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("dev")
class GatewayServiceApplicationTests {

    @Autowired
    private RouteLocator routeLocator;

    @Test
    void contextLoads() {
    }

    @Test
    void shouldLoadAllServiceRoutes() {
        List<Route> routes = routeLocator.getRoutes().collectList().block();
        assertNotNull(routes, "Las rutas no deberían ser nulas");
        assertEquals(11, routes.size(),
            "El Gateway debe tener 11 rutas configuradas (1 por cada microservicio)");
    }

    @Test
    void shouldContainUserServiceRoute() {
        List<Route> routes = routeLocator.getRoutes().collectList().block();
        assertNotNull(routes);
        boolean hasUserRoute = routes.stream()
            .anyMatch(r -> r.getId().equals("user-service"));
        assertTrue(hasUserRoute, "Debe existir la ruta para user-service");
    }

    @Test
    void shouldContainAllExpectedRouteIds() {
        List<Route> routes = routeLocator.getRoutes().collectList().block();
        assertNotNull(routes);

        List<String> expectedRouteIds = List.of(
            "user-service", "category-service", "product-service",
            "inventory-service", "order-service", "payment-service",
            "shipping-service", "cart-service", "discount-service",
            "auth-service", "notification-service"
        );

        List<String> actualRouteIds = routes.stream()
            .map(Route::getId)
            .toList();

        for (String expectedId : expectedRouteIds) {
            assertTrue(actualRouteIds.contains(expectedId),
                "Falta la ruta: " + expectedId);
        }
    }
}
