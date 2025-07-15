SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

CREATE DATABASE panaderia_v1;
use panaderia_v1;
drop database panaderia_v1;

CREATE TABLE `roles` (
    `id_rol` INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    `nombre` VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;


INSERT INTO roles (nombre) VALUES 
('ROLE_USER'),
('ROLE_ADMIN');


CREATE TABLE IF NOT EXISTS `clientes` (
    `id_cli` int AUTO_INCREMENT PRIMARY KEY,
    `nombre_cli` varchar(30) NOT NULL,
    `apellidos_cli` varchar(30) NOT NULL,
    `Direccion` varchar(30) NOT NULL,
    `Telefono` char(9) NOT NULL,
    `dni` char(8) NOT NULL,
    `correo` varchar(30) NOT NULL,
	`contraseña` varchar(30) NOT NULL,
    UNIQUE (`dni`),  
    UNIQUE (`correo`) 
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO clientes (nombre_cli, apellidos_cli, Direccion, Telefono, dni, correo, contraseña) VALUES 
('Juan', 'Pérez Gómez', 'Av.Olivos','987654321', '23541234', 'juan.perez@gmail.com', '123456'),
('Ana', 'Rodríguez Soto', 'Av.Pasamayo', '912345678', '23416523', 'ana.rs@gmail.com', '123456'),
('Luis', 'Martínez Ruiz', 'Av.Veracruz', '900111222', '12634651', 'luis.mr@gmail.com', '123456'),
('María', 'García López', 'Av.Lima', '911222333', '63461234', 'maria.gl@gmail.com', '123456'),
('Carlos', 'Fernández Díaz', 'Av.Colombia', '922333444', '12345678', 'carlos.fd@gmail.com', '123456'),
('Admin', 'Principal', 'Av.par', '922333999', '99999999', 'admin@admin.com', 'admin123');

CREATE TABLE IF NOT EXISTS `cliente_roles` (
    `id_cli` INT NOT NULL,            -- Referencia a clientes
    `id_rol` INT NOT NULL,            -- Referencia a roles
    PRIMARY KEY (`id_cli`, `id_rol`), -- Llave primaria compuesta
    FOREIGN KEY (`id_cli`) REFERENCES `clientes`(`id_cli`) ON DELETE CASCADE, -- Relación con clientes
    FOREIGN KEY (`id_rol`) REFERENCES `roles`(`id_rol`) ON DELETE CASCADE   -- Relación con roles
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

INSERT INTO cliente_roles (id_cli, id_rol)
SELECT id_cli, (SELECT id_rol FROM roles WHERE nombre = 'ROLE_USER') FROM clientes;

INSERT INTO cliente_roles (id_cli, id_rol)
SELECT id_cli, (SELECT id_rol FROM roles WHERE nombre = 'ROLE_ADMIN') 
FROM clientes WHERE correo = 'admin@admin.com';

CREATE TABLE producto (
    id_prod BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    imagen VARCHAR(255),
    categoria VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS pedido (
    id_pedido BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cli INT NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado ENUM('pendiente', 'en proceso', 'entregado','cancelado') DEFAULT 'pendiente',
    total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_cli) REFERENCES clientes(id_cli) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE IF NOT EXISTS pedido_item (
    id_item BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_pedido BIGINT NOT NULL,
    id_prod BIGINT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id_pedido) ON DELETE CASCADE,
    FOREIGN KEY (id_prod) REFERENCES producto(id_prod)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;



INSERT INTO producto (nombre, descripcion, precio, stock, imagen, categoria) VALUES
('Petipan con pollo', 'Disponible de Lunes a Sábado.', 1, 100, 'https://bonpanperu.com/wp-content/uploads/2023/10/petipan-con-pollo-80-web-1.jpg', 'Bocaditos'),
('Alfajorcitos', 'Disponible de Lunes a Sábado.', 1, 100, 'https://bonpanperu.com/wp-content/uploads/2023/10/Alfajorico-50.00-el-ciento-t-1.jpg', 'Bocaditos'),
('Chocolate #1', 'Disponible de Lunes a Sábado.', 40, 1, 'https://bonpanperu.com/wp-content/uploads/2023/10/torta-de-chocolate-60-t-1.jpg', 'Tortas'),
('Chocolate #2', 'Disponible de Lunes a Sábado.', 40, 1, 'https://bonpanperu.com/wp-content/uploads/2024/04/Torta-de-chocolate-1.webp', 'Tortas'),
('Chocolate #3', 'Disponible de Lunes a Sábado.', 40, 1, 'https://bonpanperu.com/wp-content/uploads/2024/04/Chocolate-3.webp', 'Tortas'),
('Keke rectangular', 'Disponible de Lunes a Sábado.', 14, 1, 'https://bonpanperu.com/wp-content/uploads/2023/10/Keke-rectangular-web-1.jpg', 'Kekes'),
('Triple vegetariano', 'Disponible de Lunes a Sábado.', 10, 1, 'https://bonpanperu.com/wp-content/uploads/2023/10/triple-vegetariano-web-1.jpg', 'Salados Personales'),
('Triple primaveral', 'Disponible de Lunes a Sábado.', 10, 1, 'https://bonpanperu.com/wp-content/uploads/2023/10/triple-primaveral-web-1.jpg', 'Salados Personales'),
('Triple tradicional', 'Disponible de Lunes a Sábado.', 9, 1, 'https://bonpanperu.com/wp-content/uploads/2023/10/triple-tradicional-web-1.jpg', 'Salados Personales'),
('Petipan con pollo', 'Disponible de Lunes a Sábado.', 1, 100, 'https://bonpanperu.com/wp-content/uploads/2023/10/petipan-con-pollo-80-web-1.jpg', 'Bocaditos'),
('Mini Pan', 'Disponible de Lunes a Sábado.', 1, 100, 'https://bonpanperu.com/wp-content/uploads/2023/10/Petipanweb-1.jpg', 'Panes'),
('Mini pan de hot dog', 'Disponible de Lunes a Sábado.', 1, 100, 'https://bonpanperu.com/wp-content/uploads/2023/10/Hotdogcito-web-1.jpg', 'Panes'),
('Francesitos', 'Disponible de Lunes a Sábado.', 1, 100, 'https://bonpanperu.com/wp-content/uploads/2023/10/francesitosweb-1.jpg', 'Panes'),
('Empanaditas de carne', 'Pan saludable con fibra y granos enteros.', 1, 80, 'https://bonpanperu.com/wp-content/uploads/2023/10/Empanadita-de-carne-60-t-1.jpg', 'Bocaditos');

SELECT * FROM pedido;
SELECT * FROM pedido_item;
SELECT * FROM producto;
SELECT * FROM clientes;

SELECT 
    p.id_pedido AS pedido_id,
    c.id_cli AS cliente_id,
    CONCAT(c.nombre_cli, ' ', c.apellidos_cli) AS cliente_nombre,
    p.fecha,
    p.estado,
    p.total AS total_pedido,
    pi.id_item AS pedido_item_id,
    pr.nombre AS producto_nombre,
    pi.cantidad,
    pi.precio_unitario,
    pi.total AS total_item
FROM pedido p
JOIN clientes c ON p.id_cli = c.id_cli
LEFT JOIN pedido_item pi ON pi.id_pedido = p.id_pedido
LEFT JOIN producto pr ON pi.id_prod = pr.id_prod
ORDER BY p.id_pedido, pi.id_item;



CREATE TABLE IF NOT EXISTS reclamo (
    id_reclamo INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    id_cli INT NOT NULL,
    fecha_pedido DATE NOT NULL,
    motivo_reclamo VARCHAR(100) NOT NULL,
    detalle TEXT NOT NULL,
    estado ENUM('pendiente', 'en_proceso', 'resuelto') DEFAULT 'pendiente',
    fecha_reclamo TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (id_cli) REFERENCES clientes(id_cli)
        ON DELETE CASCADE
        ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

 
INSERT INTO reclamo (id_cli, fecha_pedido, motivo_reclamo, detalle, estado)
VALUES 
(1, '2025-06-01', 'Producto dañado', 'El pan llegó aplastado y con mal olor.', 'pendiente'),
(2, '2025-06-02', 'Producto equivocado en el pedido', 'Solicité 2 tortas y me llegó solo una y era de otro sabor.', 'pendiente'),
(3, '2025-06-03', 'Atención deficiente', 'El repartidor fue grosero y llegó tarde.', 'en_proceso'),
(6, '2025-06-04', 'Precio incorrecto', 'Me cobraron más de lo que decía en la web.', 'resuelto'),
(1, '2025-06-05', 'Otro', 'No recibí boleta y necesito el comprobante.', 'pendiente');