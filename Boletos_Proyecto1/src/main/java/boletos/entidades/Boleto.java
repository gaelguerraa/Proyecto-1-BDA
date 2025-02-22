<<<<<<< HEAD
package boletos.entidades;

import java.sql.Timestamp;

public class Boleto {
    private Integer idBoleto;
    private String numSerie;
    private String fila;
    private String asiento;
    private Double precio;
    private String estado;
    private String evento;
    private Timestamp fecha;

    public Boleto() {
        
    }

    public Boleto(Integer idBoleto, String numSerie, String fila, String asiento, Double precio, String estado, String evento, Timestamp fecha) {
        this.idBoleto = idBoleto;
        this.numSerie = numSerie;
        this.fila = fila;
        this.asiento = asiento;
        this.precio = precio;
        this.estado = estado;
        this.evento = evento;
        this.fecha = fecha;
    }

    public Boleto(String numSerie, String fila, String asiento, Double precio, String estado) {
        this.numSerie = numSerie;
        this.fila = fila;
        this.asiento = asiento;
        this.precio = precio;
        this.estado = estado;
    }

    public Integer getIdBoleto() {
        return idBoleto;
    }

    public void setIdBoleto(Integer idBoleto) {
        this.idBoleto = idBoleto;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public String getAsiento() {
        return asiento;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
    
}
=======
package boletos.entidades;

import java.sql.Timestamp;

public class Boleto {
    private Integer idBoleto;
    private String numSerie;
    private String fila;
    private String asiento;
    private Double precio;
    private String estado;
    private String evento;
    private Timestamp fecha;

    public Boleto() {
        
    }

    public Boleto(Integer idBoleto, String numSerie, String fila, String asiento, Double precio, String estado, String evento, Timestamp fecha) {
        this.idBoleto = idBoleto;
        this.numSerie = numSerie;
        this.fila = fila;
        this.asiento = asiento;
        this.precio = precio;
        this.estado = estado;
        this.evento = evento;
        this.fecha = fecha;
    }

    public Boleto(String numSerie, String fila, String asiento, Double precio, String estado) {
        this.numSerie = numSerie;
        this.fila = fila;
        this.asiento = asiento;
        this.precio = precio;
        this.estado = estado;
    }

    public Integer getIdBoleto() {
        return idBoleto;
    }

    public void setIdBoleto(Integer idBoleto) {
        this.idBoleto = idBoleto;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public String getFila() {
        return fila;
    }

    public void setFila(String fila) {
        this.fila = fila;
    }

    public String getAsiento() {
        return asiento;
    }

    public void setAsiento(String asiento) {
        this.asiento = asiento;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
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
    
}
>>>>>>> 5c1a0f8a8c2778d59e747db91965ee1e5547f0bd
