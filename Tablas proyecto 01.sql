CREATE DATABASE bdBoletos;
USE bdBoletos;

CREATE TABLE Usuarios (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    nombre VARCHAR(20) NOT NULL,
    contraseña_hash VARCHAR(30) NOT NULL,
    apellidoPaterno VARCHAR(20) NOT NULL,
    apellidoMaterno VARCHAR(20) NOT NULL,
    fechaNacimiento DATE NOT NULL,
    saldo DECIMAL(10,2) NOT NULL DEFAULT 0,
    edad INT NOT NULL
    idDireccion INT NOT NULL,
    FOREIGN KEY (idDireccion) REFERENCES DireccionesUsuarios(idDireccion) ON DELETE CASCADE,
);

CREATE TABLE DireccionesUsuarios (
    idDireccion INT AUTO_INCREMENT PRIMARY KEY
    calle VARCHAR(30) NOT NULL,
    ciudad VARCHAR(30) NOT NULL,
    estado VARCHAR(30) NOT NULL,
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
    estado VARCHAR("Disponible", "Apartado", "Vendido") NOT NULL,
    idEvento INT NOT NULL,
    idPersona INT NOT NULL,
    FOREIGN KEY (idEvento) REFERENCES Eventos(idEvento) ON DELETE CASCADE,
    FOREIGN KEY (idPersona) REFERENCES Personas(idPersona) ON DELETE CASCADE
);

CREATE TABLE Transacciones (
    idTransaccion INT AUTO_INCREMENT PRIMARY KEY,
    fechaHora DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    monto DECIMAL(10,2) NOT NULL,
    tipo VARCHAR("Compra", "Reventa") NOT NULL,
    estado VARCHAR("Completada", "Cancelada") NOT NULL,
    idBoleto INT NOT NULL,
    idComprador INT NOT NULL,
    idVendedor INT NOT NULL,
    FOREIGN KEY (idBoleto) REFERENCES Boletos(idBoleto) ON DELETE CASCADE,
    FOREIGN KEY (idComprador) REFERENCES Personas(idPersona) ON DELETE CASCADE,
    FOREIGN KEY (idVendedor) REFERENCES Personas(idPersona) ON DELETE CASCADE
);

-- FUNCIÓN PARA ACTUALIZAR EDADES CADA AÑO
DELIMITER //
CREATE PROCEDURE ActualizarEdades()
BEGIN
    UPDATE Personas 
    SET edad = TIMESTAMPDIFF(YEAR, fechaNacimiento, CURDATE());
END //
DELIMITER ;

-- EVENTO PARA EJECUTAR LA FUNCIÓN AUTOMÁTICAMENTE CADA AÑO (EL 1 DE ENERO)
CREATE EVENT IF NOT EXISTS EventoActualizarEdades
ON SCHEDULE EVERY 1 YEAR STARTS TIMESTAMP(CURDATE(), '00:00:00')
DO CALL ActualizarEdades();
