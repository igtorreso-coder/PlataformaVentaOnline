INSERT INTO carritos (id, usuario_id, estado, created_at, updated_at) VALUES
(1, 1, 'ACTIVO', NOW(), NOW()),
(2, 2, 'ACTIVO', NOW(), NOW());

INSERT INTO carrito_items (carrito_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(1, 1, 1, 25000.00, 25000.00),
(1, 3, 2, 350.00, 700.00),
(2, 2, 1, 15000.00, 15000.00);
