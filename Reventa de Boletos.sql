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