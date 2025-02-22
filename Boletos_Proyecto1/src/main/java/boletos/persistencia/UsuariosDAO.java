package boletos.persistencia;

import boletos.dtos.UsuarioDTO;
import java.sql.*;

public class UsuariosDAO {

    private final ConexionBD manejadorConexiones;

    public UsuariosDAO(ConexionBD manejadorConexiones) {
        this.manejadorConexiones = manejadorConexiones;
    }

    public UsuarioDTO obtenerUsuarioPorID(Integer id) {
        String sql = "SELECT * FROM Usuarios WHERE idUsuario = ?";
        try (Connection conn = manejadorConexiones.crearConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
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
            System.err.println("No se pudo obtener el usuario: " + e);
        }
        return null;
    }

    public boolean agregarSaldoUsuario(Integer id, Double saldo) {
        String sql = "call AÑADIR_SALDO(?,?);";
        try (Connection conn = manejadorConexiones.crearConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.setDouble(2, saldo);
            boolean exito = stmt.execute(); // Ejecuta el procedimiento

            return true; // Si no hubo errores, asumimos que se ejecutó correctamente
            //regresa true si se realizo
        } catch (SQLException e) {
            System.err.println("No se pudo añadir el saldo: " + e);
            return false;
        }
    }

    public double obtenerSaldoUsuario(UsuarioDTO usuario) {
        String sql = "SELECT saldo FROM Usuarios WHERE idUsuario = ?";
        try (Connection conn = manejadorConexiones.crearConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, usuario.getIdUsuario());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("saldo"); // Retorna el saldo actualizado
            }
        } catch (SQLException e) {
            System.err.println("Error obteniendo el saldo: " + e.getMessage());
        }
        return 0.0;
    }
}
