package boletos.persistencia;

import boletos.entidades.Transaccion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

public class TransaccionDAO {
    
    private final ConexionBD manejadorConexiones;
    
    public TransaccionDAO(ConexionBD manejadorConexiones){
        this.manejadorConexiones = manejadorConexiones;
    }
    
//DEBE DEVOLVER TODAS LAS TRANSACCIONES
    public List<Transaccion> consularTransacciones(){
        String codigoSQL = """
                           SELECT * FROM TRANSACCIONES WHERE idUsuario = ?;
                           """;
                
        List<Transaccion> listaTransacciones = new LinkedList<>();
        try {
            Connection conexion = this.manejadorConexiones.crearConexion();
            PreparedStatement comando = conexion.prepareStatement(codigoSQL);
            ResultSet resultadosConsulta = comando.executeQuery();
            
            while(resultadosConsulta.next()){
                Integer idTransaccion = resultadosConsulta.getInt("idTransaccion");
                Timestamp fechaHora = resultadosConsulta.getTimestamp("fechaHora");
                double monto = resultadosConsulta.getDouble("monto");
                String tipo = resultadosConsulta.getString("tipo");
                String estado = resultadosConsulta.getString("estado");
                Integer idBoleto = resultadosConsulta.getInt("idBoleto");
                Integer idComprador = resultadosConsulta.getInt("idComprador");
                Integer idVendedor = resultadosConsulta.getInt("idVendedor");
                
                Transaccion transaccion = new Transaccion(idTransaccion, fechaHora, monto, tipo, estado, idBoleto, idComprador, idVendedor);
                listaTransacciones.add(transaccion);
                
            }
        } catch (SQLException ex) {
            System.err.println("Error al consultar los boletos: " + ex.getMessage());
        }
        
        return listaTransacciones;
      
    }
    
//DEBE MOSTRAR LAS TRANSACCIONES DE LAS COMPRAS DEL USUARIO 
    public List<Transaccion> consultarCompras(){
        String codigoSQL = """
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
                           """;
        
        List<Transaccion> listaCompras = new LinkedList<>();
        try {
            Connection conexion = this.manejadorConexiones.crearConexion();
            PreparedStatement comando = conexion.prepareStatement(codigoSQL);
            ResultSet resultadosConsulta = comando.executeQuery();
            
            while(resultadosConsulta.next()){
                Integer idTransaccion = resultadosConsulta.getInt("idTransaccion");
                String nombre = resultadosConsulta.getString("nombre");
                Timestamp fecha = resultadosConsulta.getTimestamp("fecha");
                String asiento = resultadosConsulta.getString("asiento");
                String fila = resultadosConsulta.getString("fila");
                String numeroSerie = resultadosConsulta.getString("numeroSerie");
                String estado = resultadosConsulta.getString("estado");
                Double monto = resultadosConsulta.getDouble("monto");
                String tipo = resultadosConsulta.getString("tipo");
                
                 Transaccion transaccion = new Transaccion(idTransaccion, nombre, fecha, asiento, fila, numeroSerie, estado, monto,tipo);
                 
                 listaCompras.add(transaccion);
                 
            }
        } catch (SQLException ex) {
            System.err.println("Error al consultar los boletos: " + ex.getMessage());
        }
        
        return listaCompras;
    }
    
    //DEBE MOSTRAR LAS TRANSACCIONES DE LAS VENTAS DEL USUARIO 
    public List<Transaccion> consultarVentas(){
        String codigoSQL = """
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
                           """;
        
        List<Transaccion> listaVentas = new LinkedList<>();
        try {
            Connection conexion = this.manejadorConexiones.crearConexion();
            PreparedStatement comando = conexion.prepareStatement(codigoSQL);
            ResultSet resultadosConsulta = comando.executeQuery();
            
            while(resultadosConsulta.next()){
                Integer idTransaccion = resultadosConsulta.getInt("idTransaccion");
                String nombre = resultadosConsulta.getString("nombre");
                Timestamp fecha = resultadosConsulta.getTimestamp("fecha");
                String asiento = resultadosConsulta.getString("asiento");
                String fila = resultadosConsulta.getString("fila");
                String numeroSerie = resultadosConsulta.getString("numeroSerie");
                String estado = resultadosConsulta.getString("estado");
                Double monto = resultadosConsulta.getDouble("monto");
                String tipo = resultadosConsulta.getString("tipo");
                
                 Transaccion transaccion = new Transaccion(idTransaccion, nombre, fecha, asiento, fila, numeroSerie, estado, monto,tipo);
                 
                 listaVentas.add(transaccion);
                 
            }
        } catch (SQLException ex) {
            System.err.println("Error al consultar los boletos: " + ex.getMessage());
        }
        
        return listaVentas;
    }
}
