package boletos.control;

import boletos.dtos.BoletoDTO;
import boletos.dtos.UsuarioDTO;
import boletos.entidades.Boleto;
import boletos.persistencia.BoletosDAO;
import boletos.presentacion.ReciboBoleto;
import boletos.presentacion.ComprarBoletosBusqueda;
import boletos.presentacion.ComprarBoletosDisponibilidad;
import boletos.presentacion.MenuPrincipal;
import java.util.List;
import javax.swing.JOptionPane;

public class ControlComprarBoletos {
    private ComprarBoletosDisponibilidad frmComprarBoletosDisponibilidad;
    private ComprarBoletosBusqueda frmComprarBoletosBusqueda;
    private BoletosDAO boletosDAO;
    private MenuPrincipal frmMenuPrincipal;
    private UsuarioDTO usuarioDTO;
    private ReciboBoleto frmBoletoAquirido;
    

    public ControlComprarBoletos(BoletosDAO boletosDAO, MenuPrincipal frmMenuPrincipal, UsuarioDTO usuarioActual) {
        this.boletosDAO = boletosDAO;
        this.frmMenuPrincipal = frmMenuPrincipal;
        this.usuarioDTO = usuarioActual;

    }
    
    public void iniciarCasoUso(){
        this.frmComprarBoletosBusqueda = new ComprarBoletosBusqueda(this);
        this.frmComprarBoletosBusqueda.setVisible(true);
//        this.frmComprarBoletosDisponibilidad = new ComprarBoletosDisponibilidad(this);
//        this.frmComprarBoletosDisponibilidad.setVisible(true);
    }
    
    public void busquedaNombre(String nombre){
        this.frmComprarBoletosDisponibilidad = new ComprarBoletosDisponibilidad(this, nombre);
        this.frmComprarBoletosDisponibilidad.setVisible(true);
    }
    
    public List<Boleto> consultarListaBoletos(){
        return this.boletosDAO.consultarBoletos();
    }
    
    public List<Boleto> consultarListaBoletosNombre(String nombre){
        return this.boletosDAO.consultarBoletosPorNombre(nombre);
    }
    
    public void comprarBoletosSistema(Integer idBoleto){
        
        boolean compraExitosa = this.boletosDAO.comprarBoleto(idBoleto, usuarioDTO.getIdUsuario());
        
        if (compraExitosa) {
            //Si la compra es exitosa se abre boletoAdquirido
            System.out.println(compraExitosa);
            JOptionPane.showMessageDialog(frmComprarBoletosDisponibilidad, "Se compro el boleto " + idBoleto + " con exito!", "Compra Exitosa!", JOptionPane.INFORMATION_MESSAGE);
        }
        else{
            // se tiene que actualizar a apartado
            System.out.println(compraExitosa);
            JOptionPane.showMessageDialog(frmComprarBoletosDisponibilidad, "El boleto " + idBoleto + " no fue posible comprar, \n Sera apartado durante 10 minutos.", "Fallo", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public void regresar(){
        this.frmMenuPrincipal.setVisible(true);
        
    }
    
    //Ventana de boletoAdquirido
    public void boletoAdquirido(){
        this.frmBoletoAquirido = new ReciboBoleto(this);
        this.frmBoletoAquirido.setVisible(true);
    }
    
    
    
    
    
    
    
    
    
}
