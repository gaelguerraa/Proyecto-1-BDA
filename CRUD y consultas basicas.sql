#Vista para ocultar el numero interno del boleto
CREATE VIEW boletosView AS SELECT idBoleto,numeroSerie,fila,asiento,precioOriginal,estado,idEvento,idUsuario FROM boletos;

#1. Insertar un nuevo usuario
INSERT INTO usuarios (email, nombre, contraseña_hash, apellidoPaterno, apellidoMaterno, fechaNacimiento, saldo, idDireccion)
VALUES('maria.lopez@example.com','Maria', 'xyz123', 'Lopez', 'Rodriguez', '1990-07-15', 100.00, 5);


#2. Buscar Boletos disponibles en base a fecha y hora
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

#3. Mostrar boletos disponibles ocultando el numero interno

SELECT bl.idBoleto,ev.nombre as evento,bl.numeroSerie,bl.fila, bl.asiento,bl.precioOriginal,bl.estado 
FROM boletosView as bl
INNER JOIN eventos as ev
ON bl.idEvento=ev.idEvento
WHERE bl.estado LIKE 'Disponible';

#4. Mostrar los boletos del usuario ocultando el numero interno

SELECT idBoleto, u.nombre as usuario,numeroSerie, fila, asiento, precioOriginal, estado, idEvento
FROM boletosView as b
INNER JOIN usuarios as u
on b.idUsuario= u.idUsuario
WHERE b.idUsuario = 1; # id del usuario del que se desea ver sus boletos

#5. Procedimiento para actualizar datos del usuario

DELIMITER $$
CREATE PROCEDURE ActualizarUsuario(
	#parametros
    IN _IDUSUARIO INT,
	IN _EMAIL VARCHAR(25),
	IN _NOMBRE VARCHAR(20),
	IN _APELLIDOPATERNO VARCHAR(20),
	IN _APELLIDOMATERNO VARCHAR(20),
	IN _FECHANACIMIENTO DATE,
	IN _CALLE VARCHAR(30),
	IN _CIUDAD VARCHAR(30),
	IN _ESTADO VARCHAR(30))

BEGIN
	#Se declara _IDDIRECCION
	DECLARE _IDDIRECCION INT;
    
    #Consulta la direccion del usuario y la guarda en _IDDIRECCION con el into
    SELECT idDireccion 
    INTO _IDDIRECCION 
    FROM usuarios
    WHERE idUsuario = _IDDIRECCION;
    
    #Actualizar datos del usuario
    UPDATE usuarios
    SET
		#Se actualizan los datos con los datos recibidos de parametro
		email = _EMAIL,
		nombre = _NOMBRE,
		apellidoPaterno = _APELLIDOPATERNO,
		apellidoMaterno = _APELLIDOMATERNO,
		fechaNacimiento = _FECHANACIMIENTO
    WHERE idUsuario = _IDUSUARIO;
    
    #Actualizar la direccion del usuario
    UPDATE direccionesusuarios 
    SET 
        calle = _CALLE, 
        ciudad = _CIUDAD, 
        estado = _ESTADO
    WHERE idDireccion = _IDDIRECCION;
    
END$$
DELIMITER ;


#6. SABER LAS COMPRAS DE UN USUARIO
SELECT 
    T.IDTRANSACCION, 
    E.NOMBRE AS Evento, 
    E.FECHA, 
    B.ASIENTO, 
    B.FILA, 
    B.NUMEROSERIE, 
    T.ESTADO, 
    T.MONTO, 
    T.TIPO  
FROM TRANSACCIONES T  
JOIN BOLETOS B ON T.IDBOLETO = B.IDBOLETO  
JOIN EVENTOS E ON B.IDEVENTO = E.IDEVENTO  
WHERE T.IDCOMPRADOR = ?; 

#7. SABER LAS VENTAS DE UN USUARIO
SELECT 
    T.IDTRANSACCION, 
    E.NOMBRE AS Evento, 
    E.FECHA, 
    B.ASIENTO, 
    B.FILA, 
    B.NUMEROSERIE, 
    T.ESTADO, 
    T.MONTO, 
    T.TIPO  
FROM TRANSACCIONES T  
JOIN BOLETOS B ON T.IDBOLETO = B.IDBOLETO  
JOIN EVENTOS E ON B.IDEVENTO = E.IDEVENTO  
WHERE T.IDVENDEDOR = ?; 
    
