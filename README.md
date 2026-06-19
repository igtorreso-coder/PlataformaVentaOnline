# PlataformaVentaOnline - Sistema de Gestión de E-commerce

Autor y Único Participante: **Ignacio Torres**
Bienvenidos a Plataforma de Venta Online, una solución integral de comercio electrónico diseñada para modernizar la gestión de ventas y logística mediante una arquitectura basada en microservicios.

En los sistemas tradicionales (monolitos), un fallo en el módulo de pagos puede hacer caer toda la plataforma, impidiendo incluso que los usuarios vean el catálogo. Para solucionar este problema de escalabilidad y tolerancia a fallos, este proyecto implementa una arquitectura distribuida utilizando Java + Spring Boot. Aquí, cada dominio del negocio opera de forma independiente, asegurando que un alto volumen de ventas o despachos no afecte la experiencia general del cliente.

## Propósito y Justificación de la Arquitectura

El propósito central de este proyecto es demostrar la capacidad técnica para diseñar, construir y justificar una arquitectura distribuida basada en microservicios.

Opté por este enfoque porque un E-commerce requiere manejar dominios de datos muy diferentes (un producto no tiene el mismo ciclo de vida que una factura o un envío). Al separar la plataforma en 12 servicios independientes, se logró:

- **Aislamiento de Bases de Datos**: Cada servicio gestiona sus propios datos, garantizando integridad y evitando bloqueos (locks) masivos en una única base central.
- **Resiliencia**: Si el servicio de notificaciones falla, la venta se procesa de igual manera, manteniendo la operatividad crítica del negocio.
- **Escalabilidad Horizontal**: En fechas de alta demanda, se pueden levantar más instancias del servicio de `Pedidos` sin necesidad de escalar servicios menos críticos como el de `Categorías`.

## Tecnologías Implementadas

- **Persistencia** (`JPA` + `Hibernate`): Cada microservicio posee su propio esquema relacional (MySQL), configurado en su respectivo `application.yml`. Las operaciones CRUD se realizan mediante `JpaRepository`.
- **Migraciones** (`Flyway`): Al iniciar cada aplicación, Flyway ejecuta automáticamente los scripts SQL asegurando que la base de datos siempre esté sincronizada con el código.
- **Validación** (Bean Validation - JSR 380): Se utilizan DTOs con anotaciones como `@NotNull` o `@Positive` para garantizar datos correctos desde el controlador.
- **Manejo Centralizado de Excepciones**: `@ControllerAdvice` (GlobalExceptionHandler) en cada servicio, retornando JSON estructurado con códigos HTTP correctos (400, 404, 409, 422).
- **Patrón CSR**: El Controller recibe la petición REST, el Service ejecuta la regla de negocio y el Repository persiste el dato.
- **Comunicación Síncrona con `RestClient`**: Los servicios se comunican entre sí mediante RestClient con beans específicos por servicio.
- **Documentación con Swagger/OpenAPI**: Cada microservicio expone su documentación interactiva vía springdoc-openapi.
- **API Gateway** (Spring Cloud Gateway): Centraliza el enrutamiento hacia los 11 microservicios internos.
- **Pruebas Unitarias**: JUnit 5 + Mockito con cobertura sobre la capa de servicio.
- **Trazabilidad** (SLF4J): Logs estructurados en las capas de servicio para auditar cada transacción.

## Flujo de Negocio

Para entender la integración de los microservicios, este es el viaje de un pedido en la plataforma:

- El usuario se autentica (`AuthService`) y navega por el catálogo, filtrando artículos (`ProductService` y `CategoryService`).
- El cliente añade productos a su sesión temporal (`CartService`).
- Al proceder al pago, el carrito se envía al orquestador principal (`OrderService`).
- `OrderService` realiza tres acciones vitales:
  - Valida y descuenta físicamente el stock en el `InventoryService`.
  - Si se ingresó un cupón, calcula el nuevo monto en el `DiscountService`.
  - Envía el monto final a la pasarela simulada (`PaymentService`).
- Si el pago es APROBADO, la orden se marca como confirmada, se genera la guía de despacho en `ShippingService` y se registra una alerta de éxito en `NotificationService`.

## Composición del Sistema (Microservicios)

El ecosistema se compone de los siguientes 12 módulos independientes:

| # | Servicio | Puerto | Descripción |
|---|----------|--------|-------------|
| 1 | **GatewayService** | `8080` | API Gateway que centraliza y enruta todas las solicitudes a los microservicios internos |
| 2 | **UserServices** | `8081` | Gestión de perfiles de clientes registrados |
| 3 | **CategoryService** | `8082` | Clasificación y agrupación del catálogo de productos |
| 4 | **ProductService** | `8083` | Catálogo central de productos; se comunica con CategoryService |
| 5 | **InventoryService** | `8084` | Control de existencias y movimientos de stock |
| 6 | **OrderService** | `8085` | Cerebro de la operación: orquesta pedidos, inventario, descuentos y pagos |
| 7 | **PaymentService** | `8086` | Pasarela de pagos simulada |
| 8 | **ShippingService** | `8087` | Logística post-venta: guías de despacho y seguimiento |
| 9 | **CartService** | `8088` | Sesión temporal de compra del cliente |
| 10 | **DiscountService** | `8089` | Promociones, cupones y validación de descuentos |
| 11 | **AuthService** | `8090` | Autenticación, registro y emisión de tokens |
| 12 | **NotificationService** | `8091` | Notificaciones y alertas de eventos del sistema |

