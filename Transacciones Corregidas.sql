USE BDBOLETOS;

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

CREATE PROCEDURE comprar_reventa(
    IN p_idBoleto INT, 
    IN p_idUsuario INT
)
BEGIN
    DECLARE saldoUsuario DECIMAL(10,2);
    DECLARE precioOriginal DECIMAL(10,2);
    DECLARE precioReventa DECIMAL(10,2);
    DECLARE estadoActual VARCHAR(30);
    DECLARE boletoDueño INT;
    DECLARE precioMaximo DECIMAL(10,2);
    DECLARE totalBoletos INT;
    DECLARE boletosEnReventa INT;
    DECLARE maxBoletosEnReventa INT;

    -- Obtener datos del boleto
    SELECT precioOriginal, precioReventa, estado, idUsuario 
    INTO precioOriginal, precioReventa, estadoActual, boletoDueño
    FROM Boletos
    WHERE idBoleto = p_idBoleto;

    -- Contar cuántos boletos tiene el usuario
    SELECT COUNT(*) INTO totalBoletos 
    FROM Boletos 
    WHERE idUsuario = boletoDueño;

    -- Calcular el máximo de boletos que puede revender
    SET maxBoletosEnReventa = FLOOR(totalBoletos / 2);

    -- Contar cuántos boletos ya están en reventa
    SELECT COUNT(*) INTO boletosEnReventa 
    FROM Boletos 
    WHERE idUsuario = boletoDueño AND estado = 'Reventa';

    -- Verificar si el boleto está en reventa
    IF estadoActual != "Reventa" THEN
        SELECT 'Este boleto no está disponible para reventa.' AS Mensaje;
    ELSE
        -- Verificar que el usuario no compre su propio boleto
        IF boletoDueño = p_idUsuario THEN
            SELECT 'No puedes comprar tu propio boleto en reventa.' AS Mensaje;
        ELSE
            -- Verificar si el usuario ha alcanzado el límite de reventa
            IF boletosEnReventa >= maxBoletosEnReventa THEN
                SELECT 'Has alcanzado el límite de boletos en reventa.' AS Mensaje;
            ELSE
                -- Obtener el saldo del usuario comprador
                SELECT saldo INTO saldoUsuario
                FROM Usuarios
                WHERE idUsuario = p_idUsuario;

                -- Calcular precio máximo permitido (3% extra)
                SET precioMaximo = precioOriginal * 1.03;

                -- Verificar que el precio de reventa no exceda el límite
                IF precioReventa > precioMaximo THEN
                    SELECT 'El precio de reventa supera el límite permitido.' AS Mensaje;
                ELSE
                    -- Verificar si el usuario tiene saldo suficiente
                    IF saldoUsuario < precioReventa THEN
                        SELECT 'Saldo insuficiente para completar la compra.' AS Mensaje;
                    ELSE
                        START TRANSACTION;

                        -- Restar el saldo del comprador
                        UPDATE Usuarios 
                        SET Saldo = saldoUsuario - precioReventa 
                        WHERE idUsuario = p_idUsuario;

                        -- Agregar el saldo al vendedor original
                        UPDATE Usuarios 
                        SET Saldo = Saldo + precioReventa 
                        WHERE idUsuario = boletoDueño;

                        -- Transferir el boleto al comprador
                        UPDATE Boletos 
                        SET idUsuario = p_idUsuario, estado = "Vendido"
                        WHERE idBoleto = p_idBoleto;

                        -- Registrar la transacción de reventa
                        INSERT INTO TRANSACCIONES (fechaHora, monto, tipo, estado, idBoleto, IdComprador) 
                        VALUES (NOW(), precioReventa, "Reventa", "Completada", p_idBoleto, p_idUsuario);

                        COMMIT;
                        SELECT 'Compra de reventa realizada con éxito.' AS Mensaje;
                    END IF;
                END IF;
            END IF;
        END IF;
    END IF;
END;
$$

DELIMITER ;
