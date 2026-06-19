INSERT INTO envios (pedido_id, direccion, ciudad, estado, codigo_seguimiento, fecha_envio, created_at, updated_at) VALUES
(2, 'Calle Morelos 456, Col. Roma', 'Ciudad de México', 'PENDIENTE', NULL, NULL, NOW(), NOW()),
(3, 'Av. Universidad 789, Col. Del Valle', 'Ciudad de México', 'ENVIADO', 'TRACK-002-ABC', NOW(), NOW(), NOW()),
(4, 'Av. Reforma 123, Col. Centro', 'Ciudad de México', 'ENTREGADO', 'TRACK-003-DEF', DATE_SUB(NOW(), INTERVAL 1 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY), DATE_SUB(NOW(), INTERVAL 2 DAY));
