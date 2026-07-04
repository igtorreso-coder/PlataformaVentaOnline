INSERT INTO cupones (codigo, tipo, valor, monto_minimo, usos_maximos, usos_actuales, fecha_expiracion, activo, created_at, updated_at) VALUES
('BIENVENIDO', 'PORCENTAJE', 10.00, 500.00, 100, 0, '2026-12-31 23:59:59', TRUE, NOW(), NOW()),
('VERANO2025', 'PORCENTAJE', 15.00, 1000.00, 50, 0, '2025-09-30 23:59:59', TRUE, NOW(), NOW()),
('ENVIOGRATIS', 'FIJO', 150.00, 500.00, 200, 0, '2026-12-31 23:59:59', TRUE, NOW(), NOW()),
('VIP10', 'PORCENTAJE', 20.00, 3000.00, 10, 0, '2026-06-30 23:59:59', TRUE, NOW(), NOW());
