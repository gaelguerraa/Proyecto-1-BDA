-- Crear la base de datos y usarla
CREATE DATABASE IF NOT EXISTS bdBoletos;
USE bdBoletos;

-- Insertar direcciones de usuarios
INSERT INTO DireccionesUsuarios (calle, ciudad, estado) VALUES
('Av. Reforma 123', 'Ciudad de México', 'CDMX'),
('Calle Morelos 456', 'Guadalajara', 'Jalisco'),
('Blvd. Independencia 789', 'Monterrey', 'Nuevo León'),
('Av. Juárez 101', 'Puebla', 'Puebla'),
('Calle Hidalgo 202', 'Mérida', 'Yucatán');

-- Insertar usuarios con direcciones asignadas
INSERT INTO Usuarios (email, nombre, contraseña_hash, apellidoPaterno, apellidoMaterno, fechaNacimiento, saldo, idDireccion) VALUES
('juan@example.com', 'Juan', 'abc123', 'Pérez', 'López', '1990-05-15', 500.00, 1),
('maria@example.com', 'María', 'def456', 'Gómez', 'Hernández', '1995-08-22', 700.00, 2),
('carlos@example.com', 'Carlos', 'ghi789', 'Ramírez', 'Díaz', '1988-12-10', 900.00, 3),
('ana@example.com', 'Ana', 'jkl012', 'Torres', 'Martínez', '2000-03-25', 300.00, 4),
('luis@example.com', 'Luis', 'mno345', 'Fernández', 'Sánchez', '1992-07-18', 1000.00, 5);

-- Insertar eventos
INSERT INTO Eventos (nombre, fecha, recinto, ciudad, estado, descripcion) VALUES
('Concierto Rock', '2025-06-15 20:00:00', 'Auditorio Nacional', 'Ciudad de México', 'CDMX', 'Concierto de rock internacional'),
('Festival Electrónico', '2025-07-20 18:00:00', 'Foro Sol', 'Ciudad de México', 'CDMX', 'Los mejores DJs del mundo en un solo lugar'),
('Obra de Teatro', '2025-08-10 19:30:00', 'Teatro Metropolitano', 'Guadalajara', 'Jalisco', 'Obra clásica con actores reconocidos');

-- Insertar boletos para cada evento (10 boletos por evento)
INSERT INTO Boletos (numeroSerie, fila, asiento, precioOriginal, numeroInterno, estado, idEvento, idUsuario) VALUES
('A0000001', 'A', '1', 1000.00, 'BOL-001', 'Vendido', 1, 1),
('A0000002', 'A', '2', 1000.00, 'BOL-002', 'Vendido', 1, 2),
('A0000003', 'A', '3', 1000.00, 'BOL-003', 'Disponible', 1, null),
('A0000004', 'A', '4', 1000.00, 'BOL-004', 'Disponible', 1, null),
('A0000005', 'A', '5', 1000.00, 'BOL-005', 'Vendido', 1, 3),
('A0000006', 'A', '6', 1000.00, 'BOL-006', 'Disponible', 1, null),
('A0000007', 'A', '7', 1000.00, 'BOL-007', 'Vendido', 1, 4),
('A0000008', 'A', '8', 1000.00, 'BOL-008', 'Disponible', 1, null),
('A0000009', 'A', '9', 1000.00, 'BOL-009', 'Vendido', 1, 5),
('A0000010', 'A', '10', 1000.00, 'BOL-010', 'Disponible', 1, null),
-- Festival Electrónico
('B0000001', 'B', '1', 1500.00, 'BOL-011', 'Vendido', 2, 1),
('B0000002', 'B', '2', 1500.00, 'BOL-012', 'Disponible', 2, null),
('B0000003', 'B', '3', 1500.00, 'BOL-013', 'Vendido', 2, 2),
('B0000004', 'B', '4', 1500.00, 'BOL-014', 'Disponible', 2, null),
('B0000005', 'B', '5', 1500.00, 'BOL-015', 'Vendido', 2, 3),
('B0000006', 'B', '6', 1500.00, 'BOL-016', 'Disponible', 2, NULL),
('B0000007', 'B', '7', 1500.00, 'BOL-017', 'Disponible', 2, NULL),
('B0000008', 'B', '8', 1500.00, 'BOL-018', 'Vendido', 2, 4),
('B0000009', 'B', '9', 1500.00, 'BOL-019', 'Disponible', 2, NULL),
('B0000010', 'B', '10', 1500.00, 'BOL-020', 'Vendido', 2, 5),
-- Obra de Teatro
('C0000001', 'C', '1', 800.00, 'BOL-021', 'Vendido', 3, 1),
('C0000002', 'C', '2', 800.00, 'BOL-022', 'Disponible', 3, NULL),
('C0000003', 'C', '3', 800.00, 'BOL-023', 'Vendido', 3, 2),
('C0000004', 'C', '4', 800.00, 'BOL-024', 'Disponible', 3, NULL),
('C0000005', 'C', '5', 800.00, 'BOL-025', 'Vendido', 3, 3),
('C0000006', 'C', '6', 800.00, 'BOL-026', 'Disponible', 3, NULL),
('C0000007', 'C', '7', 800.00, 'BOL-027', 'Disponible', 3, NULL),
('C0000008', 'C', '8', 800.00, 'BOL-028', 'Vendido', 3, 4),
('C0000009', 'C', '9', 800.00, 'BOL-029', 'Disponible', 3, NULL),
('C0000010', 'C', '10', 800.00, 'BOL-030', 'Vendido', 3, 5);

