<<<<<<< HEAD
package boletos.persistencia;

import boletos.dtos.BoletoDTO;
import boletos.entidades.Boleto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.sql.Timestamp;

public class BoletosDAO {

    private final ConexionBD manejadorConexiones;

    public BoletosDAO(ConexionBD manejadorConexiones) {
        this.manejadorConexiones = manejadorConexiones;
    }

    // hacer para que solo devuelve los disponbles
    public List<Boleto> consultarBoletos() {
        String codigoSQL = """
                           SELECT B.idBoleto, E.nombre, E.fecha, B.asiento, B.fila, B.numeroSerie, B.estado, B.precioOriginal
                           FROM boletos AS B
                           INNER JOIN eventos AS E ON B.idEvento = E.idEvento
                           WHERE B.estado = 'Disponible';
                           """;
        List<Boleto> listaBoletos = new LinkedList<>();
        try {
            Connection conexion = this.manejadorConexiones.crearConexion();
            PreparedStatement comando = conexion.prepareStatement(codigoSQL);
            ResultSet resultadosConsulta = comando.executeQuery();

            // recorre las filas de la consulta
            while (resultadosConsulta.next()) {
                Integer idBoleto = resultadosConsulta.getInt("idBoleto");
                String evento = resultadosConsulta.getString("nombre");
                Timestamp fecha = resultadosConsulta.getTimestamp("fecha");
                String asiento = resultadosConsulta.getString("asiento");
                String fila = resultadosConsulta.getString("fila");
                String numSerie = resultadosConsulta.getString("numeroSerie");
                String estado = resultadosConsulta.getString("estado");
                Double precio = resultadosConsulta.getDouble("precioOriginal");

                Boleto boleto = new Boleto(idBoleto, numSerie, fila, asiento, precio, estado, evento, fecha);
                listaBoletos.add(boleto);
            }
        } catch (SQLException ex) {
            System.err.println("Error al consultar los boletos: " + ex.getMessage());
        }

        return listaBoletos;
    }

    public List<Boleto> consultarBoletosPorNombre(String nombre) {
        String codigoSQL = """
                           SELECT B.idBoleto, E.nombre, E.fecha, B.asiento, B.fila, B.numeroSerie, B.estado, B.precioOriginal
                           FROM boletos AS B
                           INNER JOIN eventos AS E ON B.idEvento = E.idEvento
                           WHERE B.estado = 'Disponible' AND E.nombre = ?;
                           """;
        List<Boleto> listaBoletos = new LinkedList<>();
        try {
            Connection conexion = this.manejadorConexiones.crearConexion();
            PreparedStatement comando = conexion.prepareStatement(codigoSQL);
            comando.setString(1, nombre);
            ResultSet resultadosConsulta = comando.executeQuery();

            // recorre las filas de la consulta
            while (resultadosConsulta.next()) {
                Integer idBoleto = resultadosConsulta.getInt("idBoleto");
                String evento = resultadosConsulta.getString("nombre");
                Timestamp fecha = resultadosConsulta.getTimestamp("fecha");
                String asiento = resultadosConsulta.getString("asiento");
                String fila = resultadosConsulta.getString("fila");
                String numSerie = resultadosConsulta.getString("numeroSerie");
                String estado = resultadosConsulta.getString("estado");
                Double precio = resultadosConsulta.getDouble("precioOriginal");

                Boleto boleto = new Boleto(idBoleto, numSerie, fila, asiento, precio, estado, evento, fecha);
                listaBoletos.add(boleto);
            }
        } catch (SQLException ex) {
            System.err.println("Error al consultar los boletos: " + ex.getMessage());
        }

        return listaBoletos;
    }

