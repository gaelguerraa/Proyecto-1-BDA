/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boletos.presentacion;

import boletos.control.ControlIniciarSesion;
import boletos.persistencia.ConexionBD;
import boletos.persistencia.UsuariosDAO;

/**
 *
 * @author jorge
 */
public class main {

    public static void main(String[] args) {
        ConexionBD manejadorConexiones = new ConexionBD();
        UsuariosDAO usuarioDAO = new UsuariosDAO(manejadorConexiones);
        ControlIniciarSesion control = new ControlIniciarSesion(usuarioDAO);
        control.iniciarCasoUso();
    }

}
