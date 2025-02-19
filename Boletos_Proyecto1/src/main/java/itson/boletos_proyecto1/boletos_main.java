/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package itson.boletos_proyecto1;

import boletos.persistencia.ConexionBD;
import boletos.presentacion.AgregarSaldo;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jorge
 */
public class boletos_main {

    public static void main(String[] args) {

        AgregarSaldo frmAgregarSaldo = new AgregarSaldo();
        ConexionBD conexionbd = new ConexionBD();
        try {
            System.out.println(conexionbd.crearConexion());
            frmAgregarSaldo.setVisible(true);
        } catch (SQLException ex) {
            System.err.println(ex);
        }
    }
}
