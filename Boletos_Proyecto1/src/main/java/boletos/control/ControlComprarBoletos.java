package boletos.control;

import boletos.dtos.BoletoDTO;
import boletos.dtos.UsuarioDTO;
import boletos.entidades.Boleto;
import boletos.persistencia.BoletosDAO;
import boletos.presentacion.BoletoAdquirido;
import boletos.presentacion.ComprarBoletosBusqueda;
import boletos.presentacion.ComprarBoletosDisponibilidad;
import boletos.presentacion.MenuPrincipal;
import java.util.List;

public class ControlComprarBoletos {
    private ComprarBoletosDisponibilidad frmComprarBoletosDisponibilidad;
    private BoletosDAO boletosDAO;
    private MenuPrincipal frmMenuPrincipal;
    private UsuarioDTO usuarioDTO;
    private BoletoAdquirido frmBoletoAquirido;
    

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
    
    public void comprarBoletosSistema(Integer idBoleto){
        
        boolean compraExitosa = this.boletosDAO.comprarBoletoSistema(idBoleto, usuarioDTO.getIdUsuario());
        
        if (compraExitosa) {
            //Si la compra es exitosa se abre boletoAdquirido
            boletoAdquirido();
        }
    }
    
    public void regresar(){
        this.frmMenuPrincipal.setVisible(true);
        
    }
    
    //Ventana de boletoAdquirido
    public void boletoAdquirido(){
        this.frmBoletoAquirido = new BoletoAdquirido(this);
        this.frmBoletoAquirido.setVisible(true);
    }
    
    
    
    // regresar el mismo usuario al menu
    
    
    
}
