package boletos.control;

import boletos.dtos.UsuarioDTO;
import boletos.entidades.Boleto;
import boletos.persistencia.BoletosDAO;
import boletos.presentacion.MenuPrincipal;
import boletos.presentacion.MisBoletos;
import java.util.List;


public class ControlVenderBoletos {
    private MisBoletos frmMisBoletos;
    private MenuPrincipal frmMenuPrincipal;
    private BoletosDAO boletosDAO;
    private UsuarioDTO usuarioActual;

    public ControlVenderBoletos(MenuPrincipal frmMenuPrincipal, BoletosDAO boletosDAO, UsuarioDTO usuarioActual) {
        this.frmMenuPrincipal = frmMenuPrincipal;
        this.boletosDAO = boletosDAO;
        this.usuarioActual = usuarioActual;
    }
   
    
    public void iniciarCasoUso(){
        this.frmMisBoletos = new MisBoletos(this);
        frmMisBoletos.setVisible(true);
    }
    
    public List<Boleto> mostrarMisBoletos(){
        return this.boletosDAO.consultarBoletosPorUsuario(usuarioActual.getIdUsuario());
    }
    
    public void regresar(){
        this.frmMenuPrincipal.setVisible(true);
    }
    
    
    
}
