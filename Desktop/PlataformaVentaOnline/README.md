# PlataformaVentaOnline

**Autor:** Ignacio Torres

## 1. Qué es el proyecto

Sistema de e-commerce basado en **12 microservicios independientes**, cada uno con su propia base de datos MySQL. La arquitectura está diseñada para que un fallo en un servicio no afecte al resto del sistema.

**Problema que resuelve:** En un monolito, si falla el módulo de pagos, toda la plataforma cae. Con microservicios, cada dominio opera de forma aislada.

---

## 2. Arquitectura del sistema

```
Cliente (Postman/Swagger)
        │
        ▼
┌─────────────────┐
│  GatewayService  │ ← Puerto 8080 (punto de entrada único)
│  Spring Cloud    │
│  Gateway         │
└────────┬────────┘
         │ Rutas: /api/usuarios/**, /api/productos/**, etc.
         │
    ┌────┴────┬──────────┬──────────┬──────────┐
    ▼         ▼          ▼          ▼          ▼
┌────────┐┌────────┐┌────────┐┌────────┐┌────────┐
│ User   ││Product ││ Order  ││ Payment││  Cart  │
│Service ││Service ││Service ││Service ││Service │
│ :8081  ││ :8083  ││ :8085  ││ :8086  ││ :8088  │
└───┬────┘└───┬────┘└───┬────┘└───┬────┘└───┬────┘
    │         │         │         │         │
    ▼         ▼         ▼         ▼         ▼
  MySQL     MySQL     MySQL     MySQL     MySQL
  (usr_db)  (prod_db) (ped_db)  (pag_db)  (car_db)
```

**Cada microservicio tiene:**
- Su propia base de datos MySQL
- Patrón CSR (Controller → Service → Repository)
- Validaciones con Bean Validation
- Manejo centralizado de excepciones
- Documentación Swagger propia
- Pruebas unitarias con JUnit + Mockito

---

## 3. Los 12 microservicios

| # | Servicio | Puerto | Qué hace | Con quién se comunica |
|---|----------|--------|----------|----------------------|
| 1 | **GatewayService** | 8080 | Enrutamiento centralizado, CORS, Swagger unificado | Todos (enruta) |
| 2 | **UserServices** | 8081 | CRUD de usuarios | CategoryService |
| 3 | **AuthService** | 8090 | Login, registro, tokens | UserServices |
| 4 | **CategoryService** | 8082 | CRUD de categorías | UserServices |
| 5 | **ProductService** | 8083 | CRUD de productos | CategoryService |
| 6 | **InventoryService** | 8084 | Control de stock | ProductService |
| 7 | **CartService** | 8088 | Carrito de compras | UserServices, ProductService |
| 8 | **OrderService** | 8085 | Orquestador de pedidos | UserServices, ProductService |
| 9 | **PaymentService** | 8086 | Procesamiento de pagos | OrderService |
| 10 | **ShippingService** | 8087 | Guías de despacho | OrderService |
| 11 | **DiscountService** | 8089 | Cupones y descuentos | — |
| 12 | **NotificationService** | 8091 | Notificaciones del sistema | UserServices |

---

## 4. Flujo de negocio (cómo funciona una venta)

```
1. Usuario se loguea          → AuthService valida credenciales
2. Navega el catálogo         → ProductService + CategoryService
3. Agrega al carrito          → CartService guarda items
4. Confirma la compra         → OrderService orquesta todo:
   │
   ├─ Valida stock            → InventoryService descuenta
   ├─ Aplica cupón            → DiscountService calcula
   ├─ Procesa pago            → PaymentService aprueba/rechaza
   ├─ Genera envío            → ShippingService crea guía
   └─ Envía notificación      → NotificationService alerta
```

---

## 5. Tecnologías principales

| Capa | Tecnología | Para qué |
|------|-----------|----------|
| **Framework** | Spring Boot 3.2.5 + Java 17 | Base del proyecto |
| **Persistencia** | JPA + Hibernate | ORM y mapeo objeto-relacional |
| **Base de datos** | MySQL 8.0 (1 por servicio) | Almacenamiento |
| **Migraciones** | Flyway | Scripts SQL automáticos al iniciar |
| **Validaciones** | Bean Validation (JSR 380) | @NotBlank, @NotNull, @Size, @Email |
| **Excepciones** | @RestControllerAdvice | GlobalExceptionHandler |
| **Comunicación** | WebClient (Spring WebFlux) | Llamadas síncronas entre servicios |
| **Gateway** | Spring Cloud Gateway | Enrutamiento y CORS |
| **Documentación** | springdoc-openapi 2.5.0 | Swagger UI |
| **Pruebas** | JUnit 5 + Mockito | Tests unitarios |
| **Logs** | SLF4J con Lombok @Slf4j | Trazabilidad |
| **Despliegue** | Docker Compose | Orquestación de contenedores |

---

## 6. Comunicación entre microservicios

Los servicios se comunican **síncronamente** mediante WebClient:

```java
// Ejemplo: OrderService consulta a UserServices
usersWebClient.get()
    .uri("/api/usuarios/{id}", usuarioId)
    .retrieve()
    .bodyToMono(UsuarioResponse.class)
    .block();
```

**Manejo de errores:**
- `WebClientResponseException` captura errores HTTP
- Cada service-client maneja 404 (no encontrado) y errores generales
- Los DTOs separan la respuesta interna del modelo expuesto

---

## 7. Estructura CSR de cada microservicio

