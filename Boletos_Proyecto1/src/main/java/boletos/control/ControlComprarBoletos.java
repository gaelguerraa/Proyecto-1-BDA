package boletos.control;

import boletos.dtos.UsuarioDTO;
import boletos.entidades.Boleto;
import boletos.persistencia.BoletosDAO;
import boletos.presentacion.ComprarBoletosBusqueda;
import boletos.presentacion.ComprarBoletosDisponibilidad;
import boletos.presentacion.MenuPrincipal;
import java.util.List;

public class ControlComprarBoletos {
    private ComprarBoletosDisponibilidad frmComprarBoletosDisponibilidad;
    private BoletosDAO boletosDAO;
    private MenuPrincipal frmMenuPrincipal;
    private UsuarioDTO usuarioDTO;

    public ControlComprarBoletos(BoletosDAO boletosDAO, MenuPrincipal frmMenuPrincipal, UsuarioDTO usuarioActual) {
        this.boletosDAO = boletosDAO;
        this.frmMenuPrincipal = frmMenuPrincipal;
        this.usuarioDTO = usuarioActual;
    }
    
    public void iniciarCasoUso(){
        this.frmComprarBoletosDisponibilidad = new ComprarBoletosDisponibilidad(this, usuarioDTO);
        this.frmComprarBoletosDisponibilidad.setVisible(true);
    }
    
    public List<Boleto> consultarListaBoletos(){
        return this.boletosDAO.consultarBoletos();
    }
    
    public void regresar(){
        this.frmMenuPrincipal.setVisible(true);
        
    }
    
    // regresar el mismo usuario al menu
    
    
    
}
