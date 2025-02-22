package boletos.control;

import boletos.dtos.UsuarioDTO;
import boletos.persistencia.UsuariosDAO;
import boletos.presentacion.IniciarSesion;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ControlIniciarSesion {

    private IniciarSesion frmIniciarSesion;
    private UsuariosDAO usuarioDAO;
    private UsuarioDTO usuarioActual; // Guarda el usuario que ha iniciado sesión
    
    public ControlIniciarSesion(UsuariosDAO usuarioDAO){
        this.usuarioDAO = usuarioDAO;
    }
    
    public void iniciarCasoUso(){
        this.frmIniciarSesion = new IniciarSesion(this);
        this.frmIniciarSesion.setVisible(true);
    }
    

    public boolean iniciarSesion(Integer id, String contraseña) {
        UsuarioDTO usuario = usuarioDAO.obtenerUsuarioPorID(id);
        if (usuario != null && verificarHash(contraseña, usuario.getHashContraseña())) {
            usuarioActual = usuario; // Guardar el usuario que inició sesión
            return true;
        }
        return false;
    }

    private boolean verificarHash(String contraseña, String hashAlmacenado) {
        return hashAlmacenado.equals(generarHash(contraseña));
    }

    private String generarHash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public UsuarioDTO getUsuarioActual() {
        return usuarioActual;
    }
} 