## Rutas del API Gateway

El Gateway (`http://localhost:8080`) expone las siguientes rutas:

| Ruta | Servicio Destino |
|------|-----------------|
| `/api/usuarios/**` | UserServices (`:8081`) |
| `/api/categorias/**` | CategoryService (`:8082`) |
| `/api/productos/**` | ProductService (`:8083`) |
| `/api/inventarios/**` | InventoryService (`:8084`) |
| `/api/pedidos/**` | OrderService (`:8085`) |
| `/api/pagos/**` | PaymentService (`:8086`) |
| `/api/envios/**` | ShippingService (`:8087`) |
| `/api/carritos/**` | CartService (`:8088`) |
| `/api/descuentos/**` | DiscountService (`:8089`) |
| `/api/auth/**` | AuthService (`:8090`) |
| `/api/notificaciones/**` | NotificationService (`:8091`) |

## Documentación Swagger (Local)

Cada microservicio expone su documentación interactiva en:

| Servicio | URL Swagger UI |
|----------|---------------|
| GatewayService | `http://localhost:8080/swagger-ui.html` |
| UserServices | `http://localhost:8081/swagger-ui.html` |
| CategoryService | `http://localhost:8082/swagger-ui.html` |
| ProductService | `http://localhost:8083/swagger-ui.html` |
| InventoryService | `http://localhost:8084/swagger-ui.html` |
| OrderService | `http://localhost:8085/swagger-ui.html` |
| PaymentService | `http://localhost:8086/swagger-ui.html` |
| ShippingService | `http://localhost:8087/swagger-ui.html` |
| CartService | `http://localhost:8088/swagger-ui.html` |
| DiscountService | `http://localhost:8089/swagger-ui.html` |
| AuthService | `http://localhost:8090/swagger-ui.html` |
| NotificationService | `http://localhost:8091/swagger-ui.html` |

Alternativamente, los endpoints OpenAPI JSON están disponibles en `http://localhost:{puerto}/v3/api-docs`.

## Requisitos para Ejecutar

- Docker y Docker Compose instalados
- Puerto `3307` libre (MySQL de Docker)
- Git para clonar el repositorio
- JDK 17+ (para ejecución local sin Docker)

## Ejecución Local con Docker Compose

```bash
# 1. Clonar el repositorio
git clone https://github.com/igtorreso-coder/PlataformaVentaOnline.git
cd PlataformaVentaOnline

# 2. Iniciar todos los servicios
docker compose up -d

# 3. Verificar que los 12 servicios estén levantados
docker compose ps

# 4. Revisar logs de un servicio específico
docker compose logs -f order-service

# 5. Detener todos los servicios
docker compose down
```

La primera vez, MySQL crea automáticamente las 12 bases de datos y Flyway aplica las migraciones. Esperar aproximadamente 30-60 segundos hasta que todos los servicios estén listos.


El MySQL de Docker se expone en el puerto `3307` para no interferir con instalaciones locales:

- **Puerto:** `3307`
- **Usuario:** `root`
- **Contraseña:** `root`


### Datos de prueba disponibles

Los siguientes datos se crean automáticamente al iniciar los servicios (vía Flyway):

| Servicio | Registros |
|---|---|---|
| UserService | 5 usuarios: Ignacio Torres, Maria Garcia, Juan Perez, Ana Lopez, Carlos Ruiz |
| AuthService | 5 cuentas con contraseña `123456` |
| CategoryService | 5 categorías: Electrónica, Ropa, Hogar, Deportes, Libros |
| ProductService | 18 productos (8 originales + 10 nuevos: iPad Air, Audífonos Sony, Vestido, Chaqueta, Lámpara, Cojín, Pesas, Tapete Yoga, El Principito, Cuaderno) |
| InventoryService | 27 movimientos (entradas iniciales + stock nuevos productos + salidas por ventas) |
| CartService | 4 carritos: 2 activos (usuarios 1 y 3), 1 completado (usuario 1), items variados |
| DiscountService | 4 cupones: BIENVENIDO, VERANO2025, ENVIOGRATIS, VIP10 |
| OrderService | 4 pedidos (1 confirmado, 1 pendiente, 1 enviado, 1 entregado) |
| PaymentService | 4 pagos (3 aprobados, 1 pendiente) |
| ShippingService | 4 envíos (1 enviado, 1 pendiente, 1 enviado, 1 entregado) |
| NotificationService | 5 notificaciones (3 enviadas, 2 pendientes) |

### Credenciales de prueba

| Correo | Contraseña | Rol |
|---|---|---|
| `ignacio@email.com` | `123456` | ADMIN |
| `maria@email.com` | `123456` | USER |
| `juan@email.com` | `123456` | USER |
| `ana@email.com` | `123456` | USER |
| `carlos@email.com` | `123456` | USER |
