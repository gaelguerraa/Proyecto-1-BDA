package boletos.dtos;

import java.sql.Timestamp;

/**
 *
 * @author gael_
 */
public class TransaccionDTO {
    private Integer idTransaccion;
    private Timestamp fechaHora;
    private double monto;
    private String tipo;
    private String estado;
    private Integer idBoleto;
    private Integer idComprador;
    private Integer idVendedor;

    public TransaccionDTO(Integer idTransaccion, Timestamp fechaHora, double monto, String tipo, String estado, Integer idBoleto, Integer idComprador, Integer idVendedor) {
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

    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public double getMonto() {
        return monto;
    }

    public String getTipo() {
        return tipo;
    }

    public String getEstado() {
        return estado;
    }

    public Integer getIdBoleto() {
        return idBoleto;
    }

    public Integer getIdComprador() {
        return idComprador;
    }

    public Integer getIdVendedor() {
        return idVendedor;
    }
    
    
}
