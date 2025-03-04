CREATE DATABASE bdBoletos;
USE bdBoletos;

-- Tablas de la BD
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
-- Crea el atributo edad en usuarios y le asigna la diferencia de años entre su año de nacimiento y el año actual
DELIMITER //
CREATE PROCEDURE ActualizarEdades()
BEGIN
    UPDATE Usuarios 
    SET edad = TIMESTAMPDIFF(YEAR, fechaNacimiento, CURDATE());
END //
DELIMITER ;

-- EVENTO PARA EJECUTAR LA FUNCIÓN AUTOMÁTICAMENTE CADA AÑO (EL 1 DE ENERO)
-- Depues de un año se vuelve a llamar la funcion
CREATE EVENT IF NOT EXISTS EventoActualizarEdades
ON SCHEDULE EVERY 1 YEAR STARTS TIMESTAMP(CURDATE(), '00:00:00')
DO CALL ActualizarEdades();

-- VISTA BOLETOS SIN NUMERO INTERNO
CREATE VIEW boletosView AS SELECT idBoleto,numeroSerie,fila,asiento,precioOriginal,estado,idEvento,idUsuario FROM boletos;

-- PROCEDIMIENTO ALMACENADO PARA BUSCAR POR FECHA Y HORA
-- Creamos una funcion que muestra los boletos de un evento que tengan la fecha y hora que el usuario introdujo y estan disponibles
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
-- Vista para ver todos los eventos disponibles
CREATE VIEW boletos_Disponibles AS
SELECT B.idBoleto, E.nombre, E.fecha, B.asiento, B.fila, B.numeroSerie, B.estado, B.precioOriginal
FROM boletos AS B
INNER JOIN eventos AS E ON B.idEvento = E.idEvento
WHERE B.estado = 'Disponible';

-- PROCEDIMIENTO ALMACENADO CON TRANSACCION PARA AÑADIR SALDO A UN USUARIO
-- Añade el saldo dado al usuario dado
DELIMITER $$
CREATE PROCEDURE AÑADIR_SALDO (
    _IDUSUARIO INT, 
    _SALDO DECIMAL(10,2)
)
BEGIN
    START TRANSACTION;

    -- Se actualiza el saldo del usuario
    UPDATE USUARIOS 
    SET SALDO = SALDO + _SALDO
    WHERE IDUSUARIO = _IDUSUARIO;

    COMMIT;
    ROLLBACK;

END $$
DELIMITER ;

-- PROCEDIMIENTO ALMACENADO CON TRANSACCION PARA QUE EL USUARIO COMPRE UN BOLETO AL SISTEMA
-- Recibe el id del boleto a comprar y el id del usuario comprador
DELIMITER $$
CREATE PROCEDURE comprar_boleto_sistema(p_idBoleto int, p_idUsuario INT)
BEGIN
-- Declaramos las siguientes variables necesarias para la transaccion
DECLARE saldoUsuario DECIMAL(10, 2);
DECLARE precio DECIMAL(10, 2);
DECLARE estadoActual VARCHAR(30);
DECLARE boletoDueño int;
-- Obtenemos el saldo del usuario que quiere adquirir los boletos
SELECT saldo INTO saldoUsuario
FROM Usuarios
WHERE idUsuario=p_idUsuario;
START TRANSACTION;
-- Consultamos los datos del boleto que el usuario quiere comprar y les asignamos variables locales
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
    -- Verificamos si el boleto ya ha sido vendido y si es así, cancelamos la transacción y mostramos un mensaje.
    IF estadoActual = "Vendido" THEN
        ROLLBACK;
        SELECT 'No hay boletos disponibles para este evento y fecha.' AS Mensaje;
    ELSE
        -- Verificamos si el usuario tiene suficiente saldo y que el sistema es quien lo vende
        IF saldoUsuario < precio and boletoDueño IS null THEN
            -- Si no tiene suficiente saldo, el boleto se mostrara como apartado
            UPDATE Boletos 
            SET Estado = "Apartado"
            WHERE idBoleto = p_idBoleto;
			
            -- Guardamos la transaccion donde el boleto fue apartado
            INSERT INTO TRANSACCIONES (fechaHora, monto, tipo, estado, idBoleto, IdComprador) 
            VALUES (NOW(), precio, "Compra", "Procesando", p_idBoleto, p_idUsuario);
			
            -- Enviamos mensaje diciendo que el boleto ha sido apartado
            COMMIT;
            SELECT 'Boleto apartado. Saldo insuficiente para completar la compra.' AS Mensaje;
        ELSE
            -- Si tiene el usuario tiene suficiente saldo, le restamos el precio del boleto a su cuenta y actualizamos su saldo
            UPDATE Usuarios
            SET Saldo = saldoUsuario - precio
            WHERE idUsuario = p_idUsuario;
			
            -- Cambiamos el estado del boleto a vendido y le asignamos el id del usuario que lo compro
            UPDATE Boletos
            SET estado = 'Vendido', idUsuario = p_idUsuario
            WHERE idBoleto = p_idBoleto;
			
            -- Guardamos la transaccion de la compra y mandamos un menaje diciendo que la compra fue exitosa
            INSERT INTO TRANSACCIONES (fechaHora, monto, tipo, estado, idBoleto, IdComprador) 
            VALUES (NOW(), precio, "Compra", "Completada", p_idBoleto, p_idUsuario);

            COMMIT;
            SELECT 'Compra realizada exitosamente.' AS Mensaje;
        END IF;
    END IF;
