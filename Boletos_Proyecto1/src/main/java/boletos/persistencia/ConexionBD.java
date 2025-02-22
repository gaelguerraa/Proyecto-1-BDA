package boletos.persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {
    //informacion para conectarte a la bd
    private final String cadenaConexion = "jdbc:mysql://localhost/bdboletos";
    private final String usuario = "root";
    private final String contrasenia = "";
    
    public Connection crearConexion() throws SQLException {
        // establece conexion con el server bd
            Connection conexion = DriverManager.getConnection(cadenaConexion, usuario, contrasenia);
            return conexion;
    }
}
