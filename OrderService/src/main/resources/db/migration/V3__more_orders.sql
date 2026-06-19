INSERT INTO pedidos (id, usuario_id, fecha, total, estado, created_at, updated_at) VALUES
(3, 3, NOW(), 27500.00, 'ENVIADO', NOW(), NOW()),
(4, 1, NOW(), 6100.00, 'ENTREGADO', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY));

INSERT INTO pedido_detalles (pedido_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(3, 1, 1, 25000.00, 25000.00),
(3, 10, 1, 2500.00, 2500.00),
(4, 5, 1, 4500.00, 4500.00),
(4, 15, 2, 800.00, 1600.00);