```
src/main/java/com/VentaOnline/[Service]/
├── controller/    ← Recibe peticiones REST, valida con @Valid
├── service/       ← Lógica de negocio, reglas, validaciones
├── repository/    ← JpaRepository para acceso a datos
├── model/         ← Entidades JPA (@Entity)
├── dto/           ← Objetos de transferencia de datos
├── client/        ← Comunicación con otros servicios (WebClient)
├── config/        ← WebClientConfig, OpenApiConfig
└── exception/     ← GlobalExceptionHandler (@RestControllerAdvice)
```

**Flujo de una petición:**
```
HTTP Request → Controller (@Valid) → Service (lógica) → Repository (JPA) → MySQL
                                   ↓
                            Si necesita otro servicio:
                            Client → WebClient → Otro microservicio
```

---

## 8. API Gateway

**Puerto:** 8080

Todas las peticiones pasan por el Gateway. Rutas configuradas:

| Ruta del Gateway | Servicio destino |
|-----------------|-----------------|
| `/api/usuarios/**` | UserServices :8081 |
| `/api/categorias/**` | CategoryService :8082 |
| `/api/productos/**` | ProductService :8083 |
| `/api/inventarios/**` | InventoryService :8084 |
| `/api/pedidos/**` | OrderService :8085 |
| `/api/pagos/**` | PaymentService :8086 |
| `/api/envios/**` | ShippingService :8087 |
| `/api/carritos/**` | CartService :8088 |
| `/api/descuentos/**` | DiscountService :8089 |
| `/api/auth/**` | AuthService :8090 |
| `/api/notificaciones/**` | NotificationService :8091 |

**Swagger unificado:** `http://localhost:8080/swagger-ui.html`

---

## 9. Documentación Swagger

Cada servicio tiene su documentación individual, pero está **unificada a través del Gateway**:

| Swagger UI | URL |
|-----------|-----|
| Todos los servicios | `http://localhost:8080/swagger-ui.html` |
| Solo Usuarios | `http://localhost:8081/swagger-ui.html` |
| Solo Productos | `http://localhost:8083/swagger-ui.html` |
| Solo Pedidos | `http://localhost:8085/swagger-ui.html` |

**Configuración:** `springdoc-openapi-starter-webmvc-ui:2.5.0` en cada pom.xml + `OpenApiConfig.java` con título y URL del Gateway.

---

## 10. Pruebas unitarias

**Framework:** JUnit 5 + Mockito

**Estructura de cada test:**
```java
@ExtendWith(MockitoExtension.class)
class PedidoServiceTest {

    @Mock
    private PedidoRepository pedidoRepository;

    @InjectMocks
    private PedidoService pedidoService;

    @Test
    void crearPedido_DevuelvePedidoCreado() {
        // Given
        PedidoRequestDTO request = new PedidoRequestDTO();
        when(pedidoRepository.save(any())).thenReturn(pedido);

        // When
        PedidoResponseDTO resultado = pedidoService.crear(request);

        // Then
        assertNotNull(resultado);
        verify(pedidoRepository).save(any());
    }
}
```

**Qué se testea:**
- Operaciones CRUD exitosas
- Errores y excepciones esperadas
- Validaciones de datos
- Interacciones con repositorios (mocks)

---

## 11. Configuración YAML y Perfiles

**Estructura de archivos en cada servicio:**
```
src/main/resources/
├── application.yml          ← Config base (nombre app, flyway)
├── application-dev.yml      ← Perfil desarrollo (H2, puertos locales)
├── application-prod.yml     ← Perfil producción (MySQL, puertos Docker)
└── db/migration/
    ├── V1__create_tables.sql
    └── V2__seed_data.sql
```

**Variables importantes:**
- `spring.profiles.active: prod` (en docker-compose)
- `spring.datasource.url` por servicio
- `springdoc.swagger-ui.urls` para Swagger unificado

---

## 12. Despliegue con Docker

**Ejecutar todo el sistema:**
```bash
docker compose up -d
```

**Archivos:**
- `docker-compose.yml` → Define 12 servicios + MySQL
- `mysql/init/01-databases.sql` → Crea las 11 bases de datos
- Cada servicio usa `maven:3.9-eclipse-temurin-17` para compilar

**Puertos del sistema:**

| Servicio | Puerto |
|----------|--------|
| Gateway | 8080 |
| MySQL | 3307 |
| UserServices | 8081 |
| CategoryService | 8082 |
| ProductService | 8083 |
| InventoryService | 8084 |
| OrderService | 8085 |
| PaymentService | 8086 |
| ShippingService | 8087 |
| CartService | 8088 |
| DiscountService | 8089 |
| AuthService | 8090 |
| NotificationService | 8091 |

---

## 13. Datos de prueba

**Credenciales:**
| Correo | Contraseña | Rol |
|--------|-----------|-----|
| ignacio@email.com | 123456 | ADMIN |
| maria@email.com | 123456 | USER |
| juan@email.com | 123456 | USER |

**Datos iniciales (Flyway V2):**
- 5 usuarios
- 5 categorías (Electrónica, Ropa, Hogar, Deportes, Libros)
- 18 productos
- 4 pedidos con diferentes estados
- 4 pagos (3 aprobados, 1 pendiente)
- 4 envíos
- 4 cupones de descuento
- 5 notificaciones

---

## 14. Requisitos

- **JDK 17** (para ejecución local)
- **Docker Desktop** (para docker-compose)
- **Puerto 3307** libre (MySQL)
- **Internet** (para descargar dependencias Maven la primera vez)
