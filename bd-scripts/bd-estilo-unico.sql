-- ===================================
-- BASE DE DATOS: ESTILOUNICO - VERSIÓN FINAL
-- ===================================

CREATE DATABASE estilounico_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE estilounico_db;

-- ===================================
-- TABLA BASE: USUARIO (Autenticación)
-- ===================================
CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    rol ENUM('ADMIN', 'EMPLEADO', 'CLIENTE') NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ===================================
-- TABLA: EMPLEADO
-- ===================================
CREATE TABLE empleado (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    nombre_completo VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) DEFAULT 'No registrado',
    direccion VARCHAR(100) DEFAULT 'No registrada',
    dni VARCHAR(20) DEFAULT 'No registrado',
    fecha_contratacion DATE NOT NULL,
    cargo VARCHAR(50) DEFAULT 'Vendedor',
    estado_laboral ENUM('ACTIVO', 'INACTIVO', 'VACACIONES', 'SUSPENDIDO') DEFAULT 'ACTIVO',
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ===================================
-- TABLA: CLIENTE
-- ===================================
CREATE TABLE cliente (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL UNIQUE,
    nombre_completo VARCHAR(100) NOT NULL,
    telefono VARCHAR(20) DEFAULT 'No registrado',
    direccion_principal VARCHAR(100) DEFAULT 'No registrada',
    direccion_secundaria VARCHAR(100) NULL,
    dni VARCHAR(20) DEFAULT 'No registrado',
    fecha_nacimiento DATE NULL,
    genero ENUM('MASCULINO', 'FEMENINO', 'OTRO', 'NO_ESPECIFICA') DEFAULT 'NO_ESPECIFICA',
    total_compras DECIMAL(10,2) DEFAULT 0.00,
    numero_pedidos INT DEFAULT 0,
    cliente_frecuente BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (usuario_id) REFERENCES usuario(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ===================================
-- TABLA: CATEGORIA
-- ===================================
CREATE TABLE categoria (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(200) DEFAULT 'Sin descripción',
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ===================================
-- TABLA: PRODUCTO
-- ===================================
CREATE TABLE producto (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(200) DEFAULT 'Sin descripción disponible',
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    talla VARCHAR(10) DEFAULT 'UNICA',
    color VARCHAR(30) DEFAULT 'Sin especificar',
    marca VARCHAR(50) DEFAULT 'Genérica',
    genero ENUM('HOMBRE', 'MUJER', 'UNISEX') DEFAULT 'UNISEX',
    imagen_url VARCHAR(255) DEFAULT '/images/default-product.jpg',
    activo BOOLEAN DEFAULT TRUE,
    categoria_id BIGINT NOT NULL,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (categoria_id) REFERENCES categoria(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ===================================
-- TABLA: PEDIDO
-- ===================================
CREATE TABLE pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    fecha_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL,
    estado ENUM('PENDIENTE', 'EN_PROCESO', 'ENVIADO', 'ENTREGADO', 'CANCELADO') DEFAULT 'PENDIENTE',
    direccion_envio VARCHAR(100) DEFAULT 'Recoger en tienda',
    cliente_id BIGINT NOT NULL,
    empleado_procesador_id BIGINT NULL,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE CASCADE,
    FOREIGN KEY (empleado_procesador_id) REFERENCES empleado(id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ===================================
-- TABLA: DETALLE_PEDIDO
-- ===================================
CREATE TABLE detalle_pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (pedido_id) REFERENCES pedido(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES producto(id) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ===================================
-- TABLA: CARRITO_ITEM
-- ===================================
CREATE TABLE carrito_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    producto_id BIGINT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    fecha_agregado TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (cliente_id) REFERENCES cliente(id) ON DELETE CASCADE,
    FOREIGN KEY (producto_id) REFERENCES producto(id) ON DELETE CASCADE,
    UNIQUE KEY unique_cliente_producto (cliente_id, producto_id)
) ENGINE=InnoDB;

-- ---------------------------------
-- 1. CATEGORÍAS
-- ---------------------------------
INSERT INTO categoria (id, nombre, descripcion) VALUES
(1, 'Polos y Camisetas', 'Polos de algodón, camisetas con estampados y más.'),
(2, 'Pantalones y Jeans', 'Jeans, pantalones de tela, joggers y shorts.'),
(3, 'Abrigos y Casacas', 'Casacas, chompas, polerones y abrigos para toda estación.'),
(4, 'Calzado', 'Zapatillas urbanas, de running, zapatos formales y sandalias.'),
(5, 'Accesorios', 'Gorras, cinturones, bolsos, billeteras y más complementos.');

-- ---------------------------------
-- 2. USUARIOS Y ROLES
-- ---------------------------------
-- Contraseña para todos los usuarios de prueba: '123'
INSERT INTO usuario (id, username, password, email, rol) VALUES
-- Admin
(1, 'admin', '123', 'admin@estilounico.com', 'ADMIN'),
-- Empleados
(2, 'jperez', '123', 'juan.perez@estilounico.com', 'EMPLEADO'),
(3, 'atorres', '123', 'ana.torres@estilounico.com', 'EMPLEADO'),
(4, 'mgarcia', '123', 'mario.garcia@estilounico.com', 'EMPLEADO'),
-- Clientes
(5, 'cgonzales', '123', 'carlos.gonzales@email.com', 'CLIENTE'),
(6, 'lrodriguez', '123', 'laura.rodriguez@email.com', 'CLIENTE'),
(7, 'projas', '123', 'pedro.rojas@email.com', 'CLIENTE'),
(8, 'smendoza', '123', 'sofia.mendoza@email.com', 'CLIENTE'),
(9, 'dcastillo', '123', 'diego.castillo@email.com', 'CLIENTE');

-- ---------------------------------
-- 3. DETALLES DE EMPLEADOS
-- ---------------------------------
INSERT INTO empleado (id, usuario_id, nombre_completo, telefono, direccion, dni, fecha_contratacion, cargo, estado_laboral) VALUES
(1, 2, 'Juan Pérez García', '987654321', 'Av. Principal 123, Arequipa', '71234567', '2023-01-15', 'Gerente de Tienda', 'ACTIVO'),
(2, 3, 'Ana Torres López', '912345678', 'Calle Secundaria 456, Arequipa', '72345678', '2023-05-20', 'Vendedora Senior', 'ACTIVO'),
(3, 4, 'Mario García Luna', '923456789', 'Jr. Los Pinos 789, Arequipa', '73456789', '2024-02-10', 'Vendedor', 'VACACIONES');

-- ---------------------------------
-- 4. DETALLES DE CLIENTES
-- ---------------------------------
-- Los valores de total_compras y numero_pedidos se pre-calculan basados en los pedidos que se insertarán más abajo.
INSERT INTO cliente (id, usuario_id, nombre_completo, telefono, direccion_principal, dni, fecha_nacimiento, genero, total_compras, numero_pedidos, cliente_frecuente) VALUES
-- Carlos (ID 1): 1 pedido entregado (219.80), 1 pendiente. Total: 2 pedidos.
(1, 5, 'Carlos Gonzales Ruiz', '934567890', 'Urb. La Florida A-1, Arequipa', '74567890', '1990-08-25', 'MASCULINO', 629.60, 2, TRUE),
-- Laura (ID 2): 1 pedido enviado, 1 en proceso. Total: 2 pedidos.
(2, 6, 'Laura Rodríguez Díaz', '945678901', 'Calle Mercaderes 302, Arequipa', '75678901', '1995-11-12', 'FEMENINO', 569.50, 2, TRUE),
-- Pedro (ID 3): 1 pedido cancelado. Total: 0 pedidos válidos.
(3, 7, 'Pedro Rojas Flores', '956789012', 'Av. Ejército 555, Yanahuara', '76789012', '1988-03-30', 'MASCULINO', 0.00, 0, FALSE),
-- Sofía (ID 4): 1 pedido entregado (209.80). Total: 1 pedido.
(4, 8, 'Sofía Mendoza Casas', '967890123', 'Residencial Los Álamos B-7', '77890123', '2001-07-19', 'FEMENINO', 209.80, 1, TRUE),
-- Diego (ID 5): Sin pedidos.
(5, 9, 'Diego Castillo Vera', '978901234', 'Av. Dolores 101, Bustamante y Rivero', '78901234', '1998-01-05', 'MASCULINO', 0.00, 0, FALSE);

-- ---------------------------------
-- 5. PRODUCTOS
-- ---------------------------------
-- URLs de imágenes son placeholders. Reemplazar con rutas reales.
INSERT INTO producto (id, nombre, descripcion, precio, stock, talla, color, marca, genero, imagen_url, categoria_id, fecha_creacion) VALUES
-- Polos y Camisetas (Cat 1)
(1, 'Polo Básico de Algodón', 'Un clásico infaltable. 100% algodón pima.', 49.90, 50, 'M', 'Blanco', 'UrbanStyle', 'UNISEX', '/images/default-product.jpg', 1, '2024-11-01 10:00:00'),
(2, 'Polo Básico de Algodón', 'Un clásico infaltable. 100% algodón pima.', 49.90, 45, 'L', 'Negro', 'UrbanStyle', 'UNISEX', '/images/default-product.jpg', 1, '2024-11-01 10:00:00'),
(3, 'Camiseta Gráfica "Adventure"', 'Estampado de alta calidad con temática de aventura.', 69.90, 5, 'M', 'Gris Melange', 'Wanderlust', 'HOMBRE', '/images/default-product.jpg', 1, '2024-10-28 11:00:00'),
(4, 'Polo Oversize "Tokyo Night"', 'Corte holgado y estampado japonés moderno.', 89.90, 25, 'UNICA', 'Negro', 'StreetArt', 'UNISEX', '/images/default-product.jpg', 1, '2024-10-25 12:00:00'),
-- Pantalones y Jeans (Cat 2)
(5, 'Jean Slim Fit Clásico', 'Jean versátil de corte slim, perfecto para cualquier ocasión.', 149.90, 40, '32', 'Azul Oscuro', 'DenimCo', 'HOMBRE', '/images/default-product.jpg', 2, '2024-10-20 13:00:00'),
(6, 'Jean Mom Fit "90s Style"', 'Corte retro de tiro alto, cómodo y a la moda.', 159.90, 35, '28', 'Celeste', 'RetroVibes', 'MUJER', '/images/default-product.jpg', 2, '2024-10-18 14:00:00'),
(7, 'Pantalón Cargo de Tela', 'Pantalón utilitario con múltiples bolsillos, 100% algodón.', 129.90, 30, 'M', 'Verde Militar', 'WorkWear', 'HOMBRE', '/images/default-product.jpg', 2, '2024-10-15 15:00:00'),
(8, 'Jogger Deportivo Tech', 'Tela ligera y transpirable, ideal para el día a día o entrenar.', 99.90, 50, 'L', 'Negro', 'ActiveLife', 'UNISEX', '/images/default-product.jpg', 2, '2024-10-12 16:00:00'),
-- Abrigos y Casacas (Cat 3)
(9, 'Casaca de Jean Trucker', 'La icónica casaca de jean que nunca pasa de moda.', 189.90, 25, 'M', 'Azul Clásico', 'DenimCo', 'UNISEX', '/images/default-product.jpg', 3, '2024-10-10 17:00:00'),
(10, 'Polerón con Capucha "Basic"', 'Algodón grueso y suave, perfecto para el frío.', 119.90, 60, 'L', 'Gris', 'ComfortZone', 'UNISEX', '/images/default-product.jpg', 3, '2024-10-08 18:00:00'),
(11, 'Chompa de Hilo Fino', 'Elegante y ligera, ideal para un look casual de oficina.', 99.90, 30, 'S', 'Beige', 'Elegantia', 'MUJER', '/images/default-product.jpg', 3, '2024-10-05 19:00:00'),
(12, 'Casaca Cortavientos "Runner"', 'Impermeable y ultraligera, con detalles reflectantes.', 139.90, 20, 'M', 'Negro', 'ActiveLife', 'HOMBRE', '/images/default-product.jpg', 3, '2024-10-02 20:00:00'),
-- Calzado (Cat 4)
(13, 'Zapatillas Urbanas "Classic"', 'Diseño minimalista en cuero sintético blanco.', 199.90, 40, '42', 'Blanco', 'UrbanStyle', 'UNISEX', '/images/default-product.jpg', 4, '2024-09-30 21:00:00'),
(14, 'Zapatillas de Running "AirMax"', 'Máxima amortiguación y soporte para corredores.', 349.90, 20, '43', 'Negro/Rojo', 'RunFast', 'HOMBRE', '/images/default-product.jpg', 4, '2024-09-28 22:00:00'),
(15, 'Botines de Cuero "Chelsea"', 'Botines elegantes y versátiles para un look formal o casual.', 299.90, 15, '38', 'Marrón', 'Elegantia', 'MUJER', '/images/default-product.jpg', 4, '2024-09-25 23:00:00'),
(16, 'Sandalias de Plataforma', 'Sandalias cómodas con plataforma de yute.', 129.90, 30, '37', 'Negro', 'SummerVibes', 'MUJER', '/images/default-product.jpg', 4, '2024-09-22 10:00:00'),
-- Accesorios (Cat 5)
(17, 'Gorra de Béisbol Bordada', 'Gorra de algodón con logo bordado en 3D.', 59.90, 80, 'UNICA', 'Negro', 'UrbanStyle', 'UNISEX', '/images/default-product.jpg', 5, '2024-09-20 11:00:00'),
(18, 'Cinturón de Cuero Reversible', 'Dos cinturones en uno: un lado negro y otro marrón.', 89.90, 40, 'UNICA', 'Negro/Marrón', 'Elegantia', 'HOMBRE', '/images/default-product.jpg', 5, '2024-09-18 12:00:00'),
(19, 'Bolso Tote de Lona', 'Bolso espacioso y resistente, ideal para el día a día.', 79.90, 35, 'UNICA', 'Crudo', 'EcoStyle', 'MUJER', '/images/default-product.jpg', 5, '2024-09-15 13:00:00'),
(20, 'Mochila "Explorer" 25L', 'Mochila funcional con compartimento para laptop.', 179.90, 25, 'UNICA', 'Azul Marino', 'Wanderlust', 'UNISEX', '/images/default-product.jpg', 5, '2024-09-12 14:00:00');

-- ---------------------------------
-- 6. PEDIDOS Y DETALLES
-- ---------------------------------
-- Se crean 6 pedidos con diferentes estados y clientes para simular historial.
-- Pedido 1: Cliente 1 (Carlos), estado ENTREGADO. Venta en tienda.
INSERT INTO pedido (id, cliente_id, direccion_envio, total, estado, fecha_pedido, empleado_procesador_id) VALUES
(1, 1, 'Recoger en tienda', 219.80, 'ENTREGADO', '2024-10-15 10:30:00', 2);
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(1, 3, 1, 69.90, 69.90),
(1, 5, 1, 149.90, 149.90);

-- Pedido 2: Cliente 2 (Laura), estado ENVIADO. Venta online.
INSERT INTO pedido (id, cliente_id, direccion_envio, total, estado, fecha_pedido, empleado_procesador_id) VALUES
(2, 2, 'Calle Mercaderes 302, Arequipa', 309.70, 'ENVIADO', '2025-11-01 14:00:00', 1);
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(2, 6, 1, 159.90, 159.90),
(2, 11, 1, 99.90, 99.90),
(2, 1, 1, 49.90, 49.90);

-- Pedido 3: Cliente 1 (Carlos), estado PENDIENTE. Venta online reciente.
INSERT INTO pedido (id, cliente_id, direccion_envio, total, estado, fecha_pedido) VALUES
(3, 1, 'Urb. La Florida A-1, Arequipa', 409.80, 'PENDIENTE', '2024-11-04 18:00:00');
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(3, 14, 1, 349.90, 349.90),
(3, 17, 1, 59.90, 59.90);

-- Pedido 4: Cliente 4 (Sofía), estado ENTREGADO. Venta online antigua.
INSERT INTO pedido (id, cliente_id, direccion_envio, total, estado, fecha_pedido, empleado_procesador_id) VALUES
(4, 4, 'Residencial Los Álamos B-7', 209.80, 'ENTREGADO', '2024-09-20 18:45:00', 2);
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(4, 19, 1, 79.90, 79.90),
(4, 16, 1, 129.90, 129.90);

-- Pedido 5: Cliente 3 (Pedro), estado CANCELADO.
INSERT INTO pedido (id, cliente_id, direccion_envio, total, estado, fecha_pedido) VALUES
(5, 3, 'Av. Ejército 555, Yanahuara', 119.90, 'CANCELADO', '2024-10-28 11:00:00');
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(5, 10, 1, 119.90, 119.90);

-- Pedido 6: Cliente 2 (Laura), estado EN_PROCESO.
INSERT INTO pedido (id, cliente_id, direccion_envio, total, estado, fecha_pedido, empleado_procesador_id) VALUES
(6, 2, 'Calle Mercaderes 302, Arequipa', 279.80, 'EN_PROCESO', '2024-11-03 09:15:00', 2);
INSERT INTO detalle_pedido (pedido_id, producto_id, cantidad, precio_unitario, subtotal) VALUES
(6, 9, 1, 189.90, 189.90),
(6, 18, 1, 89.90, 89.90);

-- ---------------------------------
-- 7. CARRITO DE COMPRAS
-- ---------------------------------
-- Se añaden items al carrito de algunos clientes para simular una sesión activa.
INSERT INTO carrito_item (cliente_id, producto_id, cantidad) VALUES
(2, 8, 1),  -- Laura (ID 2) tiene un Jogger en su carrito
(2, 20, 1), -- y también una Mochila
(4, 2, 2);  -- Sofía (ID 4) tiene 2 Polos Básicos Negros en su carrito