END;
$$

DELIMITER ;
-- EVENTO PARA ACTUALIZAR ESTADO DE "APARTADO" A "DISPONIBLE" LUEGO DE 10 MINUTOS
SET GLOBAL event_scheduler = ON;
DELIMITER $$
CREATE EVENT IF NOT EXISTS actualizar_boletos_apartados
ON SCHEDULE EVERY 1 MINUTE
DO
BEGIN
	-- Cada 1 minuto se revisara la diferencia de tiempo de la fecha y hora de la tranaccion, y cuando esta diferencia sea mayor o igual a 10 minutos del tiempo actual se cambiara el estado del boleto de "Apartado" a "Disponible"  
    UPDATE Boletos
    SET estado = 'Disponible'
    WHERE estado = 'Apartado' AND TIMESTAMPDIFF(MINUTE, (SELECT MAX(fechaHora) FROM TRANSACCIONES WHERE TRANSACCIONES.idBoleto = Boletos.idBoleto), NOW()) >= 10;
END $$
DELIMITER ;
DELIMITER $$

-- PROCEDIMIENTO ALMACENADO PARA COMPRAR UN BOLETO QUE OTRO USUARIO VENDE
CREATE PROCEDURE comprar_reventa(
    IN p_idBoleto INT,  
    IN p_idUsuario INT 
)
BEGIN
	-- Declaramos variables que son necesarias para el procedimiento
    DECLARE saldoUsuario DECIMAL(10,2);
    DECLARE precioBoleto DECIMAL(10,2);
    DECLARE estadoBoleto VARCHAR(30);
    DECLARE idUsuarioVendedor INT;
    
    -- Obtenemos los valores de interes para la compra y las asginamos a variables locales
    SELECT precioOriginal, estado, idUsuario
    INTO precioBoleto, estadoBoleto, idUsuarioVendedor
    FROM Boletos
    WHERE idBoleto = p_idBoleto;

    -- Verificamos si el boleto existe, si no existe lanzamos un mensaje personalizado usando un codigo de error generico 
    IF precioBoleto IS NULL THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Boleto no encontrado.';
    END IF;

    -- Verificamos si el boleto esta disponible, si no esta disponible lanzamos un mensaje personalizado usando un codigo de error generico
    IF estadoBoleto = 'Vendido' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Este boleto ya ha sido vendido.';
    END IF;

    -- Obtenemos el saldo del usuario comprador
    SELECT saldo INTO saldoUsuario
    FROM Usuarios
    WHERE idUsuario = p_idUsuario;

    -- Verificamos si el comprador tiene saldo suficiente
    IF saldoUsuario < precioBoleto THEN
        -- En caso de que el usuario no tiene saldo suficiente, se aparta el boleto durante 10 minutos y se genera la transacción de reventa
        UPDATE Boletos
        SET estado = 'Apartado'
        WHERE idBoleto = p_idBoleto;

        -- Registramos la transacción de "Apartado" cuando el boleto es apartado
        INSERT INTO Transacciones (fechaHora, monto, tipo, estado, idBoleto, idComprador, idVendedor)
        VALUES (NOW(), precioBoleto, 'Reventa', 'Procesando', p_idBoleto, p_idUsuario, idUsuarioVendedor);
		
        -- Mostramos mensaje personalizado diciendo que el saldo es insuficiente y el boleto fue apartado
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'Saldo insuficiente. El boleto ha sido apartado por 10 minutos.';
    END IF;

    -- En caso de que el usuario tenga saldo suficiente inicia la transaccion
    START TRANSACTION;

    -- Restar el saldo del comprador
    UPDATE Usuarios 
    SET saldo = saldoUsuario - precioBoleto
    WHERE idUsuario = p_idUsuario;

    -- Transferir el boleto al comprador y cambiar su estado a 'Vendido'
    UPDATE Boletos
    SET idUsuario = p_idUsuario, estado = 'Vendido'
    WHERE idBoleto = p_idBoleto;

    -- Acreditamos el monto del boleto menos el 3% de comision al vendedor
    UPDATE Usuarios
    SET saldo = saldo + (precioBoleto * 0.97)
    WHERE idUsuario = idUsuarioVendedor;

    -- Registramos la transacción de compra
    INSERT INTO Transacciones (fechaHora, monto, tipo, estado, idBoleto, idComprador, idVendedor)
    VALUES (NOW(), precioBoleto, 'Compra', 'Completada', p_idBoleto, p_idUsuario, idUsuarioVendedor);

    COMMIT;

    SELECT 'Compra realizada con éxito.' AS Mensaje;
