INSERT INTO auth_usuarios (correo, contrasena, nombre_completo, rol, activo, ultimo_acceso, created_at, updated_at) VALUES
('ignacio@email.com', '$2b$10$zL0w9K5JoiXRIg7XF6L5G.MWxz5vUPQqwBh39XZg/MGvJLUj8TTIC', 'Ignacio Torres', 'ADMIN', TRUE, NOW(), NOW(), NOW()),
('maria@email.com', '$2b$10$zL0w9K5JoiXRIg7XF6L5G.MWxz5vUPQqwBh39XZg/MGvJLUj8TTIC', 'Maria Garcia', 'USER', TRUE, NOW(), NOW(), NOW()),
('juan@email.com', '$2b$10$zL0w9K5JoiXRIg7XF6L5G.MWxz5vUPQqwBh39XZg/MGvJLUj8TTIC', 'Juan Perez', 'USER', TRUE, NOW(), NOW(), NOW());
