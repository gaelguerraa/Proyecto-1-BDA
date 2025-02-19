package boletos.persistencia;

import boletos.dtos.UsuarioDTO;
import java.sql.*;

public class UsuariosDAO {
    
    private final ConexionBD manejadorConexiones;
    
    public UsuariosDAO(ConexionBD manejadorConexiones){
        this.manejadorConexiones = manejadorConexiones;
    }
    
    public UsuarioDTO obtenerUsuarioPorID(Integer id) {
        String sql = "SELECT * FROM Usuarios WHERE idUsuario = ?";
        try (Connection conn = manejadorConexiones.crearConexion();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new UsuarioDTO(
                    rs.getInt("idUsuario"),
                    rs.getString("email"),
                    rs.getString("nombre"),
                    rs.getString("contraseña_hash"),
                    rs.getDouble("saldo")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

