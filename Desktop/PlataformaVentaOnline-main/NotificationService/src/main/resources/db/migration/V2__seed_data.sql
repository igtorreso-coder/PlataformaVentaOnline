INSERT INTO notificaciones (usuario_id, tipo, asunto, mensaje, destinatario, estado, created_at, updated_at) VALUES
(1, 'PEDIDO_CONFIRMADO', 'Pedido #1 Confirmado', 'Tu pedido #1 ha sido confirmado exitosamente. Total: $25,700.00', 'ignacio@email.com', 'ENVIADO', NOW(), NOW()),
(2, 'PEDIDO_CREADO', 'Pedido #2 Recibido', 'Tu pedido #2 ha sido recibido y está pendiente de confirmación.', 'maria@email.com', 'PENDIENTE', NOW(), NOW());
