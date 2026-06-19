INSERT INTO notificaciones (usuario_id, tipo, asunto, mensaje, destinatario, estado, created_at, updated_at) VALUES
(3, 'PEDIDO_ENVIADO', 'Pedido #3 Enviado', 'Tu pedido #3 ha sido enviado. Código de seguimiento: TRACK-002-ABC', 'juan@email.com', 'ENVIADO', NOW(), NOW()),
(1, 'PEDIDO_ENTREGADO', 'Pedido #4 Entregado', 'Tu pedido #4 ha sido entregado exitosamente. ¡Gracias por tu compra!', 'ignacio@email.com', 'ENVIADO', NOW(), NOW()),
(2, 'RECORDATORIO', 'Carrito abandonado', 'Tienes productos en tu carrito esperando. ¡Completa tu compra ahora!', 'maria@email.com', 'PENDIENTE', NOW(), NOW());
