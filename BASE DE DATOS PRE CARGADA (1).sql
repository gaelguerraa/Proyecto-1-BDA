CREATE DATABASE bdBoletos;
USE bdBoletos;

CREATE TABLE DireccionesUsuarios (
    idDireccion INT AUTO_INCREMENT PRIMARY KEY,
    calle VARCHAR(30) NOT NULL,
    ciudad VARCHAR(30) NOT NULL,
    estado VARCHAR(30) NOT NULL
);

CREATE TABLE Usuarios (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    nombre VARCHAR(20) NOT NULL,
    contraseña_hash VARCHAR(120) NOT NULL,
    apellidoPaterno VARCHAR(20) NOT NULL,
    apellidoMaterno VARCHAR(20) NOT NULL,
    fechaNacimiento DATE NOT NULL,
    saldo DECIMAL(10,2) NOT NULL DEFAULT 0,
    idDireccion INT NOT NULL,
    FOREIGN KEY (idDireccion) REFERENCES DireccionesUsuarios(idDireccion) ON DELETE CASCADE
);

CREATE TABLE Eventos (
    idEvento INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL,
    fecha DATETIME NOT NULL,
    recinto VARCHAR(30) NOT NULL,
    ciudad VARCHAR(30) NOT NULL,
    estado VARCHAR(30) NOT NULL,
    descripcion VARCHAR(100) NOT NULL
);

CREATE TABLE Boletos (
    idBoleto INT AUTO_INCREMENT PRIMARY KEY,
    numeroSerie CHAR(8) NOT NULL UNIQUE,
    fila VARCHAR(10) NOT NULL,
    asiento VARCHAR(10) NOT NULL,
    precioOriginal DECIMAL(10,2) NOT NULL,
    numeroInterno VARCHAR(20) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    idEvento INT NOT NULL,
    idUsuario INT,
    FOREIGN KEY (idEvento) REFERENCES Eventos(idEvento) ON DELETE CASCADE,
    FOREIGN KEY (idUsuario) REFERENCES Usuarios(idUsuario) ON DELETE CASCADE,
    CONSTRAINT revisar_estadoBoleto CHECK (estado IN ('Disponible', 'Apartado', 'Vendido'))
);

CREATE TABLE Transacciones (
    idTransaccion INT AUTO_INCREMENT PRIMARY KEY,
    fechaHora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    monto DECIMAL(10,2) NOT NULL,
    tipo VARCHAR(15) NOT NULL,
    estado VARCHAR(20) NOT NULL,
    idBoleto INT NOT NULL,
    idComprador INT NOT NULL,
    idVendedor INT,
    FOREIGN KEY (idBoleto) REFERENCES Boletos(idBoleto) ON DELETE CASCADE,
    FOREIGN KEY (idComprador) REFERENCES Usuarios(idUsuario) ON DELETE CASCADE,
    FOREIGN KEY (idVendedor) REFERENCES Usuarios(idUsuario) ON DELETE CASCADE,
    CONSTRAINT chk_tipo CHECK (tipo IN ('Compra', 'Reventa')),
    CONSTRAINT revisar_estadoTransaccion CHECK (estado IN ('Completada', 'Cancelada', 'Procesando'))
);

-- FUNCIÓN PARA ACTUALIZAR EDADES CADA AÑO
DELIMITER //
CREATE PROCEDURE ActualizarEdades()
BEGIN
    UPDATE Usuarios 
    SET edad = TIMESTAMPDIFF(YEAR, fechaNacimiento, CURDATE());
END //
DELIMITER ;

-- EVENTO PARA EJECUTAR LA FUNCIÓN AUTOMÁTICAMENTE CADA AÑO (EL 1 DE ENERO)
CREATE EVENT IF NOT EXISTS EventoActualizarEdades
ON SCHEDULE EVERY 1 YEAR STARTS TIMESTAMP(CURDATE(), '00:00:00')
DO CALL ActualizarEdades();

-- VISTA BOLETOS SIN NUM INTERNO
CREATE VIEW boletosView AS SELECT idBoleto,numeroSerie,fila,asiento,precioOriginal,estado,idEvento,idUsuario FROM boletos;

-- SP PARA BUSCAR POR FECHA Y HORA
DELIMITER $$
CREATE PROCEDURE buscarPorFechaYhora(IN _FECHAHORA DATETIME)
BEGIN
SELECT bl.idBoleto,ev.nombre,ev.ciudad,ev.recinto,ev.fecha,bl.numeroSerie,bl.fila, bl.asiento,bl.precioOriginal,bl.estado
FROM boletosView as bl
INNER JOIN eventos as ev
ON bl.idEvento=ev.idEvento
WHERE bl.estado LIKE 'Disponible'
AND ev.fecha = _FECHAHORA
ORDER BY ev.fecha ASC;
END$$
DELIMITER $$

