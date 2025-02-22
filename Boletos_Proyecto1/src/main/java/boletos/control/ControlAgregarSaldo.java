package boletos.control;

import boletos.dtos.UsuarioDTO;
import boletos.persistencia.UsuariosDAO;
import boletos.presentacion.AgregarSaldo;
import boletos.presentacion.MenuPrincipal;
import javax.swing.JOptionPane;

public class ControlAgregarSaldo {

    private AgregarSaldo frmAgregarSaldo;
    private MenuPrincipal frmMenuPrincipal;
    private UsuariosDAO usuariosDAO;
    private UsuarioDTO usuarioActual;

    public ControlAgregarSaldo(UsuariosDAO usuariosDAO, MenuPrincipal frmMenuPrincipal, UsuarioDTO usuarioActual) {
        this.frmAgregarSaldo = frmAgregarSaldo;
        this.frmMenuPrincipal = frmMenuPrincipal;
        this.usuariosDAO = usuariosDAO;
        this.usuarioActual = usuarioActual;
    }

    public void iniciarCasoUso() {
        this.frmAgregarSaldo = new AgregarSaldo(this);
        this.frmAgregarSaldo.setVisible(true);
    }

    public void regresar() {
        this.frmMenuPrincipal.setVisible(true);
    }

    public void agregarSaldo(Double cantidad) {
        if(usuariosDAO.agregarSaldoUsuario(usuarioActual.getIdUsuario(), cantidad)){
            JOptionPane.showMessageDialog(frmAgregarSaldo, "Saldo agregado.", "Deposito", JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            JOptionPane.showMessageDialog(frmAgregarSaldo, "Saldo no fue agregado.", "Fallo Deposito", JOptionPane.INFORMATION_MESSAGE);
        }
        

    }
    
    public double obtenerSaldoUsuario(){
        return usuariosDAO.obtenerSaldoUsuario(usuarioActual);
    }
    
    public void comprarBoletosApartados(){
        
    }

}
