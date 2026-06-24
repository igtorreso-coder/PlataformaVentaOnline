INSERT INTO pedidos (id, usuario_id, fecha, total, estado, created_at, updated_at) VALUES
(1, 1, NOW(), 25700.00, 'CONFIRMADO', NOW(), NOW()),
(2, 2, NOW(), 15000.00, 'PENDIENTE', NOW(), NOW()),
(3, 3, NOW(), 27500.00, 'ENVIADO', NOW(), NOW()),
(4, 1, NOW(), 6100.00, 'ENTREGADO', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY));

INSERT INTO pedido_detalles (pedido_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(1, 1, 1, 25000.00, 25000.00),
(1, 7, 1, 1200.00, 1200.00),
(2, 2, 1, 15000.00, 15000.00),
(3, 1, 1, 25000.00, 25000.00),
(3, 10, 1, 2500.00, 2500.00),
(4, 5, 1, 4500.00, 4500.00),
(4, 15, 2, 800.00, 1600.00);