-- VISTA BOLETOS DISPONIBLES
CREATE VIEW boletos_Disponibles AS
SELECT B.idBoleto, E.nombre, E.fecha, B.asiento, B.fila, B.numeroSerie, B.estado, B.precioOriginal
FROM boletos AS B
INNER JOIN eventos AS E ON B.idEvento = E.idEvento
WHERE B.estado = 'Disponible' and E.nombre = "Concierto Rock";

-- TRANSACCION PARA AÑADIR SALDO
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

-- TRANSACCION PARA COMPRAR BOLETOS SISTEMA
DELIMITER $$
CREATE PROCEDURE comprar_boleto_sistema(p_idBoleto int, p_idUsuario INT)
BEGIN
DECLARE saldoUsuario DECIMAL(10, 2);
DECLARE precio DECIMAL(10, 2);
DECLARE estadoActual VARCHAR(30);
DECLARE boletoDueño int;
SELECT saldo INTO saldoUsuario
FROM Usuarios
WHERE idUsuario=p_idUsuario;
START TRANSACTION;
SELECT	
		precioOriginal,
        estado,
        idUsuario
        INTO
        precio,
        estadoActual,
        boletoDueño
        FROM Boletos
        WHERE 
        idBoleto=p_IdBoleto;
    -- Verificar si el boleto está disponible
    IF estadoActual = "Vendido" THEN
        ROLLBACK;
        SELECT 'No hay boletos disponibles para este evento y fecha.' AS Mensaje;
    ELSE
        -- Verificar si el usuario tiene suficiente saldo y que el sistema es quien lo vende
        IF saldoUsuario < precio and boletoDueño IS null THEN
            -- Si no tiene suficiente saldo, apartar el boleto
            UPDATE Boletos 
            SET Estado = "Apartado"
            WHERE idBoleto = p_idBoleto;

            INSERT INTO TRANSACCIONES (fechaHora, monto, tipo, estado, idBoleto, IdComprador) 
            VALUES (NOW(), precio, "Compra", "Procesando", p_idBoleto, p_idUsuario);

            COMMIT;
            SELECT 'Boleto apartado. Saldo insuficiente para completar la compra.' AS Mensaje;
        ELSE
            -- Si tiene suficiente saldo, comprar el boleto
            UPDATE Usuarios
            SET Saldo = saldoUsuario - precio
            WHERE idUsuario = p_idUsuario;

            UPDATE Boletos
            SET estado = 'Vendido', idUsuario = p_idUsuario
            WHERE idBoleto = p_idBoleto;

            INSERT INTO TRANSACCIONES (fechaHora, monto, tipo, estado, idBoleto, IdComprador) 
            VALUES (NOW(), precio, "Compra", "Completada", p_idBoleto, p_idUsuario);

            COMMIT;
            SELECT 'Compra realizada exitosamente.' AS Mensaje;
        END IF;
    END IF;
END;
$$

DELIMITER ;

-- Actualizar estado de "Apartado" a "Disponible"
SET GLOBAL event_scheduler = ON;
DELIMITER $$
CREATE EVENT IF NOT EXISTS actualizar_boletos_apartados
ON SCHEDULE EVERY 1 MINUTE
DO
BEGIN
    UPDATE Boletos
    SET estado = 'Disponible'
    WHERE estado = 'Apartado' AND TIMESTAMPDIFF(MINUTE, (SELECT MAX(fechaHora) FROM TRANSACCIONES WHERE TRANSACCIONES.idBoleto = Boletos.idBoleto), NOW()) >= 10;
END $$
DELIMITER ;
DELIMITER $$

DELIMITER $$

CREATE PROCEDURE reventa_boletos(
	IN _IDCOMPRADOR INT,
	IN _IDVENDEDOR INT,
    IN _IDBOLETO INT,
    IN _PRECIOREVENTA DECIMAL(10,2)
)