END $$
DELIMITER ;

-- PROCEDIMIENTO ALMACENADO PARA REGISTRAR USUARIO
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

-- PROCEDIMIENTO ALMACENADO PARA OBTENER LOS BOLETOS DE UN USUARIO 
DELIMITER //
CREATE PROCEDURE obtenerBoletos(IN id INT)
BEGIN
    SELECT 
        B.idBoleto, 
        E.nombre,
        E.recinto,
        B.estado,
        E.fecha, 
        B.asiento, 
        B.fila, 
        B.numeroSerie, 
        B.precioOriginal 
    FROM Boletos AS B
    INNER JOIN eventos AS E ON B.idEvento = E.idEvento
    WHERE idUsuario = id AND B.estado = "Vendido";
END //
DELIMITER ;

-- PROCEDIMIENTO ALMACENADO PARA BUSCAR UN BOLETO POR SU ID
DELIMITER //
CREATE PROCEDURE obtenerBoletoPorId(IN p_idBoleto INT)
BEGIN
    SELECT 
        B.idBoleto, 
        E.nombre,
        E.recinto,
        B.estado,
        E.fecha, 
        B.asiento, 
        B.fila, 
        B.numeroSerie, 
        B.precioOriginal
    FROM Boletos AS B
    INNER JOIN Eventos AS E on E.idEvento = B.idEvento
    WHERE B.idBoleto = p_idBoleto;
END //
DELIMITER ;

-- TRANSACCION PARA PONER BOLETOS EN VENTA
DELIMITER $$
CREATE PROCEDURE poner_en_venta(
    IN p_idBoleto INT,
    IN p_precioNuevo DECIMAL(10,2),
    IN p_fechaLimite DATE
)
BEGIN
	-- Declaramos las variables necesarias
    DECLARE v_precioOriginal DECIMAL(10,2);
    DECLARE v_estadoActual VARCHAR(20);

    START TRANSACTION;

    -- Obtenemos el precio original y el estado actual del boleto
    SELECT precioOriginal, estado 
    INTO v_precioOriginal, v_estadoActual
    FROM Boletos 
    WHERE idBoleto = p_idBoleto;

    -- Si el boleto no existe, lanzamos error con el codigo personalizado de errores de SQL 45000
    IF v_precioOriginal IS NULL THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: El boleto no existe';
        ROLLBACK;
    END IF;

    -- Validamos que el boleto este marcado como "Vendido", de lo contrario no se podra revender
    IF v_estadoActual <> 'Vendido' THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: Solo puedes revender boletos en estado "Vendido"';
        ROLLBACK;
    END IF;

    -- Validamos que el precio nuevo no sea mayor al 3% del precio original
    IF p_precioNuevo > (v_precioOriginal * 1.03) THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Error: El precio de reventa no puede ser mayor al 3% del precio original';
        ROLLBACK;
    END IF;

    -- Actualizamos el estado del boleto y su nuevo precio
    UPDATE Boletos
    SET estado = 'Disponible', precioOriginal = p_precioNuevo
    WHERE idBoleto = p_idBoleto;

    COMMIT;
END $$
DELIMITER ;

-- PROCEDIMIENTO PARA QUITAR LOS BOLETOS QUE NO SE VENDIERON DESPUES DE LA FECHA LIMITE  
DELIMITER $$
CREATE PROCEDURE actualizar_boletos_vencidos()
BEGIN
    UPDATE Boletos
    SET estado = 'Vendido'
    WHERE estado = 'Disponible' AND fechaLimiteReventa < CURDATE();
END $$
DELIMITER ;




-- REGISTROS PRE-CARGADOS 
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


