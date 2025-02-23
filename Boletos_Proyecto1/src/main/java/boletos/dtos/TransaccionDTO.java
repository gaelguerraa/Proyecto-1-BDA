package boletos.dtos;

import java.sql.Timestamp;

/**
 *
 * @author gael_
 */
public class TransaccionDTO {
// Atributos actuales utilizados en consultarCompras y consultarVentas
    private Integer idTransaccion;
    private String evento;  // Evento relacionado
    private Timestamp fecha; // Fecha del evento
    private String asiento;  // Asiento donde se compró el boleto
    private String fila;     // Fila del asiento
    private String numeroSerie; // Número de serie del boleto
    private String estado;  // Estado de la transacción
    private Double monto;   // Monto de la transacción
    private String tipo;    // Tipo de transacción (ej. compra, devolución)

    // Atributos viejos que no están siendo utilizados actualmente
    private Timestamp fechaHora;  // Fecha y hora de la transacción
    private Integer idBoleto;    // ID del boleto
    private Integer idComprador; // ID del comprador
    private Integer idVendedor; // ID del vendedor

    // Constructor con los nuevos atributos
    public TransaccionDTO(Integer idTransaccion, String evento, Timestamp fecha, String asiento, String fila, 
                       String numeroSerie, String estado, Double monto, String tipo) {
        this.idTransaccion = idTransaccion;
        this.evento = evento;
        this.fecha = fecha;
        this.asiento = asiento;
        this.fila = fila;
        this.numeroSerie = numeroSerie;
        this.estado = estado;
        this.monto = monto;
        this.tipo = tipo;
    }

    // Constructor con los atributos viejos 
    public TransaccionDTO(Integer idTransaccion, Timestamp fechaHora, double monto, String tipo, String estado, 
                       Integer idBoleto, Integer idComprador, Integer idVendedor) {
        this.idTransaccion = idTransaccion;
        this.fechaHora = fechaHora;
        this.monto = monto;
        this.tipo = tipo;
        this.estado = estado;
        this.idBoleto = idBoleto;
        this.idComprador = idComprador;
        this.idVendedor = idVendedor;
    }

    // Getters y Setters para los nuevos atributos
    public Integer getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Integer idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    public String getEvento() {
        return evento;
    }

    public void setEvento(String evento) {
        this.evento = evento;
    }

    public Timestamp getFecha() {
        return fecha;
    }

    public void setFecha(Timestamp fecha) {
        this.fecha = fecha;
    }

    public String getAsiento() {
        return asiento;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public String getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(String numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    // Getters y Setters para los atributos viejos
    public Timestamp getFechaHora() {
        return fechaHora;
    }

    public void setFechaHora(Timestamp fechaHora) {
        this.fechaHora = fechaHora;
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