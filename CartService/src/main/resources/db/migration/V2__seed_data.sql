INSERT INTO carritos (id, usuario_id, estado, created_at, updated_at) VALUES
(1, 1, 'ACTIVO', NOW(), NOW()),
(2, 2, 'ACTIVO', NOW(), NOW()),
(3, 3, 'ACTIVO', NOW(), NOW()),
(4, 1, 'COMPLETADO', NOW(), NOW());

INSERT INTO carrito_items (carrito_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(1, 1, 1, 25000.00, 25000.00),
(1, 3, 2, 350.00, 700.00),
(2, 2, 1, 15000.00, 15000.00),
(3, 1, 1, 25000.00, 25000.00),
(3, 8, 2, 650.00, 1300.00),
(3, 10, 1, 2500.00, 2500.00),
(4, 5, 1, 4500.00, 4500.00),
(4, 15, 2, 800.00, 1600.00);
