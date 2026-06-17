INSERT INTO pedidos (id, usuario_id, fecha, total, estado, created_at, updated_at) VALUES
(1, 1, NOW(), 25700.00, 'CONFIRMADO', NOW(), NOW()),
(2, 2, NOW(), 15000.00, 'PENDIENTE', NOW(), NOW());

INSERT INTO pedido_detalles (pedido_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(1, 1, 1, 25000.00, 25000.00),
(1, 7, 1, 1200.00, 1200.00),
(2, 2, 1, 15000.00, 15000.00);
