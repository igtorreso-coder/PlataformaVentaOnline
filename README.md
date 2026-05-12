# PlataformaVentaOnline - Sistema de Gestión de E-commerce

Autor y Único Participante: **Ignacio Torres**

Bienvenidos a Plataforma de Venta Online, una solución integral de comercio electrónico diseñada para modernizar 
la gestión de ventas y logística mediante una arquitectura basada en microservicios.

En los sistemas tradicionales (monolitos), un fallo en el módulo de pagos puede hacer caer toda la plataforma, 
impidiendo incluso que los usuarios vean el catálogo. Para solucionar este problema de escalabilidad y tolerancia a fallos, 
este proyecto implementa una arquitectura distribuida utilizando Java + Spring Boot. Aquí, cada dominio del negocio opera de forma 
independiente, asegurando que un alto volumen de ventas o despachos no afecte la experiencia general del cliente.

## Propósito y Justificación de la Arquitectura

El propósito central de este proyecto es demostrar la capacidad técnica para diseñar, construir y justificar una arquitectura distribuida basada en microservicios.

Opte por este enfoque porque un E-commerce requiere manejar dominios de datos muy diferentes (un producto no tiene el mismo ciclo de vida que una factura o un envío). 
Al separar la plataforma en 11 servicios independientes, se logro:

Aislamiento de Bases de Datos: Cada servicio gestiona sus propios datos, garantizando integridad y evitando bloqueos (locks) masivos en una única base central.

Resiliencia: Si el servicio de notificaciones falla, la venta se procesa de igual manera, manteniendo la operatividad crítica del negocio.

Escalabilidad Horizontal: En fechas de alta demanda, se pueden levantar más instancias del servicio de `Pedidos` sin necesidad de escalar servicios menos críticos como el de `Categorías`.

## Criterios de Evaluación y Tecnologías Implementadas

Para cumplir con los más altos estándares de desarrollo, se aplicaron las siguientes tecnologías y metodologías:
- Persistencia (`JPA` + `Hibernate`): Cada microservicio posee su propio esquema relacional (MySQL), configurado en su respectivo `application.properties`. Las operaciones CRUD se realizan mediante `JpaRepository`.
- Migraciones (Flyway): En lugar de crear tablas manualmente, se integró `Flyway`. Al iniciar cada aplicación, `Flyway` ejecuta automáticamente los scripts SQL (V1__create...sql) asegurando que la base de datos siempre esté sincronizada con el código.
- Validación (Bean Validation - JSR 380): Los datos entrantes nunca tocan la lógica de negocio directamente. Se utilizan `DTOs` (Data Transfer Objects) con anotaciones como `@NotNul`l o `@Positive` para garantizar que la información sea correcta y segura desde el controlador.
- Manejo Centralizado de Excepciones: Se implementó `@ControllerAdvice (GlobalExceptionHandler)` en cada servicio. Esto captura errores de validación o fallas de lógica y retorna al cliente un JSON estructurado y estandarizado con códigos HTTP correctos `(400, 404, 409)`.
- Patrón CSR (Controller-Service-Repository): El código sigue un flujo estricto y ordenado. El `Controller` recibe la petición REST, el `Service` ejecuta la regla de negocio y el `Repository` persiste el dato. Las responsabilidades no se mezclan.
- Comunicación Síncrona: Los servicios se comunican entre sí para validar procesos. Por ejemplo, el `OrderService` consume los endpoints de `InventoryService` y `PaymentService` mediante clientes REST para asegurar la transacción completa.
- Trazabilidad (SLF4J): Se registraron logs estructurados en las capas de servicio, permitiendo auditar el flujo de cada transacción, errores críticos y comunicaciones externas.

## Flujo de Negocio
Para entender la integración de los microservicios, este es el viaje de un pedido en la plataforma:
- El usuario se autentica `(AuthService)` y navega por el catálogo, filtrando artículos `(ProductService` y `CategoryService)`.
- El cliente añade productos a su sesión temporal `(CartService)`.
- Al proceder al pago, el carrito se envía al orquestador principal `(OrderService)`.
- OrderService realiza tres acciones vitales:
- Valida y descuenta físicamente el stock en el `InventoryService`.
- Si se ingresó un cupón, calcula el nuevo monto en el `DiscountService`.
- Envía el monto final a la pasarela simulada `(PaymentService)`.

Si el pago es APROBADO, la orden se marca como confirmada, se genera la guía de despacho en `ShippingService` y se registra una alerta de éxito en `NotificationService`.

## Composición del Sistema (Microservicios)
El ecosistema E-commerce se compone de los siguientes 11 módulos independientes:

- 1. `AuthService` (Seguridad y Accesos):
  - Gestiona la emisión y validación de tokens de sesión para proteger los endpoints del sistema.
- 2. `UserServices` (Gestión de Perfiles):
  - Administra la información personal de los clientes registrados en la plataforma.
- 3. `CategoryService` (Clasificación):
  - Agrupa y categoriza el catálogo para facilitar la búsqueda de productos.
- 4. `ProductService` (Catálogo Central)
  - Expone el detalle de los artículos a la venta. Se comunica internamente con `CategoryService` para validar que el producto se asigne a una categoría existente.
- 5. `InventoryService` (Control de Existencias)
  - Componente Crítico: Mantiene el conteo exacto de stock
- 6. `CartService` (Sesión de Compra)
  - Almacena la intención temporal de compra del cliente. Su separación evita la saturación de la base de datos de pedidos definitivos con "carritos abandonados".
- 7. `DiscountService` (Promociones)
  - Evalúa códigos de descuento ingresados por el usuario, validando su vigencia y aplicando el porcentaje correspondiente.
- 8. `OrderService` 
  - El "cerebro" de la operación de venta. Recopila la información, crea el registro del pedido y coordina con Inventario y Pagos para finalizar la transacción de manera íntegra.
- 9. `PaymentService` (Pasarela de Pagos)
  - Recibe la solicitud de cobro del pedido y simula la aprobación o rechazo de la transacción financiera.
- 10. `ShippingService` (Logística)
  - Asume el control post-venta, encargándose de generar códigos de seguimiento y administrar los estados de despacho del paquete.
- 11. `NotificationService` (Comunicaciones)
  - Servicio que registra los eventos importantes (como la confirmación de una compra) emitiendo alertas para simular la comunicación con el cliente.
