/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boletos.control;

import boletos.persistencia.TransaccionDAO;
import boletos.presentacion.Historial;

/**
 *
 * @author gael_
 */
public class ControlHistorial {
    private TransaccionDAO transaccionDAO;
    
    public ControlHistorial(TransaccionDAO transaccionDAO){
        this.transaccionDAO = transaccionDAO;
        
    }
    
    public void mostrarHistorial(){
        Historial historial = new Historial(transaccionDAO);
        historial.setVisible(true);
    }
}