BEGIN
	DECLARE _PRECIOORIGINAL DECIMAL(10,2);
    DECLARE _SALDOCOMPRADOR DECIMAL(10,2);
    DECLARE _SALDOVENDEDOR DECIMAL(10,2);
    DECLARE _IDEVENTO INT;
    DECLARE _BOLETOSVENDEDOR INT;
    DECLARE _MAXBOLETOSREVENTA INT;
    DECLARE _PRECIOMAXREVENTA DECIMAL(10,2);
    DECLARE _COMISION DECIMAL(10,2);

	START TRANSACTION;
	
    #Verificar si el boleto ingresado es del vendedor y asignar el idevento y el precio original a las variables
    SELECT idEvento, precioOriginal 
    INTO _IDEVENTO, _PRECIOORIGINAL
    FROM Boletos 
    WHERE idBoleto = _IDBOLETO 
    AND idUsuario = _IDVENDEDOR
    FOR UPDATE; #Evita que se modifique la fila mientras se ejecuta la transaccion
    
    IF _IDEVENTO IS NULL THEN
		SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El vendedor no posee este boleto.';
	END IF;
    
    #Verificar la cantidad de boletos que puede revender el vendedor
    SELECT COUNT(*) 
    INTO _BOLETOSVENDEDOR 
    FROM Boletos 
    WHERE idUsuario = _IDVENDEDOR 
    AND idEvento = _IDEVENTO
    FOR UPDATE;
    
    #Solo puede vender la mitad de boletos para un evento
    SET _MAXBOLETOSREVENTA = FLOOR(_BOLETOSVENDEDOR / 2);
    
    #Si los boletos maximos del revendedor son < que 1 entonces no puede revender boletos
	IF _MAXBOLETOSREVENTA < 1 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El vendedor no puede revender más boletos para este evento.';
    END IF;
    
    #Verificar el saldo del comprador
    SELECT saldo 
    INTO _SALDOCOMPRADOR 
    FROM Usuarios 
    WHERE idUsuario = _IDCOMPRADOR 
    FOR UPDATE;
    
    #Si el saldo es menor al precio de reventa entonces no le alcanza
    IF _SALDOCOMPRADOR < _PRECIOREVENTA THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Saldo insuficiente para realizar la compra.';
    END IF;
    
    #Comision
    SET _COMISION = _PRECIOREVENTA * 0.03;
    
    #Una vez verificado el saldo se realiza la transaccion del saldo
    UPDATE Usuarios 
    SET saldo = saldo - _PRECIOREVENTA 
    WHERE idUsuario = _IDCOMPRADOR;
    
    #Depositar la venta del boleto menos la comision
    UPDATE Usuarios 
    SET saldo = saldo + (_PRECIOREVENTA - _COMISION) 
    WHERE idUsuario = _IDVENDEDOR;
    
    #Se le asigna el boleto comprado al comprador
    UPDATE Boletos 
    SET idUsuario = _IDCOMPRADOR, estado = 'Vendido' 
    WHERE idBoleto = _IDBOLETO;
    
    #Registrar transaccion en la tabla transacciones
    INSERT INTO Transacciones (monto, tipo, estado, idBoleto, idComprador, idVendedor)
    VALUES (_PRECIOREVENTA, 'Reventa', 'Completada', _IDBOLETO, _IDCOMPRADOR, _IDVENDEDOR);
    
    COMMIT;
END$$
DELIMITER ;

-- Cambiar numero de serie cuando cambia de dueño un boleto

DELIMITER $$

CREATE TRIGGER actualizar_numero_interno
BEFORE UPDATE ON BOLETOS
FOR EACH ROW
BEGIN
IF NEW.idUsuario <> OLD.idUsuario THEN
SET NEW.numeroSerie = LEFT(UUID(), 8);
END IF;
END $$

DELIMITER ;


DELIMITER $$

CREATE PROCEDURE registrar_usuario(
    IN p_email VARCHAR(255),
    IN p_nombre VARCHAR(20),
    IN p_contraseña_hash VARCHAR(120),
    IN p_apellidoPaterno VARCHAR(20),
    IN p_apellidoMaterno VARCHAR(20),
    IN p_fechaNacimiento DATE,
    IN p_calle VARCHAR(30),
    IN p_ciudad VARCHAR(30),
    IN p_estado VARCHAR(30)
)
BEGIN
    DECLARE direccion_id INT;
    INSERT INTO DireccionesUsuarios (calle, ciudad, estado) 
    VALUES (p_calle, p_ciudad, p_estado);
    SET direccion_id = LAST_INSERT_ID();
    INSERT INTO Usuarios (email, nombre, contraseña_hash, apellidoPaterno, apellidoMaterno, fechaNacimiento, idDireccion)
    VALUES (p_email, p_nombre, p_contraseña_hash, p_apellidoPaterno, p_apellidoMaterno, p_fechaNacimiento, direccion_id);
END;
$$

DELIMITER ;



-- REGISTROS PRE-CARGADOS (FALTA CAMBIAR CONTRASEÑAS A QUE SOLO SEAN HASHES)
-- Insertar direcciones de usuarios
INSERT INTO DireccionesUsuarios (calle, ciudad, estado) VALUES
('Av. Reforma 123', 'Ciudad de México', 'CDMX'),
('Calle Morelos 456', 'Guadalajara', 'Jalisco'),
('Blvd. Independencia 789', 'Monterrey', 'Nuevo León'),
('Av. Juárez 101', 'Puebla', 'Puebla'),
('Calle Hidalgo 202', 'Mérida', 'Yucatán');

-- Insertar usuarios con direcciones asignadas
INSERT INTO Usuarios (email, nombre, contraseña_hash, apellidoPaterno, apellidoMaterno, fechaNacimiento, saldo, idDireccion) VALUES
('juan@example.com', 'Juan', '81d8a1eb780776316525e9d20de63ed95177945e37e40da3039a668420a591cd', 'Pérez', 'López', '1990-05-15', 2000.00, 1),
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