    // Comprar Boleto (Sistema o Reventa)
    public boolean comprarBoleto(Integer idBoleto, Integer idUsuario) {
        String verificarSQL = "SELECT idUsuario FROM Boletos WHERE idBoleto = ?";
        // revisa si el boleto tiene un id de un usuario
        try (Connection conexion = this.manejadorConexiones.crearConexion(); PreparedStatement verificarComando = conexion.prepareStatement(verificarSQL)) {

            verificarComando.setInt(1, idBoleto);
            ResultSet resultado = verificarComando.executeQuery();

            if (resultado.next()) {
                Integer idUsuarioActual = resultado.getObject("idUsuario", Integer.class); // Puede ser NULL

                // Si idUsuarioActual es NULL, el boleto lo vende el sistema
                if (idUsuarioActual == null) {
                    return ejecutarCompra("CALL comprar_boleto_sistema(?, ?);", idBoleto, idUsuario);
                } else {
                    return ejecutarCompra("CALL comprar_reventa(?, ?);", idBoleto, idUsuario);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al verificar el boleto: " + ex.getMessage());
        }

        return false; // Retorna falso si hubo algún error o el boleto no existe
    }

// Ejecutar compra segun corresponda
    private boolean ejecutarCompra(String sql, Integer idBoleto, Integer idUsuario) {
        try (Connection conexion = this.manejadorConexiones.crearConexion(); PreparedStatement comando = conexion.prepareStatement(sql)) {

            comando.setInt(1, idBoleto);
            comando.setInt(2, idUsuario);
            comando.execute(); // Ejecutamos el procedimiento

            // Verificamos si el boleto fue actualizado
            String verificarSQL = "SELECT idUsuario FROM Boletos WHERE idBoleto = ?";
            try (PreparedStatement verificarComando = conexion.prepareStatement(verificarSQL)) {
                verificarComando.setInt(1, idBoleto);
                ResultSet resultado = verificarComando.executeQuery();

                if (resultado.next()) {
                    int idUsuarioRegistrado = resultado.getInt("idUsuario");
                    return idUsuarioRegistrado == idUsuario;
                }
            }

        } catch (SQLException ex) {
            System.err.println("Error al comprar el boleto: " + ex.getMessage());
        }

        return false;
    }
}
=======
package boletos.persistencia;

import boletos.dtos.BoletoDTO;
import boletos.entidades.Boleto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.sql.Timestamp;

public class BoletosDAO {

    private final ConexionBD manejadorConexiones;

    public BoletosDAO(ConexionBD manejadorConexiones) {
        this.manejadorConexiones = manejadorConexiones;
    }

    // hacer para que solo devuelve los disponbles
    public List<Boleto> consultarBoletos() {
        String codigoSQL = """
                           SELECT B.idBoleto, E.nombre, E.fecha, B.asiento, B.fila, B.numeroSerie, B.estado, B.precioOriginal
                           FROM boletos AS B
                           INNER JOIN eventos AS E ON B.idEvento = E.idEvento
                           WHERE B.estado = 'Disponible';
                           """;
        List<Boleto> listaBoletos = new LinkedList<>();
        try {
            Connection conexion = this.manejadorConexiones.crearConexion();
            PreparedStatement comando = conexion.prepareStatement(codigoSQL);
            ResultSet resultadosConsulta = comando.executeQuery();

            // recorre las filas de la consulta
            while (resultadosConsulta.next()) {
                Integer idBoleto = resultadosConsulta.getInt("idBoleto");
                String evento = resultadosConsulta.getString("nombre");
                Timestamp fecha = resultadosConsulta.getTimestamp("fecha");
                String asiento = resultadosConsulta.getString("asiento");
                String fila = resultadosConsulta.getString("fila");
                String numSerie = resultadosConsulta.getString("numeroSerie");
                String estado = resultadosConsulta.getString("estado");
                Double precio = resultadosConsulta.getDouble("precioOriginal");

                Boleto boleto = new Boleto(idBoleto, numSerie, fila, asiento, precio, estado, evento, fecha);
                listaBoletos.add(boleto);
            }
        } catch (SQLException ex) {
            System.err.println("Error al consultar los boletos: " + ex.getMessage());
        }

        return listaBoletos;
    }

    public List<Boleto> consultarBoletosPorNombre(String nombre) {
        String codigoSQL = """
                           SELECT B.idBoleto, E.nombre, E.fecha, B.asiento, B.fila, B.numeroSerie, B.estado, B.precioOriginal
                           FROM boletos AS B
                           INNER JOIN eventos AS E ON B.idEvento = E.idEvento
                           WHERE B.estado = 'Disponible' AND E.nombre = ?;
                           """;
        List<Boleto> listaBoletos = new LinkedList<>();
        try {
            Connection conexion = this.manejadorConexiones.crearConexion();
            PreparedStatement comando = conexion.prepareStatement(codigoSQL);
            comando.setString(1, nombre);
            ResultSet resultadosConsulta = comando.executeQuery();

            // recorre las filas de la consulta
            while (resultadosConsulta.next()) {
                Integer idBoleto = resultadosConsulta.getInt("idBoleto");
                String evento = resultadosConsulta.getString("nombre");
                Timestamp fecha = resultadosConsulta.getTimestamp("fecha");
                String asiento = resultadosConsulta.getString("asiento");
                String fila = resultadosConsulta.getString("fila");
                String numSerie = resultadosConsulta.getString("numeroSerie");
                String estado = resultadosConsulta.getString("estado");
                Double precio = resultadosConsulta.getDouble("precioOriginal");

                Boleto boleto = new Boleto(idBoleto, numSerie, fila, asiento, precio, estado, evento, fecha);
                listaBoletos.add(boleto);
            }
        } catch (SQLException ex) {
            System.err.println("Error al consultar los boletos: " + ex.getMessage());
        }

        return listaBoletos;
    }

    // Comprar Boleto (Sistema o Reventa)
    public boolean comprarBoleto(Integer idBoleto, Integer idUsuario) {
        String verificarSQL = "SELECT idUsuario FROM Boletos WHERE idBoleto = ?";
        // revisa si el boleto tiene un id de un usuario
        try (Connection conexion = this.manejadorConexiones.crearConexion(); PreparedStatement verificarComando = conexion.prepareStatement(verificarSQL)) {

            verificarComando.setInt(1, idBoleto);
            ResultSet resultado = verificarComando.executeQuery();

            if (resultado.next()) {
                Integer idUsuarioActual = resultado.getObject("idUsuario", Integer.class); // Puede ser NULL

                // Si idUsuarioActual es NULL, el boleto lo vende el sistema
                if (idUsuarioActual == null) {
                    return ejecutarCompra("CALL comprar_boleto_sistema(?, ?);", idBoleto, idUsuario);
                } else {
                    return ejecutarCompra("CALL comprar_reventa(?, ?);", idBoleto, idUsuario);
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al verificar el boleto: " + ex.getMessage());
        }

        return false; // Retorna falso si hubo algún error o el boleto no existe
    }

// Ejecutar compra segun corresponda
    private boolean ejecutarCompra(String sql, Integer idBoleto, Integer idUsuario) {
        try (Connection conexion = this.manejadorConexiones.crearConexion(); PreparedStatement comando = conexion.prepareStatement(sql)) {

            comando.setInt(1, idBoleto);
            comando.setInt(2, idUsuario);
            comando.execute(); // Ejecutamos el procedimiento

            // Verificamos si el boleto fue actualizado
            String verificarSQL = "SELECT idUsuario FROM Boletos WHERE idBoleto = ?";
            try (PreparedStatement verificarComando = conexion.prepareStatement(verificarSQL)) {
                verificarComando.setInt(1, idBoleto);
                ResultSet resultado = verificarComando.executeQuery();

                if (resultado.next()) {
                    int idUsuarioRegistrado = resultado.getInt("idUsuario");
                    return idUsuarioRegistrado == idUsuario;
                }
            }

        } catch (SQLException ex) {
            System.err.println("Error al comprar el boleto: " + ex.getMessage());
        }

        return false;
    }
}
>>>>>>> 5c1a0f8a8c2778d59e747db91965ee1e5547f0bd
