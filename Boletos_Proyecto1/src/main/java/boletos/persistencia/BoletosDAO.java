package boletos.persistencia;

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
    
    public BoletosDAO(ConexionBD manejadorConexiones){
        this.manejadorConexiones = manejadorConexiones;
    }
    // hacer para que solo devuelve los disponbles
    public List<Boleto> consultarBoletos(){
        String codigoSQL = """
                           SELECT B.idBoleto, E.nombre, E.fecha, B.asiento, B.fila, B.numeroSerie, B.estado, B.precioOriginal
                           FROM boletos AS B
                           INNER JOIN eventos AS E ON B.idEvento = E.idEvento
                           WHERE B.estado = 'Disponible';
                           """;
        List<Boleto> listaBoletos = new LinkedList<>();
        try{
            Connection conexion = this.manejadorConexiones.crearConexion();
            PreparedStatement comando = conexion.prepareStatement(codigoSQL);
            ResultSet resultadosConsulta = comando.executeQuery();
            
            // recorre las filas de la consulta
            while(resultadosConsulta.next()){
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
        }
        catch(SQLException ex){
            System.err.println("Error al consultar los boletos: " + ex.getMessage());
        }
        
        return listaBoletos;
    }
}
