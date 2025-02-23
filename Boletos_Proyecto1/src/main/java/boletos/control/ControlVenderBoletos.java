package boletos.control;

import boletos.dtos.UsuarioDTO;
import boletos.entidades.Boleto;
import boletos.persistencia.BoletosDAO;
import boletos.presentacion.MenuPrincipal;
import boletos.presentacion.MisBoletos;
import boletos.presentacion.VentaBoletos;
import java.time.LocalDate;
import java.util.List;
import javax.swing.JOptionPane;


public class ControlVenderBoletos {
    private MisBoletos frmMisBoletos;
    private VentaBoletos frmVentaBoletos;
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
    
    public Boleto mostrarBoletoVenta(Integer id){
        return boletosDAO.obtenerBoletoPorId(id);
    }
    
    public void vender(Integer idBoleto){
        frmMisBoletos.dispose();
        frmVentaBoletos = new VentaBoletos(this, idBoleto);
        frmVentaBoletos.setVisible(true);

    }
    
    public void regresar(){
        this.frmMenuPrincipal.setVisible(true);
    }
    
    public void regresarMisBoletos(){
        this.frmMisBoletos.setVisible(true);
    }
    
    public void confirmarVenta(Integer idBoleto, double nuevoPrecio, LocalDate fechaLimite){
        if(boletosDAO.ponerBoletoEnVenta(idBoleto, nuevoPrecio, fechaLimite)){
            JOptionPane.showMessageDialog(frmVentaBoletos, "Boleto puesto en venta!.", "Venta", JOptionPane.INFORMATION_MESSAGE);
            frmVentaBoletos.dispose();
            this.frmMisBoletos = new MisBoletos(this);
            this.frmMisBoletos.setVisible(true);
        }
        else{
            JOptionPane.showMessageDialog(frmVentaBoletos, "No fue posible poner el boleto en venta.", "Fallo Venta", JOptionPane.INFORMATION_MESSAGE);
        }
        
    }
    
    
    
}
