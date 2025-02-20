/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package boletos.dtos;

import java.sql.Timestamp;

/**
 *
 * @author jalt2
 */
public class BoletoDTO {
    private Integer idBoleto;
    private String numSerie;
    private String fila;
    private String asiento;
    private Double precio;
    private String estado;
    private String evento;
    private Timestamp fecha;

    public BoletoDTO(Integer idBoleto, String numSerie, String fila, String asiento, Double precio, String estado, String evento, Timestamp fecha) {
        this.idBoleto = idBoleto;
        this.numSerie = numSerie;
        this.fila = fila;
        this.asiento = asiento;
        this.precio = precio;
        this.estado = estado;
        this.evento = evento;
        this.fecha = fecha;
    }

    public Integer getIdBoleto() {
        return idBoleto;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public String getFila() {
        return fila;
    }

    public String getAsiento() {
        return asiento;
    }

    public Double getPrecio() {
        return precio;
    }

    public String getEstado() {
        return estado;
    }

    public String getEvento() {
        return evento;
    }

    public Timestamp getFecha() {
        return fecha;
    }
    
    
}
