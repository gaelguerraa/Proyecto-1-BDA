package boletos.control;

import boletos.dtos.UsuarioDTO;
import boletos.persistencia.BoletosDAO;
import boletos.presentacion.MenuPrincipal;
import boletos.presentacion.MisBoletos;


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
    
    public void mostrarMisBoletos(){
        
    }
    
    public void iniciarCasoUso(){
        this.frmMisBoletos = new MisBoletos(this);
        frmMisBoletos.setVisible(true);
    }
    
    public void regresar(){
        this.frmMenuPrincipal.setVisible(true);
    }
    
    
    
}
