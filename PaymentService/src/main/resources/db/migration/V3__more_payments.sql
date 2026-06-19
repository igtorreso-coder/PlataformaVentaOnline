INSERT INTO pagos (pedido_id, monto, metodo_pago, estado, referencia, fecha_pago, created_at, updated_at) VALUES
(3, 27500.00, 'TARJETA', 'APROBADO', 'REF-003-DEF', NOW(), NOW(), NOW()),
(4, 6100.00, 'EFECTIVO', 'APROBADO', 'REF-004-GHI', DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY));