-- Confirmar datos insertados
SELECT * FROM Eventos;
SELECT * FROM Usuarios;
SELECT * FROM Boletos;


-- TRANSACCION PARA AÑADIR SALDO
-- FUNCIONA BIEN
DELIMITER $$
CREATE PROCEDURE AÑADIR_SALDO (_IDUSUARIO INT, _SALDO DECIMAL(10,2))

BEGIN
START TRANSACTION;

UPDATE USUARIOS 
SET SALDO = SALDO + _SALDO
WHERE IDUSUARIO = _IDUSUARIO;

COMMIT;
ROLLBACK;
END; $$

DELIMITER ;

 


DELIMITER $$

CREATE PROCEDURE comprar_boleto_sistema(nombreEvento VARCHAR(30), fechaEvento DATETIME, idUsuario INT)

BEGIN

DECLARE saldoUsuario DECIMAL(10, 2);
DECLARE idBoleto INT;
DECLARE precio DECIMAL(10, 2);
DECLARE estadoActual VARCHAR(30);

SELECT saldo INTO saldoUsuario
FROM Usuarios
WHERE idUsuario=idUsuario;


START TRANSACTION;

SELECT	
		B.idBoleto,
		B.precioOriginal,
        B.estado
        INTO
        idBoleto,
        precio,
        estadoActual
        FROM Boletos B
        INNER JOIN EVENTOS E
        ON B.IDEVENTO=E.IDEVENTO
        WHERE 
        B.estado = "Disponible" AND
        E.nombre = nombreEvento
        AND E.fecha = fechaEvento
        LIMIT 1;
        
    -- Verificar si el boleto está disponible
    IF estadoActual != "Disponible" THEN
        ROLLBACK;
        SELECT 'No hay boletos disponibles para este evento y fecha.' AS Mensaje;
    ELSE
        -- Verificar si el usuario tiene suficiente saldo
        IF saldoUsuario < precio THEN
            -- Si no tiene suficiente saldo, apartar el boleto
            UPDATE Boletos 
            SET Estado = "Apartado"
            WHERE idBoleto = idBoleto;

            INSERT INTO TRANSACCIONES (fechaHora, monto, tipo, estado, idBoleto, IdComprador) 
            VALUES (NOW(), precio, "Compra", "Procesando", idBoleto, idUsuario);

            COMMIT;
            SELECT 'Boleto apartado. Saldo insuficiente para completar la compra.' AS Mensaje;
        ELSE
            -- Si tiene suficiente saldo, comprar el boleto
            UPDATE Usuarios
            SET Saldo = saldoUsuario - precio
            WHERE idUsuario = idUsuario;

            UPDATE Boletos
            SET estado = 'Vendido'
            WHERE idBoleto = idBoleto;

            INSERT INTO TRANSACCIONES (fechaHora, monto, tipo, estado, idBoleto, IdComprador) 
            VALUES (NOW(), precio, "Compra", "Completada", idBoleto, idUsuario);

            COMMIT;
            SELECT 'Compra realizada exitosamente.' AS Mensaje;
        END IF;
    END IF;
END;
$$

DELIMITER ;

CALL COMPRAR_BOLETO_SISTEMA('Concierto Rock', '2025-06-15 20:00:00', 5);
DROP PROCEDURE IF EXISTS COMPRAR_bOLETO_SISTEMA; 

SELECT SALDO FROM USUARIOS;