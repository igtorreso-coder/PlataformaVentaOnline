INSERT INTO pagos (pedido_id, monto, metodo_pago, estado, referencia, fecha_pago, created_at, updated_at) VALUES
(1, 25700.00, 'TARJETA', 'APROBADO', 'REF-001-ABC', NOW(), NOW(), NOW()),
(2, 15000.00, 'TRANSFERENCIA', 'PENDIENTE', NULL, NULL, NOW(), NOW());
