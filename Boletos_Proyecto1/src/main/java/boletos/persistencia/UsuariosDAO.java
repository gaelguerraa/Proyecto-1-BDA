package boletos.persistencia;

import boletos.dtos.UsuarioDTO;
import boletos.entidades.Usuario;
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

    public boolean registrarUsuario(Usuario usuario) {
        String sql = "CALL registrar_usuario(?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = manejadorConexiones.crearConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getEmail());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getContrasena()); // Contraseña ya en hash
            stmt.setString(4, usuario.getApellidoPaterno());
            stmt.setString(5, usuario.getApellidoMaterno());
            stmt.setDate(6, Date.valueOf(usuario.getFechaNacimiento()));
            stmt.setString(7, usuario.getDireccion().getCalle());
            stmt.setString(8, usuario.getDireccion().getCiudad());
            stmt.setString(9, usuario.getDireccion().getEstado());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al registrar usuario: " + e.getMessage());
            return false;
        }
    }

    public Integer obtenerIdPorEmail(String email) {
        String sql = "SELECT idUsuario FROM Usuarios WHERE email = ?";
        try (Connection conn = manejadorConexiones.crearConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("idUsuario");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null si no encuentra el usuario
    }

}
