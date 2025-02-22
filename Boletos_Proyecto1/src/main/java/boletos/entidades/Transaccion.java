package boletos.entidades;

import java.sql.Timestamp;

/**
 *
 * @author gael_
 */
public class Transaccion {
    private Integer idTransaccion;
    private Timestamp fechaHora;
    private double monto;
    private String tipo;
    private String estado;
    private Integer idBoleto;
    private Integer idComprador;
    private Integer idVendedor;

    public Transaccion(Integer idTransaccion, Timestamp fechaHora, double monto, String tipo, String estado, Integer idBoleto, Integer idComprador, Integer idVendedor) {
        this.idTransaccion = idTransaccion;
        this.fechaHora = fechaHora;
        this.monto = monto;
        this.tipo = tipo;
        this.estado = estado;
        this.idBoleto = idBoleto;
        this.idComprador = idComprador;
        this.idVendedor = idVendedor;
    }

    public Integer getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Integer idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Integer getIdBoleto() {
        return idBoleto;
    }

    public void setIdBoleto(Integer idBoleto) {
        this.idBoleto = idBoleto;
    }

    public Integer getIdComprador() {
        return idComprador;
    }

    public void setIdComprador(Integer idComprador) {
        this.idComprador = idComprador;
    }

    public Integer getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Integer idVendedor) {
        this.idVendedor = idVendedor;
    }
    
    
}
