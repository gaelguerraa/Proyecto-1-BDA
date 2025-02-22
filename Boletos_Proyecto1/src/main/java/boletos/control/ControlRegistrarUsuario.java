package boletos.control;

import boletos.entidades.Direccion;
import boletos.entidades.Usuario;
import boletos.persistencia.UsuariosDAO;
import boletos.presentacion.IniciarSesion;
import boletos.presentacion.RegistrarUsuario;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import javax.swing.JOptionPane;

public class ControlRegistrarUsuario {

    private RegistrarUsuario frmRegistrarUsuario;
    private IniciarSesion frmInicioSesion;
    private UsuariosDAO usuarioDAO;

    public ControlRegistrarUsuario(UsuariosDAO usuariosDAO, IniciarSesion frmIniciarSesion) {
        this.frmInicioSesion = frmIniciarSesion;
        this.usuarioDAO = usuariosDAO;
    }

    public void iniciarCasoUso() {
        this.frmRegistrarUsuario = new RegistrarUsuario(this);
        frmRegistrarUsuario.setVisible(true);
    }

    public boolean registrarUsuario(String email, String nombre, String contraseña, String apellidoPaterno, String apellidoMaterno, LocalDate fechaNacimiento, String calle, String ciudad, String estado) {
        // Convertir la contraseña en hash
        String contraseñaHash = generarHash(contraseña);

        if (contraseñaHash == null) {
            System.err.println("Error al generar el hash de la contraseña.");
            return false;
        }

        // Crea objetos DTO
        Direccion direccion = new Direccion(calle, ciudad, estado);
        Usuario usuario = new Usuario(email, nombre, contraseñaHash,
                apellidoPaterno, apellidoMaterno,
                fechaNacimiento, direccion);

        // Intenta registrar el usuario
        boolean registrado = usuarioDAO.registrarUsuario(usuario);

        if (registrado) {
            
            JOptionPane.showMessageDialog(null, "Usuario registrado con éxito. Ahora puedes iniciar sesión con tu id: " + usuarioDAO.obtenerIdPorEmail(email), "Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);
            frmRegistrarUsuario.dispose();  
            frmInicioSesion.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Error al registrar el usuario. Inténtalo de nuevo.", "Error", JOptionPane.ERROR_MESSAGE);
        }

        return registrado;

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

    public void regresar() {
        this.frmInicioSesion.setVisible(true);
    }

}
