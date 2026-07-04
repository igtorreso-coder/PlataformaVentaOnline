INSERT INTO notificaciones (usuario_id, tipo, asunto, mensaje, destinatario, estado, created_at, updated_at) VALUES
(1, 'PEDIDO_CONFIRMADO', 'Pedido #1 Confirmado', 'Tu pedido #1 ha sido confirmado exitosamente. Total: $25,700.00', 'ignacio@email.com', 'ENVIADO', NOW(), NOW()),
(2, 'PEDIDO_CREADO', 'Pedido #2 Recibido', 'Tu pedido #2 ha sido recibido y está pendiente de confirmación.', 'maria@email.com', 'PENDIENTE', NOW(), NOW()),
(3, 'PEDIDO_ENVIADO', 'Pedido #3 Enviado', 'Tu pedido #3 ha sido enviado. Código de seguimiento: TRACK-002-ABC', 'juan@email.com', 'ENVIADO', NOW(), NOW()),
(1, 'PEDIDO_ENTREGADO', 'Pedido #4 Entregado', 'Tu pedido #4 ha sido entregado exitosamente. ¡Gracias por tu compra!', 'ignacio@email.com', 'ENVIADO', NOW(), NOW()),
(2, 'RECORDATORIO', 'Carrito abandonado', 'Tienes productos en tu carrito esperando. ¡Completa tu compra ahora!', 'maria@email.com', 'PENDIENTE', NOW(), NOW());
